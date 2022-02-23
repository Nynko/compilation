package compilateur.ast;

import compilateur.ARMGenerator.;

public class Multiplication extends Operateur {

    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visit(this);
    }



    public Multiplication(Ast left, Ast right, int line) {
        super(left, right, line);
    }

}
