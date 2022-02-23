package compilateur.ast;

import compilateur.ARMGenerator.ARMVisitor;
import compilateur.tds.*;


/**
 * Déclaration d'une fonction qui retourne une struct
 */
public class DeclFctStruct extends AstNode implements Ast {

    public <T> T accept(AstVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public <T> T accept(TdsVisitor<T> visitor, Tds tds) {
        return visitor.visit(this, tds);
    }

    public <T> T accept(ARMVisitor<T> visitor){
        return visitor.visit(this);
    }

    /** Le type de la struct */
    public Ast Idf0;
    /** Le nom de la fonction */
    public Ast Idf1;
    /** La liste des parametres de la fonction */
    public Ast param;
    /** Le bloc d'intructions de la fonction */
    public Ast bloc;

    public DeclFctStruct(Ast idf0, Ast idf1, Ast param, Ast bloc, int line) {
        super(line);
        this.Idf0 = idf0;
        this.Idf1 = idf1;
        this.param = param;
        this.bloc = bloc;
    }
}
