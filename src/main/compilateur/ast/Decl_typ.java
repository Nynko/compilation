package compilateur.ast;

import java.util.ArrayList;

public class Decl_typ implements Ast {
    
    public <T> T accept(AstVisitor<T> visitor){
        return visitor.visit(this);
    }

    Ast idf;
    ArrayList<Ast> decl;

    public Decl_typ(Ast idf, ArrayList<Ast> decl) {
        this.idf = idf;
        this.decl = decl;
    }

}
