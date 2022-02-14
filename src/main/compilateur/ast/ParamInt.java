package compilateur.ast;

import compilateur.ARMGenerator.ARMVisitor;
import compilateur.tds.*;

public class ParamInt extends Line implements Ast {

    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public <T> T accept(TdsVisitor<T> visitor, Tds tds) {
        return visitor.visit(this, tds);
    }

    public <T> T accept(ARMVisitor<T> visitor, Tds tds){
        return visitor.visit(this, tds);
    }

    public Ast name;

    public ParamInt(Ast name, int line) {
        super(line);
        this.name = name;
    }
}
