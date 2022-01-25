package compilateur.ast;

import compilateur.tds.*;

public class MoinsUnaire implements Ast {

    public int line;
    
    public <T> T accept(AstVisitor<T> visitor){
        return visitor.visit(this);
    }

    public <T> T accept(TdsVisitor<T> visitor, Tds tds) {
        return visitor.visit(this, tds);
    }

    public Ast noeud;

    public MoinsUnaire(Ast noeud, int line){
        this.noeud = noeud;
        this.line = line;
    }

}
