package compilateur.ast;

public class Inferieur extends Comparaison {

    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public Inferieur(Ast left, Ast right, int line) {
        super(left, right, line);
    }
}