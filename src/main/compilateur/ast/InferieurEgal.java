package compilateur.ast;



public class InferieurEgal extends Comparaison {

    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visit(this);
    }



    public InferieurEgal(Ast left, Ast right, int line) {
        super(left, right, line);
    }
}
