package compilateur.ast;

public class Return implements Ast{

    public <T> T accept(AstVisitor<T> visitor){
        return visitor.visit(this);
    }

    public Ast expr;

    public Return(Ast expr) {
        this.expr = expr;
    }

}
