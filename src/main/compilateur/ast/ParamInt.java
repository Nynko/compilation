package compilateur.ast;

public class ParamInt implements Ast{

    public <T> T accept(AstVisitor<T> visitor){
        return visitor.visit(this);
    }

    public Ast name;

    public ParamInt(Ast name) {
        this.name = name;
    }
}
