package compilateur.TDS;


public class SymboleStruct extends Symbole{

    private SymboleDeclStruct struct;
    private String name;
    private int deplacement;


    public SymboleStruct(String name){
        this.name = name;
    }

    public SymboleStruct(SymboleDeclStruct struct, String name){
        this.struct = struct;
        this.name = name;
    }

    public SymboleDeclStruct getStruct(){
        return this.struct;
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
