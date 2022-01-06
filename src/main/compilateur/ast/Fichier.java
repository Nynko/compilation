package compilateur.ast;

import compilateur.TDS.*;
import java.util.ArrayList;

public class Fichier implements Ast {
    public <T> T accept(AstVisitor<T> visitor){
        return visitor.visit(this);
    }

        public void accept(TdsVisitor visitor, Tds tds){
        visitor.visit(this, tds);
    }

    public ArrayList<Ast> instructions;

    public Fichier(ArrayList<Ast> inst) {
        this.instructions = inst;
    }
}
