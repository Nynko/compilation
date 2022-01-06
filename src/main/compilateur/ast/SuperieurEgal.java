package compilateur.ast;

import compilateur.TDS.*;

public class SuperieurEgal implements Ast {
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public <T> T accept(TdsVisitor<T> visitor, Tds tds) {
        return visitor.visit(this, tds);
    }

    public Ast left;
    public Ast right;

    public SuperieurEgal(Ast left, Ast right) {
        this.left = left;
        this.right = right;
    }
}
