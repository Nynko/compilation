package compilateur.TDS;

public class SymbolAlreadyExistsException extends SemanticErrorException {
    public SymbolAlreadyExistsException(String symName, int line, int oldDefininitionLine) {
        super(line, "Symbole " + symName + " is already defined on line " + Integer.toString(oldDefininitionLine) + ".");
    }
}
