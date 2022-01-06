package compilateur.ast;

import compilateur.TDS.*;

public class MoinsUnaire implements Ast {

    public int line;
    
    public <T> T accept(AstVisitor<T> visitor){
        return visitor.visit(this);
    }

        public void accept(TdsVisitor visitor, Tds tds){
        visitor.visit(this, tds);
    }

    public Ast noeud;

    public MoinsUnaire(Ast noeud, int line){
        this.noeud = noeud;
        this.line = line;
    }

}
