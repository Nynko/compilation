package compilateur.ast;

public class Plus extends Operateur {

    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public Plus(Ast left, Ast right, int line) {
        super(left, right, line);
    }
}
