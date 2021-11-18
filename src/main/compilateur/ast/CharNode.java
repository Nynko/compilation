package compilateur.ast;

public class CharNode implements Ast{

    public <T> T accept(AstVisitor<T> visitor){
        return visitor.visit(this);
    }

    public String string; 

    public CharNode(String string) {
        this.string = string;
    }

}
