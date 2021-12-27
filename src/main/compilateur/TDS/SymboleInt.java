package compilateur.TDS;

public class SymboleInt extends Symbole{

    private String name;
    private int deplacement;

    public SymboleInt(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

    public int getDeplacement(){
        return this.deplacement;
    }

    public void setDeplacement(int deplacement){
        this.deplacement = deplacement;
    }

    
}
