package compilateur.ast;

public class Expr_et implements Ast{
    
    public <T> T accept(AstVisitor<T> visitor){
        return visitor.visit(this);
    }
    
    Ast left;
    Ast right;

    public Expr_et(Ast left, Ast right) {
        this.left = left;
        this.right = right;
    }
}
