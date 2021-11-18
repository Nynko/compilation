package compilateur.ast;

import java.util.ArrayList;

public class Decl_typ implements Ast {
    
    public <T> T accept(AstVisitor<T> visitor){
        return visitor.visit(this);
    }

    public Ast idf;
    public ArrayList<Ast> decl;

    public Decl_typ(Ast idf, ArrayList<Ast> decl) {
        this.idf = idf;
        this.decl = decl;
    }

}
