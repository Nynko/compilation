package compilateur.ast;

public class IdfParenthesisEmpty implements Ast{

    public <T> T accept(AstVisitor<T> visitor){
        return visitor.visit(this);
    }

    public Ast idf; 

    public IdfParenthesisEmpty(Ast idf) {
        this.idf = idf;
    }

}

