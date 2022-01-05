package compilateur.ast;
import compilateur.TDS.*;

public class Return implements Ast{

    public int line;

    public <T> T accept(AstVisitor<T> visitor){
        return visitor.visit(this);
    }

        public void accept(TdsVisitor visitor, Tds tds){
        visitor.visit(this, tds);
    }

    public Ast expr;

    public Return(Ast expr, int line) {
        this.expr = expr;
        this.line = line;
    }

}
