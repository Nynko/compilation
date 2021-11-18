package compilateur.ast;

public class Semicolon implements Ast{

    public <T> T accept(AstVisitor<T> visitor){
        return visitor.visit(this);
    }
}
