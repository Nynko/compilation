package compilateur.tds;

public class BadOperandTypeException extends SemanticErrorException{
    
    public BadOperandTypeException(String functName, int line) {
        super(line, "Bad operand type on "+  functName);
    }
}
