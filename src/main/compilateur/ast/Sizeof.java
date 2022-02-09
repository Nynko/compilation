package compilateur.ast;

import compilateur.tds.*;

public class Sizeof implements Ast {

    public int line;

    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public <T> T accept(TdsVisitor<T> visitor, Tds tds) {
        return visitor.visit(this, tds);
    }

    public Ast name;

    public Sizeof(Ast name, int line) {
        this.name = name;
        this.line = line;
    }
}