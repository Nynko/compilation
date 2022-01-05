package compilateur.TDS;

import java.util.ArrayList;

public class SymboleStructContent extends Symbole{

    private String name;
    private ArrayList<Symbole> declVars;
    private Tds tds;

    public SymboleStructContent(String name){
        this.name = name;
        this.declVars = new ArrayList<Symbole>();
        this.tds = new Tds();
    }

    public Tds getTds() {
        return this.tds;
    }

    public String getName(){
        return this.name;
    }
    
    public void addDeclVars(Symbole symbole){
        this.declVars.add(symbole);
    }

    public ArrayList<Symbole> getListDeclVars(){
        return this.declVars;
    }

}
