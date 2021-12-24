package compilateur.TDS;


public class DeclStruct extends Symbole{

    private String name;

    public DeclStruct(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }
    
}
