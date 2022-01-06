package compilateur.TDS;


public class SymboleFonction extends Symbole {
    private String returnType;
    private Tds tds;

    public SymboleFonction(String name, Tds tds){
        super(name);
        this.tds = tds;
    }

    public Tds getTds(){
        return this.tds;
    }

    public String getReturnType(){
        return this.returnType;
    }

    public void setReturnType(String str){
        this.returnType = str;
    }
}
