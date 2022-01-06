package compilateur.TDS;

public abstract class SymboleBloc extends Symbole {

    protected Tds tds;

    public SymboleBloc(Tds tds){
        super("jE suIs uN bLoC");
        this.tds = tds;
    }

    public Tds getTds(){
        return this.tds;
    }

}
