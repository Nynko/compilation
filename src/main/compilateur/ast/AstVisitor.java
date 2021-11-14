package compilateur.ast;

public interface AstVisitor<T> {
    public T visit(Fichier fichier);
    public T visit(Idf idf);
    public T visit(DeclVarInt declVarInt);
    public T visit(Decl decl);
    public T visit(DeclVarStruct declVarStruct);
    public T visit(Decl_typ decl_typ);
    public T visit(DeclFctInt declFctInt);
    public T visit(DeclFctStruct declFctStruct);
    public T visit(ParamListMulti paramListMulti);
    public T visit(ParamInt paramInt);
    public T visit(ParamStruct paramStruct);
    public T visit(Sizeof sizeof);
    public T visit(IdfParenthesis idfParenthesis);
    public T visit(IdfParenthesisEmpty idfParenthesisEmpty);
    public T visit(IfThen ifThen);
    public T visit(IfThenElse ifThenElse);
    public T visit(While while1);
    public T visit(Return return1);
    public T visit(Bloc bloc);
    public T visit(CharNode charNode);
    public T visit(IntNode intNode);
    public T visit(Affectation affectation);
    public T visit(Expr_ou expr_ou);
    public T visit(Expr_et expr_et);
    public T visit(Egal egal);
    public T visit(Different dif);
}