package compilateur.TDS;

public class ReturnFunctionException extends SemanticErrorException{
    
    public ReturnFunctionException(int line) {
        super(line, "function doesn't have a return");
    }
    
}
