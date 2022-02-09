package compilateur.ast;

import compilateur.tds.*;

public class IfThen extends Line implements Ast {

    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public <T> T accept(TdsVisitor<T> visitor, Tds tds) {
        return visitor.visit(this, tds);
    }

    public Ast condition;
    public Ast thenBlock;

    public IfThen(Ast condition, Ast thenBlock, int line) {
        super(line);
        this.condition = condition;
        this.thenBlock = thenBlock;
    }

}
