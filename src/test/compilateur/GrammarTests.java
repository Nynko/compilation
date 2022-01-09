package compilateur;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.DynamicTest;


import static org.junit.jupiter.api.Assertions.assertEquals;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;


import compilateur.grammar.*;


public class GrammarTests {
    @Test
    public void goodTest() throws IOException {
        assertEquals(true, testFile("./examples/good.c"));
    }

    @Test
    public void badTest() throws IOException {
        assertEquals(false, testFile("./examples/bad.c"));
    }

    @Test
    public void emptyTest() throws IOException {
        assertEquals(true, testFile("./examples/empty.c"));
    }

    @TestFactory
    Stream<DynamicTest> dynamicTestsFromStreamInJava8() {

        List<String> testFiles = new ArrayList<String>();
        File[] files = new File("./examples/").listFiles();

        Pattern pattern = Pattern.compile(".*\\.((Good)|(Bad))\\.c$");
        for (File file : files) {
            if (file.isFile()) {
                Matcher m = pattern.matcher(file.getName());
                if(m.matches()) {
                    testFiles.add(file.getName());
                }
            }
        }
            
        return testFiles.stream()
        .map((file) -> {
            String[] parts = file.split("\\.");
            System.out.println(parts.length);
            return DynamicTest.dynamicTest(parts[0].toString() + parts[1].toString() + "Test()", 
                () -> {
                    assertEquals(parts[1].equals("Good") ? true : false, testFile("./examples/" + file));
                });
        });
    }

    public boolean testFile(String testFile) throws IOException {
        //chargement du fichier et construction du parser
        CharStream input = CharStreams.fromFileName(testFile);
        
        circLexer lexer = new circLexer(input); 

        ParserErrorListener errList = new ParserErrorListener();

        lexer.removeErrorListeners();
        lexer.addErrorListener(errList);

        CommonTokenStream stream = new CommonTokenStream(lexer);
        circParser parser = new circParser(stream);

        parser.removeErrorListeners();
        parser.addErrorListener(errList);

        parser.fichier();

        return errList.getAggregator().noError();
    }
}
