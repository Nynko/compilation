package compilateur.ast;

public class DeclFctInt implements Ast{
    
    public <T> T accept(AstVisitor<T> visitor){
        return visitor.visit(this);
    }

    public Ast Idf;
    public Ast param;
    public Ast bloc;

    public DeclFctInt(Ast idf, Ast param, Ast bloc) {
        this.Idf = idf;
        this.param = param;
        this.bloc = bloc;
    }
}
