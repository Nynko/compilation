package compilateur.TDS;

public class SymboleInt extends Symbole implements CloningStructPrototype{

    private int value;
    private String name;
    private int deplacement;

    public SymboleInt(String name){
        this.name = name;
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

    public Symbole cloneSymbole(){
        return new SymboleInt(this.name);
    }

    
}
