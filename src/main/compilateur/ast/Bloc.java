package compilateur.ast;

import java.util.ArrayList;

public class Bloc implements Ast{

    public <T> T accept(AstVisitor<T> visitor){
        return visitor.visit(this);
    }

    public ArrayList<Ast> instList;

    public Bloc(ArrayList<Ast> instList) {
        this.instList = instList;
    }

}
