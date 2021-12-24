package compilateur.ast;
import compilateur.TDS.*;

public class Return implements Ast{

    public <T> T accept(AstVisitor<T> visitor){
        return visitor.visit(this);
    }

    public <T> T accept(TdsVisitor<T> visitor, Tds tds){
        return visitor.visit(this, tds);
    }

    public Ast expr;

    public Return(Ast expr) {
        this.expr = expr;
    }

}
