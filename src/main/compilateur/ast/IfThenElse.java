package compilateur.ast;

import compilateur.TDS.*;

public class IfThenElse implements Ast {

    public int lineIf;
    public int lineElse;

    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public <T> T accept(TdsVisitor<T> visitor, Tds tds) {
        return visitor.visit(this, tds);
    }

    public Ast condition;
    public Ast thenBlock;
    public Ast elseBlock;

    public IfThenElse(Ast condition, Ast thenBlock, Ast elseBlock, int lineIf, int lineElse) {
        this.condition = condition;
        this.thenBlock = thenBlock;
        this.elseBlock = elseBlock;
        this.lineIf = lineIf;
        this.lineElse = lineElse;
    }

}
