package compilateur.TDS;
import compilateur.utils.CompilerErrorException;

public abstract class SemanticErrorException extends CompilerErrorException {
    public SemanticErrorException(int line, String message) {
        super(line, "ERROR: " + message);
    }
}
