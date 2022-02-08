package compilateur.tds;

public class NumberParameterException extends SemanticErrorException{
    
    public NumberParameterException(String name, int line, int expected, int found) {
        super(line, "In function " + name + " found " +  found + " parameters, expected " + expected);
    }
}
