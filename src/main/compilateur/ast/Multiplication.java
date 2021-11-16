package compilateur.ast;

public class Multiplication implements Ast {
    public <T> T accept(AstVisitor<T> visitor){
        return visitor.visit(this);
    }
    
    Ast left;
    Ast right;

    public Multiplication(Ast left, Ast right) {
        this.left = left;
        this.right = right;
    }
    
}

