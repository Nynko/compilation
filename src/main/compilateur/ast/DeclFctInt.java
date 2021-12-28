package compilateur.ast;
import compilateur.TDS.*;

public class DeclFctInt implements Ast{

    public int line;
    
    public <T> T accept(AstVisitor<T> visitor){
        return visitor.visit(this);
    }

    public <T> T accept(TdsVisitor<T> visitor, Tds tds){
        return visitor.visit(this, tds);
    }

    public Ast Idf;
    public Ast param;
    public Ast bloc;

    public DeclFctInt(Ast idf, Ast param, Ast bloc, int line) {
        this.Idf = idf;
        this.param = param;
        this.bloc = bloc;
        this.line = line;
    }
}
