package compilateur.ast;
import compilateur.TDS.*;

public class Negation implements Ast{

    public <T> T accept(AstVisitor<T> visitor){
        return visitor.visit(this);
    }

        public void accept(TdsVisitor visitor, Tds tds){
        visitor.visit(this, tds);
    }

    public Ast noeud;

    public Negation(Ast noeud){
        this.noeud = noeud;
    }
    
}
