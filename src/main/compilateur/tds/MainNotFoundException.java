package compilateur.tds;

public class MainNotFoundException extends SemanticErrorException{
    
    public MainNotFoundException() {
        super(-1, "Function main not found");
    }
    
}
