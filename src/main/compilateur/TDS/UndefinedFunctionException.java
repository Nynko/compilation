package compilateur.TDS;

public class UndefinedFunctionException extends SemanticErrorException{
    
    public UndefinedFunctionException(String functName, int line) {
        super(line, "Undefined function"+  functionName + "on line" + line + ".");
    }
}
