package compilateur.ast;
import compilateur.TDS.*;

public class While implements Ast{

    public int line;

    public <T> T accept(AstVisitor<T> visitor){
        return visitor.visit(this);
    }

        public void accept(TdsVisitor visitor, Tds tds){
        visitor.visit(this, tds);
    }
    
    public Ast condition;
    public Ast doBlock;

    public While(Ast condition, Ast doBlock, int line) {
        this.condition = condition;
        this.doBlock = doBlock;
        this.line = line;
    }

}
