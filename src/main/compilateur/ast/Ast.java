package compilateur.ast;

import compilateur.TDS.*;

public interface Ast {

    public <T> T accept(AstVisitor<T> visitor);
    public void accept(TdsVisitor visitor, Tds tds);
}
