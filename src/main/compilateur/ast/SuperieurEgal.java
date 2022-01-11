package compilateur.ast;

public class SuperieurEgal extends Operateur {

    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public SuperieurEgal(Ast left, Ast right, int line) {
        super(left, right, line);
    }
}
