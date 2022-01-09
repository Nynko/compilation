package compilateur.ast;

import compilateur.TDS.*;

public class InferieurEgal implements Ast{

    public int line;

    public <T> T accept(AstVisitor<T> visitor){
        return visitor.visit(this);
    }

    public <T> T accept(TdsVisitor<T> visitor, Tds tds) {
        return visitor.visit(this, tds);
    }

    public Ast left;
    public Ast right;

    public InferieurEgal(Ast left, Ast right, int line) {
        this.left = left;
        this.right = right;
        this.line = line;
    }
}
