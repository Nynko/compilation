package compilateur;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import compilateur.utils.ErrorAggregator;
import compilateur.utils.SyntaxErrorException;

public class ParserErrorListener extends BaseErrorListener {

    public ErrorAggregator aggregator = new ErrorAggregator();
 
    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e)
        throws ParseCancellationException {
            SyntaxErrorException err = new SyntaxErrorException(line, charPositionInLine + " " + msg);
            this.aggregator.addError(err);
    }

    public ErrorAggregator getAggregator() {
        return this.aggregator;
    }
}
