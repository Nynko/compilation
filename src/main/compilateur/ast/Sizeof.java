package compilateur.ast;

public class Sizeof implements Ast{

    public <T> T accept(AstVisitor<T> visitor){
        return visitor.visit(this);
    }

    public Ast name;

    public Sizeof(Ast name) {
        this.name = name;
    }
}