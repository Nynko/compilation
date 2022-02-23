package compilateur.ast;

import compilateur.ARMGenerator.ARMVisitor;
import compilateur.tds.Tds;

public class InferieurEgal extends Comparaison {

    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public <T> T accept(ARMVisitor<T> visitor){
        return visitor.visit(this);
    }

    public InferieurEgal(Ast left, Ast right, int line) {
        super(left, right, line);
    }
}
