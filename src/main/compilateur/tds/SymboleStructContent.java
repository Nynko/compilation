package compilateur.tds;

import java.util.HashMap;

public class SymboleStructContent extends Symbole{
    private Tds tds;
    private int WORD_SIZE = 8;

    public SymboleStructContent(String name){
        super(name);
    }

    public void setTds(Tds tds) {
        this.tds = tds;
    }

    public Tds getTds() {
        return this.tds;
    }

    public String getType() {
        return TdsVisitor.TYPESTRUCT + this.getName();
    }

    public int getSizeOfStruct(){
        int size = 0;
        HashMap<String,Symbole> hashmap = tds.getListeSymboles();
        
        for(String symboleKey : hashmap.keySet()){
            Symbole symbole = hashmap.get(symboleKey);
            if(symbole instanceof SymboleInt){
                size += WORD_SIZE;
            }
            else if(symbole instanceof SymboleStruct){
                // size += ((SymboleStruct) symbole).getStruct().getSizeOfStruct();
                size += WORD_SIZE;
            }
        }
        return size;
    }


}
