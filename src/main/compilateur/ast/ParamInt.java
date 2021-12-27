package compilateur.ast;
import compilateur.TDS.*;

public class ParamInt implements Ast{

    public <T> T accept(AstVisitor<T> visitor){
        return visitor.visit(this);
    }

    public <T> T accept(TdsVisitor<T> visitor, Tds tds){
        return visitor.visit(this, tds);
    }

    public Ast name;

    public ParamInt(Ast name) {
        this.name = name;
    }
}
