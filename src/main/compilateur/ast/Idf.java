package compilateur.ast;

import compilateur.TDS.*;

public class Idf implements Ast{

    public <T> T accept(AstVisitor<T> visitor){
        return visitor.visit(this);
    }

        public void accept(TdsVisitor visitor, Tds tds){
        visitor.visit(this, tds);
    }

    public String name;

    public Idf(String name) {
        this.name = name;
    }
}   
