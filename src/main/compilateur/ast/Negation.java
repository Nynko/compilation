package compilateur.ast;


import compilateur.tds.*;

public class Negation extends AstNode implements Ast {

    public <T> T accept(AstVisitor<T> visitor){
        return visitor.visit(this);
    }

    public <T> T accept(TdsVisitor<T> visitor, Tds tds) {
        return visitor.visit(this, tds);
    }



    public Ast noeud;

    public Negation(Ast noeud, int line){
        super(line);
        this.noeud = noeud;
    }

}
