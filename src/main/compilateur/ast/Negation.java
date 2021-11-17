package compilateur.ast;

public class Negation implements Ast{

    public <T> T accept(AstVisitor<T> visitor){
        return visitor.visit(this);
    }

    public Ast noeud;

    public Negation(Ast noeud){
        this.noeud = noeud;
    }
    
}
