package compilateur.TDS;

import compilateur.utils.CompilerErrorException;

/**
 * WarningEception
 */
public abstract class SemanticWarningException extends CompilerErrorException {

    protected SemanticWarningException(int line, String message) {
        super(line, "WARNING: " + message);
    }

    
}