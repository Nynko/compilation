package compilateur.ast;

public interface AstVisitor<T> {
    public T visit(Fichier affect);
    public T visit(Idf affect);
    public T visit(DeclVarInt affect);
    public T visit(Decl affect);
    public T visit(DeclVarStruct affect);

}