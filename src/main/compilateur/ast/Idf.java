package compilateur.ast;

import compilateur.TDS.*;

public class Idf implements Ast{

    public <T> T accept(AstVisitor<T> visitor){
        return visitor.visit(this);
    }

    public <T> T accept(TdsVisitor<T> visitor, Tds tds){
        return visitor.visit(this, tds);
    }

    public String name;

    public Idf(String name) {
        this.name = name;
    }
}   
