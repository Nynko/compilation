package compilateur.TDS;


public class SymboleStruct extends Symbole{

    private SymboleStructContent struct;
    private String name;
    private int deplacement;


    public SymboleStruct(String name){
        this.name = name;
    }

    public SymboleStruct(SymboleStructContent struct, String name){
        this.struct = struct;
        this.name = name;
    }

    public SymboleStructContent getStruct(){
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
