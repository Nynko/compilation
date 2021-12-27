package compilateur.ast;
import compilateur.TDS.*;

public class While implements Ast{

    public <T> T accept(AstVisitor<T> visitor){
        return visitor.visit(this);
    }

    public <T> T accept(TdsVisitor<T> visitor, Tds tds){
        return visitor.visit(this, tds);
    }
    
    public Ast condition;
    public Ast doBlock;

    public While(Ast condition, Ast doBlock) {
        this.condition = condition;
        this.doBlock = doBlock;
    }

}
