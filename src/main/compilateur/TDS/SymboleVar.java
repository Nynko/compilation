package compilateur.TDS;

public abstract class SymboleVar extends Symbole {
    private int deplacement;

    public SymboleVar(String name) {
        super(name);
    }
    
    public int getDeplacement(){
        return this.deplacement;
    }

    public void setDeplacement(int deplacement){
        this.deplacement = deplacement;
    }  
}
