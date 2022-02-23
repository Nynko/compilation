package compilateur.ast;

import compilateur.ARMGenerator.ARMVisitor;
import compilateur.tds.*;

/**
 * Déclaration d'une fonction qui retourne un int
 */
public class DeclFctInt extends AstNode implements Ast {

    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public <T> T accept(TdsVisitor<T> visitor, Tds tds) {
        return visitor.visit(this, tds);
    }
    


    /** Le nom de la fonction */
    public Ast Idf;
    /** La liste des parametres de la fonction */
    public Ast param;
    /** Le bloc d'intructions de la fonction */
    public Ast bloc;

    public DeclFctInt(Ast idf, Ast param, Ast bloc, int line) {
        super(line);
        this.Idf = idf;
        this.param = param;
        this.bloc = bloc;
    }
}
