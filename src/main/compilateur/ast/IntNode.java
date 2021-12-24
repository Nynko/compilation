package compilateur.ast;
import compilateur.TDS.*;

public class IntNode implements Ast{

    public <T> T accept(AstVisitor<T> visitor){
        return visitor.visit(this);
    }

    public <T> T accept(TdsVisitor<T> visitor, Tds tds){
        return visitor.visit(this, tds);
    }

    public int parseInt;

    public IntNode(int parseInt) {
        this.parseInt = parseInt;
    }

}
