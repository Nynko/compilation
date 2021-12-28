package compilateur.ast;
import compilateur.TDS.*;

public class IfThen implements Ast {

    public int line;

    public <T> T accept(AstVisitor<T> visitor){
        return visitor.visit(this);
    }

    public <T> T accept(TdsVisitor<T> visitor, Tds tds){
        return visitor.visit(this, tds);
    }

    public Ast condition;
    public Ast thenBlock;

    public IfThen(Ast condition, Ast thenBlock, int line) {
        this.condition = condition;
        this.thenBlock = thenBlock;
        this.line = line;
    }

}
