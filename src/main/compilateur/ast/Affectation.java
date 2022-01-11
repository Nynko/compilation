package compilateur.ast;

public class Affectation extends Operateur {

    public <T> T accept(AstVisitor<T> visitor){
        return visitor.visit(this);
    }

    public Affectation(Ast left, Ast right, int line) {
        super(left, right, line);
    }
}
