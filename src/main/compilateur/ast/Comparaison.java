package compilateur.ast;

public abstract class Comparaison extends Operateur {

    protected Comparaison(Ast left, Ast right, int line) {
        super(left, right, line);
    }
    
}
