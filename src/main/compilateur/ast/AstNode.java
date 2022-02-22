package compilateur.ast;
import compilateur.tds.*;

public abstract class AstNode {
    public int line;
    private Tds tds;


    protected AstNode(int line) {
        this.line = line;
    }


    public Tds getTds() {
        return this.tds;
    }


    public void setTds(Tds tds) {
        this.tds = tds;
    }
}
