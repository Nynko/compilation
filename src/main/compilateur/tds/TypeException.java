package compilateur.tds;

public class TypeException extends SemanticErrorException {
    public  TypeException(int line, String typeRSon, String typeLSon) {
        super(line, "Type found " + typeRSon + ", expected " + typeLSon);
    }
}