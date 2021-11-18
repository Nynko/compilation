package compilateur.ast;

public class Idf implements Ast{

    public <T> T accept(AstVisitor<T> visitor){
        return visitor.visit(this);
    }

    public String name;

    public Idf(String name) {
        this.name = name;
    }
}   
