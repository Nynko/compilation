package compilateur.TDS;

import java.util.HashMap;
import java.util.NoSuchElementException;

public class NameSpaceStruct{

    private HashMap<String,SymboleStructContent> dicoStruct;

    public NameSpaceStruct(){
        this.dicoStruct = new HashMap<String,SymboleStructContent>();
    }

    public HashMap<String,SymboleStructContent> getHashMap(){
        return this.dicoStruct;
    }

    public void addSymboleDeclStruct(SymboleStructContent declStruct) throws SymbolAlreadyExistsException{
        String name = declStruct.getName();
        if(dicoStruct.containsKey(name)){
            SymboleStructContent old = dicoStruct.get(name);
            throw new SymbolAlreadyExistsException(name, declStruct.getDefinitionLine(), old.getDefinitionLine() );
        }
        else{
            this.dicoStruct.put(name,declStruct);
        }
    }

    public boolean doesContains(String str){
        return dicoStruct.containsKey(str);
    }

    public SymboleStructContent getStruct(String name) throws NoSuchElementException{
        SymboleStructContent declStruct = dicoStruct.get(name);

        if(declStruct!=null){
            return declStruct;
        }
        
        else{
            throw new NoSuchElementException();
        }
    }
    
}
