package compilateur.TDS;

public abstract class SymboleBloc extends Symbole {

    private Tds tds;

    public SymboleBloc(Tds tds){
        this.tds = tds;
    }

    public Tds getTds(){
        return this.tds;
    }

}
