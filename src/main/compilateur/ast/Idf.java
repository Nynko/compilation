package compilateur.ast;

import compilateur.ARMGenerator.ARMVisitor;
import compilateur.tds.*;

public class Idf extends AstNode implements Ast {

    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public <T> T accept(TdsVisitor<T> visitor, Tds tds) {
        return visitor.visit(this, tds);
    }

    public <T> T accept(ARMVisitor<T> visitor){
        return visitor.visit(this);
    }

    public String name;

    public Idf(String name, int line) {
        super(line);
        this.name = name;
    }
}
