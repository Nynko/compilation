package compilateur.ast;

import java.util.ArrayList;

public class Fichier implements Ast {
    public <T> T accept(AstVisitor<T> visitor){
        return visitor.visit(this);
    }

    public ArrayList<Ast> instructions;

    public Fichier(ArrayList<Ast> inst) {
        this.instructions = inst;
    }
}
