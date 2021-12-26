package compilateur.TDS;

import java.util.HashMap;
import java.util.NoSuchElementException;

public class NameSpaceStruct{

    private HashMap<String,SymboleDeclStruct> dicoStruct;

    public NameSpaceStruct(){
        this.dicoStruct = new HashMap<String,SymboleDeclStruct>();
    }

    public void addSymboleDeclStruct(SymboleDeclStruct declStruct) throws SymbolAlreadyExistsException{
        String name = declStruct.getName();
        if(dicoStruct.containsKey(name)){
            SymboleDeclStruct old = dicoStruct.get(name);
            throw new SymbolAlreadyExistsException(name, declStruct.getDefinitionLine(), old.getDefinitionLine() );
        }
        else{
            this.dicoStruct.put(name,declStruct);
        }
    }

    public boolean doesContains(String str){
        return dicoStruct.containsKey(str);
    }

    public SymboleDeclStruct getStruct(String name) throws NoSuchElementException{
        SymboleDeclStruct declStruct = dicoStruct.get(name);

        if(declStruct!=null){
            return declStruct;
        }
        
        else{
            throw new NoSuchElementException();
        }
    }
    
}
