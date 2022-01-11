package compilateur.ast;

public class Fleche extends Operateur {

    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public Fleche(Ast left, Ast right, int line) {
        super(left, right, line);
    }
}
