package compilateur.ast;

import compilateur.TDS.Tds;
import compilateur.TDS.TdsVisitor;

public abstract class Operateur implements Ast {
    public Ast left;
    public Ast right;
    public int line;

    protected Operateur(Ast left, Ast right, int line) {
        this.left = left;
        this.right = right;
        this.line = line;
    }

    public <T> T accept(TdsVisitor<T> visitor, Tds tds) {
        return visitor.visit(this, tds);
    }

}
