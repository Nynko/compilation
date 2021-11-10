package compilateur;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.DynamicTest;


import static org.junit.jupiter.api.Assertions.assertEquals;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.misc.ParseCancellationException;


import compilateur.grammar.*;


public class GrammarTests {
    @Test
    public void goodTest() throws IOException {
        assertEquals(true, testFile("./examples/good.exp"));
    }

    @Test
    public void badTest() throws IOException {
        assertEquals(false, testFile("./examples/bad.exp"));
    }

    @Test
    public void emptyTest() throws IOException {
        assertEquals(true, testFile("./examples/empty.exp"));
    }

    @TestFactory
    Stream<DynamicTest> dynamicTestsFromStreamInJava8() {
        
        List<String> testFiles = Arrays.asList(
            "Caractere.Good.exp",
            "DeclType.Bad.exp",
            "DeclType.Good.exp",
            "DeclType2.Bad.exp",
            "Fct.Good.exp",
            "noFct.Bad.exp",
            "nomIDF.Good.exp"
        );
            
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
        try {
            //chargement du fichier et construction du parser
            CharStream input = CharStreams.fromFileName(testFile);
            
            circLexer lexer = new circLexer(input); 

            lexer.removeErrorListeners();
            lexer.addErrorListener(ThrowingErrorListener.INSTANCE);

            CommonTokenStream stream = new CommonTokenStream(lexer);
            circParser parser = new circParser(stream);

            parser.removeErrorListeners();
            parser.addErrorListener(ThrowingErrorListener.INSTANCE);

            parser.fichier();

            return true;
        } 
        catch (RecognitionException e) {
            e.printStackTrace();
            return false;
        } catch (ParseCancellationException e) {
            return false;
        }
    }
}
