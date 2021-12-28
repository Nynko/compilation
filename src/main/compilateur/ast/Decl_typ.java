package compilateur.ast;

import java.util.ArrayList;
import compilateur.TDS.*;

public class Decl_typ implements Ast {

    public int line;
    
    public <T> T accept(AstVisitor<T> visitor){
        return visitor.visit(this);
    }

    public <T> T accept(TdsVisitor<T> visitor, Tds tds){
        return visitor.visit(this, tds);
    }

    public Ast idf;
    public ArrayList<Ast> decl;

    public Decl_typ(Ast idf, ArrayList<Ast> decl, int line) {
        this.idf = idf;
        this.decl = decl;
        this.line = line;
    }

}
