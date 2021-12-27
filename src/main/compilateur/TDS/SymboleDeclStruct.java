package compilateur.TDS;

import java.util.ArrayList;

public class SymboleDeclStruct extends Symbole{

    private String name;
    private ArrayList<Symbole> declVars;

    public SymboleDeclStruct(String name){
        this.name = name;
        this.declVars = new ArrayList<Symbole>();
    }

    public SymboleDeclStruct(String name, ArrayList<Symbole> declVars){
        this.name = name;
        this.declVars = declVars;
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
