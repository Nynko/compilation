package compilateur.TDS;

public class SymboleInt extends Symbole{

    private int value;
    private String name;
    private int deplacement;

    public SymboleInt(int value){
        this.value = value;
    }

    public SymboleInt(int value, String name){
        this.value = value;
        this.name = name;
    }

    public int getValue(){
        return this.value;
    }

    public String getName(){
        return this.name;
    }

    public int getDeplacement(){
        return this.deplacement;
    }

    
}
