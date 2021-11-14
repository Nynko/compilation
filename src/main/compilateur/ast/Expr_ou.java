package compilateur.ast;


public class Expr_ou implements Ast{

    public <T> T accept(AstVisitor<T> visitor){
        return visitor.visit(this);
    }

    Ast left;
    Ast right;

    public Expr_ou(Ast left, Ast right) {
        this.left = left;
        this.right = right;
    }
}
