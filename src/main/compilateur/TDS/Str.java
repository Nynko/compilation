package compilateur.TDS;

public class Str extends Symbole{

    private String str;
    
    public Str(String str){
        this.str = str;
    }

    public String getString(){
        return this.str;
    }

}
