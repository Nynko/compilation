package compilateur.ast;

import java.util.ArrayList;

public class DeclVarStruct implements Ast {
    
    public <T> T accept(AstVisitor<T> visitor){
        return visitor.visit(this);
    }

    public ArrayList<Ast> idf;

    public DeclVarStruct(ArrayList<Ast> idf) {
        this.idf = idf;
    }
}
