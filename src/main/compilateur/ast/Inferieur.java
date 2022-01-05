package compilateur.ast;

import compilateur.TDS.*;

public class Inferieur implements Ast {
    public <T> T accept(AstVisitor<T> visitor){
        return visitor.visit(this);
    }

        public void accept(TdsVisitor visitor, Tds tds){
        visitor.visit(this, tds);
    }
    
    public Ast left;
    public Ast right;

    public Inferieur(Ast left, Ast right) {
        this.left = left;
        this.right = right;
    }
}