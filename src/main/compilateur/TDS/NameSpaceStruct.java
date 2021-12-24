package compilateur.TDS;

import java.util.HashMap;

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
    
}
