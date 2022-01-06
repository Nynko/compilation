package compilateur.TDS;

public abstract class SymboleVar extends Symbole {
    private int paramIndex = -1;
    private int deplacement;

    public SymboleVar(String name) {
        super(name);
    }
    
    public void setParam(int index) {
        this.paramIndex = index;
    }

    public Boolean isParam() {
        return this.paramIndex > -1;
    }

    public int getParamIndex() {
        return this.paramIndex;
    } 

    public int getDeplacement(){
        return this.deplacement;
    }

    public void setDeplacement(int deplacement){
        this.deplacement = deplacement;
    }  
}
