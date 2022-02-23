package compilateur.ast;

import java.util.ArrayList;

import compilateur.ARMGenerator.ARMVisitor;
import compilateur.tds.*;

public class Decl_typ extends AstNode implements Ast {

    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public <T> T accept(TdsVisitor<T> visitor, Tds tds) {
        return visitor.visit(this, tds);
    }



    public Ast idf;
    public ArrayList<Ast> decl;

    public Decl_typ(Ast idf, ArrayList<Ast> decl, int line) {
        super(line);
        this.idf = idf;
        this.decl = decl;
    }

}
