package compilateur.ast;

import java.util.ArrayList;
import compilateur.TDS.*;

public class DeclVarInt implements Ast{

    public int line;

    public <T> T accept(AstVisitor<T> visitor){
        return visitor.visit(this);
    }

        public void accept(TdsVisitor visitor, Tds tds){
        visitor.visit(this, tds);
    }
    
    public ArrayList<Ast> idf;

    public DeclVarInt (ArrayList<Ast> idf, int line) {
        this.idf = idf;
        this.line = line;
    }
    
}
