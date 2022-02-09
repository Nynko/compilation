package compilateur.tds;

public class UndefinedSymboleException extends SemanticErrorException{
    
    public UndefinedSymboleException(String symName, int line) {
        super(line, "Undefined symbol " + symName);
    }
}
