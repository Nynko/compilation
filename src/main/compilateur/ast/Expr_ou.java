package compilateur.ast;

import compilateur.TDS.*;

public class Expr_ou implements Ast {

    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public <T> T accept(TdsVisitor<T> visitor, Tds tds) {
        return visitor.visit(this, tds);
    }

    public Ast left;
    public Ast right;

    public Expr_ou(Ast left, Ast right) {
        this.left = left;
        this.right = right;
    }
}
