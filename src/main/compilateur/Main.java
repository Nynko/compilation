package compilateur;

import java.io.IOException;
import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.antlr.v4.gui.TreeViewer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;

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

public class Main {

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
