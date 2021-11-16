package compilateur.ast;

public class Superieur implements Ast{
    public <T> T accept(AstVisitor<T> visitor){
        return visitor.visit(this);
    }
    
    Ast left;
    Ast right;

    public Superieur(Ast left, Ast right) {
        this.left = left;
        this.right = right;
    }
}
