package compilateur.TDS;

public class TypeException extends SemanticErrorException {
    public  TypeException(int line, int oldDefininitionLine, String nameRSon, String typeLSon) {
        super(line, "Type of" + nameRson + "is not" + typeLSon + "on line" + line + ".");
    }
}