package compilateur.ast;

import compilateur.TDS.*;
import java.util.ArrayList;

public class IdfParenthesis implements Ast {

    public int line;

    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public <T> T accept(TdsVisitor<T> visitor, Tds tds) {
        return visitor.visit(this, tds);
    }

    public Ast idf;
    public ArrayList<Ast> exprList;

    public IdfParenthesis(Ast idf, ArrayList<Ast> exprList, int line) {
        this.idf = idf;
        this.exprList = exprList;
        this.line = line;
    }

}
