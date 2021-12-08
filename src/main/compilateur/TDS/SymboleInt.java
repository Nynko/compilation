package compilateur.TDS;

import compilateur.ast.AstVisitor;

public class SymboleInt extends Symbole{

    public int value;
    public String name;
    public int deplacement;

    public SymboleInt(int value){
        this.value = value;
    }

    public SymboleInt(int value, String name){
        this.value = value;
        this.name = name;
    }

    
}
