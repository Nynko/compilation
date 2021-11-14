package compilateur.ast;

public interface AstVisitor<T> {
    public T visit(Fichier fichier);
    public T visit(Idf affect);
    public T visit(DeclVarInt affect);
    public T visit(Decl affect);
    public T visit(DeclVarStruct affect);
    public T visit(Decl_typ affect);
    public T visit(DeclFctInt affect);
    public T visit(DeclFctStruct affect);
    public T visit(ParamListMulti paramListMulti);
    public T visit(ParamInt paramInt);
    public T visit(ParamStruct paramStruct);
    public T visit(Sizeof sizeof);
    public T visit(IdfParenthesis idfParenthesis);
    public T visit(IdfParenthesisEmpty idfParenthesisEmpty);
}