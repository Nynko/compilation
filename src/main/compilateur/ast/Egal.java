package compilateur.ast;

import compilateur.ARMGenerator.;

public class Egal extends Comparaison {

    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visit(this);
    }
    


    public Egal(Ast left, Ast right, int line) {
        super(left, right, line);
    }

}
