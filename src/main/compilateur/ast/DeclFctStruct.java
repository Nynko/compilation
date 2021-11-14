package compilateur.ast;

public class DeclFctStruct implements Ast{
    
    public <T> T accept(AstVisitor<T> visitor){
        return visitor.visit(this);
    }

    Ast Idf0;
    Ast Idf1;
    Ast param;
    Ast bloc;

    public DeclFctStruct(Ast idf0, Ast idf1, Ast param, Ast bloc) {
        this.Idf0 = idf0;
        this.Idf1 = idf1;
        this.param = param;
        this.bloc = bloc;
    }
}
