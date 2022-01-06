package compilateur.ast;

import compilateur.TDS.*;

public class Expr_et implements Ast {

    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public <T> T accept(TdsVisitor<T> visitor, Tds tds) {
        return visitor.visit(this, tds);
    }

    public Ast left;
    public Ast right;

    public Expr_et(Ast left, Ast right) {
        this.left = left;
        this.right = right;
    }
}
