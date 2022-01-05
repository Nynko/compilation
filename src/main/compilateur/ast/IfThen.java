package compilateur.ast;
import compilateur.TDS.*;

public class IfThen implements Ast {

    public int line;

    public <T> T accept(AstVisitor<T> visitor){
        return visitor.visit(this);
    }

        public void accept(TdsVisitor visitor, Tds tds){
        visitor.visit(this, tds);
    }

    public Ast condition;
    public Ast thenBlock;

    public IfThen(Ast condition, Ast thenBlock, int line) {
        this.condition = condition;
        this.thenBlock = thenBlock;
        this.line = line;
    }

}
