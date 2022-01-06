package compilateur.ast;

import compilateur.TDS.*;

public class Sizeof implements Ast{

    public int line;

    public <T> T accept(AstVisitor<T> visitor){
        return visitor.visit(this);
    }

        public void accept(TdsVisitor visitor, Tds tds){
        visitor.visit(this, tds);
    }

    public Ast name;

    public Sizeof(Ast name, int line) {
        this.name = name;
        this.line = line;
    }
}