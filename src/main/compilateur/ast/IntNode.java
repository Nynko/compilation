package compilateur.ast;

public class IntNode implements Ast{

    public <T> T accept(AstVisitor<T> visitor){
        return visitor.visit(this);
    }

    public int parseInt;

    public IntNode(int parseInt) {
        this.parseInt = parseInt;
    }

}
