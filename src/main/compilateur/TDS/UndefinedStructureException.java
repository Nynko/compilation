package compilateur.TDS;

public class UndefinedStructureException extends SemanticErrorException {
    
    public UndefinedStructureException(String symName, int line) {
        super(line, "Structure " + symName + "on line" + line + "is not defined.");
    }
}
