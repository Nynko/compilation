package compilateur.ast;

public class InferieurEgal implements Ast{
    public <T> T accept(AstVisitor<T> visitor){
        return visitor.visit(this);
    }
    
    Ast left;
    Ast right;

    public InferieurEgal(Ast left, Ast right) {
        this.left = left;
        this.right = right;
    }
}
