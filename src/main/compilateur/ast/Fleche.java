package compilateur.ast;

import java.util.ArrayList;

public class Fleche implements Ast {
    public <T> T accept(AstVisitor<T> visitor){
        return visitor.visit(this);
    }

    public Ast left;
    public ArrayList<Ast> listeIdf;

    public Fleche(Ast left,ArrayList<Ast> listeIdf) {
        this.left = left;
        this.listeIdf = listeIdf;
    }


}
