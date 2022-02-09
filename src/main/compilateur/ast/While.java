package compilateur.ast;

import compilateur.tds.*;

public class While extends Line implements Ast {

    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public <T> T accept(TdsVisitor<T> visitor, Tds tds) {
        return visitor.visit(this, tds);
    }

    public Ast condition;
    public Ast doBlock;

    public While(Ast condition, Ast doBlock, int line) {
        super(line);
        this.condition = condition;
        this.doBlock = doBlock;
    }

}
