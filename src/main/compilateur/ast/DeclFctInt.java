package compilateur.ast;

import compilateur.ARMGenerator.ARMVisitor;
import compilateur.tds.*;

/**
 * Declaration of a function returning an int
 */
public class DeclFctInt extends AstNode implements Ast {

    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public <T> T accept(TdsVisitor<T> visitor, Tds tds) {
        return visitor.visit(this, tds);
    }
    
    public <T> T accept(ARMVisitor<T> visitor){
        return visitor.visit(this);
    }

    public Ast Idf;
    public Ast param;
    public Ast bloc;

    public DeclFctInt(Ast idf, Ast param, Ast bloc, int line) {
        super(line);
        this.Idf = idf;
        this.param = param;
        this.bloc = bloc;
    }
}
