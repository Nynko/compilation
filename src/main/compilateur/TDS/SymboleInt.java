package compilateur.TDS;

import compilateur.ast.AstVisitor;

public class SymboleInt{

    public int value;
    public String name;

    public SymboleInt(int value){
        this.value = value;
    }

    public SymboleInt(int value, String name){
        this.value = value;
        this.name = name;
    }

    
}
