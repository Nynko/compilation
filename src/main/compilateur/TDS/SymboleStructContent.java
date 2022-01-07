package compilateur.TDS;

public class SymboleStructContent extends Symbole{
    private Tds tds;

    public SymboleStructContent(String name){
        super(name);
    }

    public void setTds(Tds tds) {
        this.tds = tds;
    }

    public Tds getTds() {
        return this.tds;
    }
}
