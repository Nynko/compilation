package compilateur.ast;
import compilateur.TDS.*;

public class CharNode implements Ast{

    public <T> T accept(AstVisitor<T> visitor){
        return visitor.visit(this);
    }

    public <T> T accept(TdsVisitor<T> visitor, Tds tds){
        return visitor.visit(this, tds);
    }

    public String string; 

    public CharNode(String string) {
        this.string = string;
    }

}
