package test;

import java.io.Console;
import java.io.IOException;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.antlr.v4.gui.TreeViewer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.antlr.v4.runtime.CommonTokenStream;


import main.grammar.*;
import main.grammar.circParser.FichierContext;

import main.ThrowingErrorListener;


public class GrammarTests {
    @Test
    public void goodTest() throws IOException {
        assertEquals(testFile("./examples/good.exp"), true);
    }

    @Test
    public void badTest() throws IOException {
        assertEquals(testFile("./examples/bad.exp"), false);
    }

    @Test
    public void emptyTest() throws IOException {
        assertEquals(testFile("./examples/empty.exp"), true);
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

            FichierContext program = parser.fichier();

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
