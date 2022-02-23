package compilateur.ast;

import compilateur.ARMGenerator.ARMVisitor;

public class SuperieurEgal extends Comparaison {

    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visit(this);
    }



    public SuperieurEgal(Ast left, Ast right, int line) {
        super(left, right, line);
    }

}
