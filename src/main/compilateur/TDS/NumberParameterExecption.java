package compilateur.TDS;

public class NumberParameterExecption extends SemanticErrorException{
    
    public NumberParameterExecption(String name, int line, int expected, int found) {
        super(line, "In function " + name + " found " +  found + " parameters, expected " + expected);
    }
}
