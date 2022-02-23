package compilateur.ast;

import compilateur.ARMGenerator.ARMVisitor;

public class Expr_et extends Operateur {

    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visit(this);
    }



    public Expr_et(Ast left, Ast right, int line) {
        super(left, right, line);
    }
}
