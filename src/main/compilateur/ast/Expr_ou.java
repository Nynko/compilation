package compilateur.ast;



public class Expr_ou extends Operateur {

    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visit(this);
    }



    public Expr_ou(Ast left, Ast right, int line) {
        super(left, right, line);
    }
}
