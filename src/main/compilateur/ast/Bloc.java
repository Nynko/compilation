package compilateur.ast;

import java.util.ArrayList;

import compilateur.TDS.*;

public class Bloc implements Ast{

    public <T> T accept(AstVisitor<T> visitor){
        return visitor.visit(this);
    }

    public <T> T accept(TdsVisitor<T> visitor, Tds tds){
        return visitor.visit(this, tds);
    }

    public ArrayList<Ast> instList;

    public Bloc(ArrayList<Ast> instList) {
        this.instList = instList;
    }

}
