package compilateur.ast;
import compilateur.TDS.*;

public class IdfParenthesisEmpty implements Ast{

    public int line;

    public <T> T accept(AstVisitor<T> visitor){
        return visitor.visit(this);
    }

        public void accept(TdsVisitor visitor, Tds tds){
        visitor.visit(this, tds);
    }

    public Ast idf; 

    public IdfParenthesisEmpty(Ast idf, int line) {
        this.idf = idf;
        this.line = line;
    }

}

