package compilateur.ast;

public class While implements Ast{

    public <T> T accept(AstVisitor<T> visitor){
        return visitor.visit(this);
    }

    public Ast condition;
    public Ast doBlock;

    public While(Ast condition, Ast doBlock) {
        this.condition = condition;
        this.doBlock = doBlock;
    }

}
