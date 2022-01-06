package compilateur.ast;
import compilateur.TDS.*;

public class Multiplication implements Ast {

    public int line;

    public <T> T accept(AstVisitor<T> visitor){
        return visitor.visit(this);
    }

        public void accept(TdsVisitor visitor, Tds tds){
        visitor.visit(this, tds);
    }
    
    public Ast left;
    public Ast right;

    public Multiplication(Ast left, Ast right, int line) {
        this.left = left;
        this.right = right;
        this.line = line;
    }
    
}

