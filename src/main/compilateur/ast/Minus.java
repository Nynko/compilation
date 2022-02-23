package compilateur.ast;

import compilateur.ARMGenerator.;

public class Minus extends Operateur {

    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visit(this);
    }



    public Minus(Ast left, Ast right, int line) {
        super(left, right, line);
    }

}
