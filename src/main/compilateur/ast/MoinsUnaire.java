package compilateur.ast;

public class MoinsUnaire implements Ast {
    
    public <T> T accept(AstVisitor<T> visitor){
        return visitor.visit(this);
    }

    public Ast noeud;

    public MoinsUnaire(Ast noeud){
        this.noeud = noeud;
    }

}
