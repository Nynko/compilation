package compilateur.ast;

public interface AstVisitor<T> {
    public T visit(Fichier affect);

}