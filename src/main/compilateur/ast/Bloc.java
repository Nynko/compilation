package compilateur.ast;

import java.util.ArrayList;

import compilateur.TDS.*;

public class Bloc implements Ast{

    public int line;

    public <T> T accept(AstVisitor<T> visitor){
        return visitor.visit(this);
    }

    public void accept(TdsVisitor visitor, Tds tds){
        visitor.visit(this, tds);
    }

    public ArrayList<Ast> instList;

    public Bloc(ArrayList<Ast> instList, int line) {
        this.instList = instList;
        this.line = line;
    }

}
