package compilateur.TDS;

import compilateur.ast.AstVisitor;

public interface Tds {
    
    public <T> T accept(AstVisitor<T> visitor);

}
