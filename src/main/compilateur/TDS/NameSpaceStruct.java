package compilateur.TDS;

import java.util.HashMap;
import java.util.NoSuchElementException;

public class NameSpaceStruct{

    private HashMap<String,DeclStruct> dicoStruct;

    public NameSpaceStruct(){
        this.dicoStruct = new HashMap<String,DeclStruct>();
    }

    public void addDeclStruct(DeclStruct declStruct) throws SymbolAlreadyExistsException{
        String name = declStruct.getName();
        if(dicoStruct.containsKey(name)){
            DeclStruct old = dicoStruct.get(name);
            throw new SymbolAlreadyExistsException(name, declStruct.getDefinitionLine(), old.getDefinitionLine() );
        }
        else{
            this.dicoStruct.put(name,declStruct);
        }
    }

    public boolean doesContains(String str){
        return dicoStruct.containsKey(str);
    }

    public DeclStruct getStruct(String name) throws NoSuchElementException{
        DeclStruct declStruct = dicoStruct.get(name);

        if(declStruct!=null){
            return declStruct;
        }
        
        else{
            throw new NoSuchElementException();
        }
    }
    
}
