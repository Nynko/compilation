package compilateur.ast;

public class Egal extends Operateur {

    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public Egal(Ast left, Ast right, int line) {
        super(left, right, line);
    }

}
