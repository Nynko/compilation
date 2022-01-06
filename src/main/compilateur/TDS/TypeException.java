package compilateur.TDS;

public class TypeException extends SemanticErrorException {
    public  TypeException(int line, String typeRSon, String typeLSon) {
        super(line, "Type of" + typeRSon + "is not" + typeLSon + "on line" + line + ".");
    }
}