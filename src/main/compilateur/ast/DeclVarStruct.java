package compilateur.ast;

import java.util.ArrayList;

import compilateur.ARMGenerator.ARMVisitor;
import compilateur.tds.*;

public class DeclVarStruct extends AstNode implements Ast {

    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public <T> T accept(TdsVisitor<T> visitor, Tds tds) {
        return visitor.visit(this, tds);
    }

    public <T> T accept(ARMVisitor<T> visitor){
        return visitor.visit(this);
    }

    public ArrayList<Ast> idf;

    public DeclVarStruct(ArrayList<Ast> idf, int line) {
        super(line);
        this.idf = idf;
    }
}
