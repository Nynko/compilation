package compilateur.ast;
import compilateur.TDS.*;

public class CharNode implements Ast{

    public int line;
    
    public <T> T accept(AstVisitor<T> visitor){
        return visitor.visit(this);
    }

        public void accept(TdsVisitor visitor, Tds tds){
        visitor.visit(this, tds);
    }

    public String string; 

    public CharNode(String string, int line) {
        this.string = string;
        this.line = line;
    }

}
