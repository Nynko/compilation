package compilateur.ast;

import java.util.ArrayList;

public class IdfParenthesis implements Ast{

    public <T> T accept(AstVisitor<T> visitor){
        return visitor.visit(this);
    }

    public Ast idf;
    public ArrayList<Ast> exprList;   

    public IdfParenthesis(Ast idf, ArrayList<Ast> exprList) {
        this.idf = idf;
        this.exprList = exprList;
    }

}
