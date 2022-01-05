package compilateur.TDS;

public class SymboleName extends Symbole{

    private String str;
    
    public SymboleName(String str){
        this.str = str;
    }

    public String getString(){
        return this.str;
    }

}
