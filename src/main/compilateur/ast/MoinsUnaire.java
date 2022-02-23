package compilateur.ast;

import compilateur.ARMGenerator.ARMVisitor;
import compilateur.tds.*;

public class MoinsUnaire extends AstNode implements Ast {
    
    public <T> T accept(AstVisitor<T> visitor){
        return visitor.visit(this);
    }

    public <T> T accept(TdsVisitor<T> visitor, Tds tds) {
        return visitor.visit(this, tds);
    }
    


    public Ast noeud;

    public MoinsUnaire(Ast noeud, int line){
        super(line);
        this.noeud = noeud;
    }

}
