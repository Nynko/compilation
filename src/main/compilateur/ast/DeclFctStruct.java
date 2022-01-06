package compilateur.ast;

import compilateur.TDS.*;

public class DeclFctStruct implements Ast {

    public int line;

    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public <T> T accept(TdsVisitor<T> visitor, Tds tds) {
        return visitor.visit(this, tds);
    }

    public Ast Idf0;
    public Ast Idf1;
    public Ast param;
    public Ast bloc;

    public DeclFctStruct(Ast idf0, Ast idf1, Ast param, Ast bloc, int line) {
        this.Idf0 = idf0;
        this.Idf1 = idf1;
        this.param = param;
        this.bloc = bloc;
        this.line = line;
    }
}
