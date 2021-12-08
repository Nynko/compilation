package compilateur.TDS;

public class SymbolAlreadyExistsException extends Exception {
    public SymbolAlreadyExistsException(String symName, int line) {
        super("Symbole " + symName + " is already defined on line " + Integer.toString(line));
    }
}
