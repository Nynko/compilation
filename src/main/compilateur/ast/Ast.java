package compilateur.ast;

import compilateur.TDS.*;

public interface Ast {

    public <T> T accept(AstVisitor<T> visitor);
    public <T> T accept(TdsVisitor<T> visitor, Tds tds);
}
