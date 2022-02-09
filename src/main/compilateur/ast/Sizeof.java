package compilateur.ast;

import compilateur.tds.*;

public class Sizeof extends Line implements Ast {

    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public <T> T accept(TdsVisitor<T> visitor, Tds tds) {
        return visitor.visit(this, tds);
    }

    public Ast name;

    public Sizeof(Ast name, int line) {
        super(line);
        this.name = name;
    }
}