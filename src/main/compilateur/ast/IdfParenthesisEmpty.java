package compilateur.ast;
import compilateur.TDS.*;

public class IdfParenthesisEmpty implements Ast{

    public <T> T accept(AstVisitor<T> visitor){
        return visitor.visit(this);
    }

    public <T> T accept(TdsVisitor<T> visitor, Tds tds){
        return visitor.visit(this, tds);
    }

    public Ast idf; 

    public IdfParenthesisEmpty(Ast idf) {
        this.idf = idf;
    }

}

