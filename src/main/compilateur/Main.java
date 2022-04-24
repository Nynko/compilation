package compilateur;

import java.io.FileOutputStream;
import java.io.IOException;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;

import compilateur.ARMGenerator.ARMGenerator;
import compilateur.ARMGenerator.TrueARM64Generator;
import compilateur.ast.Ast;
import compilateur.ast.AstCreator;
import compilateur.grammar.circLexer;
import compilateur.grammar.circParser;
import compilateur.grammar.circParser.FichierContext;
import compilateur.graphviz.GraphVizTdsVisitor;
import compilateur.graphviz.GraphVizVisitor;
import compilateur.tds.Tds;
import compilateur.tds.TdsCreator;
import compilateur.utils.ErrorAggregator;
import compilateur.utils.Os;

public class Main {

    private static void outputARM(String str){
        try (FileOutputStream output = new FileOutputStream("./out/ARM.s")) {
            String buffer = str;
            byte[] strToBytes = buffer.getBytes();
            try {
                output.write(strToBytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void outputARMinBin(String str, String testFile){
        try (FileOutputStream output = new FileOutputStream("./bin/ARM/ARMs/" + testFile.split("/")[2].split("\\.")[0].split("_")[1] + ".s")) {
            String buffer = str;
            byte[] strToBytes = buffer.getBytes();
            try {
                output.write(strToBytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        if (args.length < 1) {
            System.out.println("Error : Expected 1 argument filepath, found 0");
            return;
        }

        String testFile = args[0];

        try {

            // chargement du fichier et construction du parser
            CharStream input = CharStreams.fromFileName(testFile);
            circLexer lexer = new circLexer(input);

            ParserErrorListener errList = new ParserErrorListener();

            lexer.removeErrorListeners();
            lexer.addErrorListener(errList);

            CommonTokenStream stream = new CommonTokenStream(lexer);
            circParser parser = new circParser(stream);

            parser.removeErrorListeners();
            parser.addErrorListener(errList);

            FichierContext program = parser.fichier();

            // code d'affichage de l'arbre syntaxique
            // JFrame frame = new JFrame("Antlr AST");
            // JPanel panel = new JPanel();
            // TreeViewer viewer = new TreeViewer(Arrays.asList(
            // parser.getRuleNames()), program);
            // viewer.setScale(1.5); // Scale a little
            // panel.add(viewer);
            // frame.add(panel);
            // frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            // frame.pack();
            // frame.setVisible(true);

            // Récupération des erreurs syntaxiques et lexicales
            ErrorAggregator agg = errList.getAggregator();

            if (agg.noError()) {
                // Visiteur de création de l'AST + création de l'AST
                AstCreator creator = new AstCreator();
                Ast ast = program.accept(creator);

                if (agg.noError()) {
                    // Visiteur de représentation graphique + appel
                    GraphVizVisitor graphViz = new GraphVizVisitor();
                    ast.accept(graphViz);
                    graphViz.dumpGraph("./out/tree.dot");

                    // TDS
                    GraphVizTdsVisitor graphVizTds = new GraphVizTdsVisitor();
                    Tds tds = new Tds("prog");
                    TdsCreator tdsCreator = new TdsCreator();
                    tdsCreator.setErrorAggregator(agg);
                    ast.accept(tdsCreator, tds);

                    graphVizTds.createGraph(tds);
                    graphVizTds.dumpGraph("./out/tds.dot");

                    // ARM
                    TrueARM64Generator arm = new TrueARM64Generator(Os.getOS());
                    if(testFile.contains("_sl.c")){
                        arm.sl = true;
                    }
                    String str = ast.accept(arm);
                    outputARM(str);
                    outputARMinBin(str, testFile);
                }
                

            } else {
                System.out.println("erreur syntaxique");
            }
            System.out.println("==== Erreurs ====");
            agg.printErrors();
            System.out.println("==== Erreurs ====\n\n");

        } catch (IOException | RecognitionException e) {
            e.printStackTrace();
        }

    }

}
