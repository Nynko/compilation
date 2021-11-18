package compilateur.ast;

public class ParamStruct implements Ast{

    public <T> T accept(AstVisitor<T> visitor){
        return visitor.visit(this);
    }

    public Ast idf0;
    public Ast idf1;

    public ParamStruct(Ast idf0, Ast idf1) {
        this.idf0 = idf0;
        this.idf1 = idf1;
    }
}
