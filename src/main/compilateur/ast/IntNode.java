package compilateur.ast;
import compilateur.TDS.*;

public class IntNode implements Ast{

    public int line;

    public <T> T accept(AstVisitor<T> visitor){
        return visitor.visit(this);
    }

        public void accept(TdsVisitor visitor, Tds tds){
        visitor.visit(this, tds);
    }

    public int parseInt;

    public IntNode(int parseInt, int line) {
        this.parseInt = parseInt;
        this.line = line;
    }

}
