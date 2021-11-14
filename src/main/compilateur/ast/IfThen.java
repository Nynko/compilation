package compilateur.ast;

public class IfThen implements Ast {
    public <T> T accept(AstVisitor<T> visitor){
        return visitor.visit(this);
    }

    public Ast condition;
    public Ast thenBlock;

    public IfThen(Ast condition, Ast thenBlock) {
        this.condition = condition;
        this.thenBlock = thenBlock;
    }

}
