package compilateur.ast;
import compilateur.TDS.*;

public class ParamStruct implements Ast{

    public int line;

    public <T> T accept(AstVisitor<T> visitor){
        return visitor.visit(this);
    }

        public void accept(TdsVisitor visitor, Tds tds){
        visitor.visit(this, tds);
    }

    public Ast idf0;
    public Ast idf1;

    public ParamStruct(Ast idf0, Ast idf1, int line) {
        this.idf0 = idf0;
        this.idf1 = idf1;
        this.line = line;
    }
}
