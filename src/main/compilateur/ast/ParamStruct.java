package compilateur.ast;

import compilateur.ARMGenerator.ARMVisitor;
import compilateur.tds.*;

public class ParamStruct extends AstNode implements Ast {

    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public <T> T accept(TdsVisitor<T> visitor, Tds tds) {
        return visitor.visit(this, tds);
    }

    public <T> T accept(ARMVisitor<T> visitor){
        return visitor.visit(this);
    }

    public Ast idf0;
    public Ast idf1;

    public ParamStruct(Ast idf0, Ast idf1, int line) {
        super(line);
        this.idf0 = idf0;
        this.idf1 = idf1;
    }
}
