package compilateur.utils;

public class CompilerErrorException extends Exception {
    int line;

    public CompilerErrorException(int line, String message) {
        super(message);
        this.line = line;
    }
    
    public int getLine() {
        return this.line;
    }
}
