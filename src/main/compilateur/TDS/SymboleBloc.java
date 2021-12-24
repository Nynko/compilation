package compilateur.TDS;

public abstract class SymboleBloc extends Symbole {

    protected Tds tds;

    public SymboleBloc(Tds tds){
        this.tds = tds;
    }

    public Tds getTds(){
        return this.tds;
    }

}
