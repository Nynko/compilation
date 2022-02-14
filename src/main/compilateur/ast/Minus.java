package compilateur.ast;

import compilateur.ARMGenerator.ARMVisitor;
import compilateur.tds.Tds;

public class Minus extends Operateur {

    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public <T> T accept(ARMVisitor<T> visitor, Tds tds){
        return visitor.visit(this, tds);
    }

    public Minus(Ast left, Ast right, int line) {
        super(left, right, line);
    }

}
