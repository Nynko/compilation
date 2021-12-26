package compilateur.TDS;


public class SymboleFonction extends SymboleBlocNomme {

    private String returnType;

    public SymboleFonction(String name, Tds tds){
        super(name,tds);
    }

    public Tds getTds(){
        return this.tds;
    }

    public Boolean isNamed(String nom){
        return nom.equals(this.name);
    }

    public String getReturnType(){
        return this.returnType;
    }

    public void setReturnType(String str){
        this.returnType = str;
    }

    
}
