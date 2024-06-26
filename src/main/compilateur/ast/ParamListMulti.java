package compilateur.ast;

import java.util.ArrayList;


import compilateur.tds.*;

public class ParamListMulti extends AstNode implements Ast {

    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public <T> T accept(TdsVisitor<T> visitor, Tds tds) {
        return visitor.visit(this, tds);
    }



    public ArrayList<Ast> paramList;

    public ParamListMulti(ArrayList<Ast> paramList) {
        super(-1);
        this.paramList = paramList;
    }
}
