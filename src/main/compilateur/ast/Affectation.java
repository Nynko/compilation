package compilateur.ast;

import compilateur.tds.Tds;
import compilateur.tds.TdsVisitor;

public class Affectation extends Operateur {

    public <T> T accept(AstVisitor<T> visitor){
        return visitor.visit(this);
    }

    @Override
    public <T> T accept(TdsVisitor<T> visitor, Tds tds) {
        return visitor.visit(this, tds);
    }

    public Affectation(Ast left, Ast right, int line) {
        super(left, right, line);
    }
}
