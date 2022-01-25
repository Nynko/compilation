package compilateur.tds;

public class ReturnFunctionException extends SemanticErrorException{
    
    public ReturnFunctionException(int line) {
        super(line, "Function doesn't have a return");
    }
    
}
