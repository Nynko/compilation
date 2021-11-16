package compilateur.ast;

import java.util.ArrayList;

public class Unaire implements Ast {
    
    public <T> T accept(AstVisitor<T> visitor){
        return visitor.visit(this);
    }

    public Ast noeud;
    public ArrayList<String> listeSignes;

    public Unaire(ArrayList<String> listeSignes,Ast noeud){
        this.noeud = noeud;
        this.listeSignes = listeSignes;
    }
        
}
