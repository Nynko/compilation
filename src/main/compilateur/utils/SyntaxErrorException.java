package compilateur.utils;

public class SyntaxErrorException extends CompilerErrorException {
    public SyntaxErrorException(int line, String message) {
        super(line, message);
    }
}
