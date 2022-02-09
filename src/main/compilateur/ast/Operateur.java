package compilateur.ast;

import compilateur.tds.Tds;
import compilateur.tds.TdsVisitor;

public abstract class Operateur extends Line implements Ast {
    public Ast left;
    public Ast right;

    protected Operateur(Ast left, Ast right, int line) {
        super(line);
        this.left = left;
        this.right = right;
    }

    public <T> T accept(TdsVisitor<T> visitor, Tds tds) {
        return visitor.visit(this, tds);
    }

}
