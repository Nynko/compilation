package compilateur.ast;

public interface AstVisitor<T> {
    public T visit(Fichier affect);
    public T visit(Idf affect);
    public T visit(DeclVarInt affect);
    public T visit(Decl affect);
    public T visit(DeclVarStruct affect);
    public T visit(Decl_typ affect);
    public T visit(DeclFctInt affect);
    public T visit(DeclFctStruct affect);
}