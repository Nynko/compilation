package compilateur.TDS;

import java.util.ArrayList;

public class SymboleStructContent extends Symbole{
    private ArrayList<Symbole> declVars;
    private Tds tds;

    public SymboleStructContent(String name){
        super(name);
        this.declVars = new ArrayList<Symbole>();
    }

    public void setTds(Tds tds) {
        this.tds = tds;
    }

    public Tds getTds() {
        return this.tds;
    }
    
    public void addDeclVars(Symbole symbole){
        this.declVars.add(symbole);
    }

    public ArrayList<Symbole> getListDeclVars(){
        return this.declVars;
    }

}
