package compilateur.TDS;

public class UndefinedSymboleException extends SemanticErrorException{
    
    public UndefinedSymboleException(String symLeftName, String symRightName, int line) {
        super(line, "Undefined symbol" + symLeftName + "in" + symRightName + "on line" + line + ".");
    }
}
