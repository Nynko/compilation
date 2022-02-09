package compilateur.tds;

public class CodeNeverUseWarningException extends SemanticWarningException {

    public CodeNeverUseWarningException(int line) {
        super(line, "Remove this part, code is never used");
    }

}
