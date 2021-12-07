package compilateur.TDS;

import compilateur.ast.AstVisitor;

public interface Symbole {

    public <T> T accept(AstVisitor<T> visitor);
}
