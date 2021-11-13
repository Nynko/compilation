package compilateur.ast;

public class Fichier implements Ast {
    public <T> T accept(AstVisitor<T> visitor){
        return visitor.visit(this);
    }

    public Ast instructions;

    public Fichier(Ast inst) {
        this.instructions = inst;
    }
}
