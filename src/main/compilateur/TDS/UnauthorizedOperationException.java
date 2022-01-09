package compilateur.TDS;

public class UnauthorizedOperationException extends SemanticErrorException{
    
    public UnauthorizedOperationException(int line) {
        super(line, "Unauthorized operation on line " + line + ".");
    }
    
}
