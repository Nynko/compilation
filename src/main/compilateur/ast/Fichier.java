package compilateur.ast;

import java.util.ArrayList;

import compilateur.ARMGenerator.ARMVisitor;
import compilateur.tds.*;

public class Fichier implements Ast {
    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public <T> T accept(TdsVisitor<T> visitor, Tds tds) {
        return visitor.visit(this, tds);
    }



    public ArrayList<Ast> instructions;

    public Fichier(ArrayList<Ast> inst) {
        this.instructions = inst;
    }
}
