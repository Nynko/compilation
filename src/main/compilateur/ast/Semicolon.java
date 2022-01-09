package compilateur.ast;

import compilateur.TDS.*;

public class Semicolon implements Ast {

    public int line;

    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public <T> T accept(TdsVisitor<T> visitor, Tds tds) {
        return visitor.visit(this, tds);
    }

    public Semicolon(int line) {
        this.line = line;
    }
}
