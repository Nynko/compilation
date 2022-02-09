package compilateur.tds;

public class VarNotInitializedException extends SemanticErrorException{
    
    public VarNotInitializedException(int lineErreur, String name, int line) {
        super(lineErreur, "Variable " + name + " declared on line " + line + " is not initialized");
    }
    
}
