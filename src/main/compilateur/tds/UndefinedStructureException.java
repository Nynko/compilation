package compilateur.tds;

public class UndefinedStructureException extends SemanticErrorException {
    
    public UndefinedStructureException(String symName, int line) {
        super(line, "Struct " + symName +  " on line " + line + " is not defined.");
    }
}
