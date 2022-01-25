package compilateur.tds;

public class TypeWarningException extends SemanticWarningException {

    public TypeWarningException(int line, String typeLeft, String typeRight) {
        super(line, "incompatible pointer to integer conversion assigning to '"+ typeLeft +"' from '"+ typeRight +"'" );
    }

}
