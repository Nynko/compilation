package compilateur.TDS;

public class DivisionByZeroException extends SemanticErrorException{
    
    public DivisionByZeroException(int line) {
        super(line, "Division by zero");
    }
    
}
