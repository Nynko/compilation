package compilateur.ast;

public class Division extends Operateur {

    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public Division(Ast left, Ast right, int line) {
        super(left, right, line);
    }

}
