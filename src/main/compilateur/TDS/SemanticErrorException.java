package compilateur.TDS;
import compilateur.utils.CompilerErrorException;

public abstract class SemanticErrorException extends CompilerErrorException {
    protected SemanticErrorException(int line, String message) {
        super(line, "ERROR: " + message);
    }
}
