package compilateur.ast;


import java.util.ArrayList;

public class ParamListMulti implements Ast{

    public <T> T accept(AstVisitor<T> visitor){
        return visitor.visit(this);
    }

    public ArrayList<Ast> paramList;

    public ParamListMulti(ArrayList<Ast> paramList) {
        this.paramList = paramList;
    }
}
