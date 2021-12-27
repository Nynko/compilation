package compilateur.ast;
import compilateur.TDS.*;

public class Negation implements Ast{

    public <T> T accept(AstVisitor<T> visitor){
        return visitor.visit(this);
    }

    public <T> T accept(TdsVisitor<T> visitor, Tds tds){
        return visitor.visit(this, tds);
    }

    public Ast noeud;

    public Negation(Ast noeud){
        this.noeud = noeud;
    }
    
}
