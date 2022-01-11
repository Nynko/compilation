package compilateur.ast;

public class Superieur extends Operateur {

    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public Superieur(Ast left, Ast right, int line) {
        super(left, right, line);
    }
}
