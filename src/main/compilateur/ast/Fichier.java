package compilateur.ast;

import compilateur.TDS.*;
import java.util.ArrayList;

public class Fichier implements Ast {
    public <T> T accept(AstVisitor<T> visitor){
        return visitor.visit(this);
    }

    public <T> T accept(TdsVisitor<T> visitor, Tds tds){
        return visitor.visit(this, tds);
    }

    public ArrayList<Ast> instructions;

    public Fichier(ArrayList<Ast> inst) {
        this.instructions = inst;
    }
}
