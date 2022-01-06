package compilateur.TDS;

public class UndefinedFunctionException extends SemanticErrorException{
    
    public UndefinedFunctionException(String functName, int line) {
        super(line, "Undefined function"+  functName + "on line" + line + ".");
    }
}
