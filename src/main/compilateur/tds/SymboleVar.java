package compilateur.tds;

public abstract class SymboleVar extends Symbole {
    private int paramIndex = -1;
    private int deplacement;
    private boolean initialise = false;

    protected SymboleVar(String name) {
        super(name);
    }

    public boolean isInitalized() {
        return this.initialise;
    }

    public void setInitalized(boolean b) {
        this.initialise = b;
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

    public int getDeplacement(int wordsize){
        return this.deplacement * wordsize;
    }

    public int getDeplacement(){
        return this.deplacement;
    }

    public void setDeplacement(int deplacement){
        this.deplacement = deplacement;
    }  
}
