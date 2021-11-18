package compilateur.ast;

import java.util.ArrayList;

public class DeclVarInt implements Ast{
    public <T> T accept(AstVisitor<T> visitor){
        return visitor.visit(this);
    }

    public ArrayList<Ast> idf;

    public DeclVarInt (ArrayList<Ast> idf) {
        this.idf = idf;
    }
    
}
