package compilateur.tds;

public class DivisionByZeroException extends SemanticErrorException{
    
    public DivisionByZeroException(int line) {
        super(line, "Division by zero");
    }
    
}
