package compilateur.TDS;

public class SymboleInt implements Symbole{

    public SymboleInt(){

    }

    public <T> T accept(AstVisitor<T> visitor){
        return visitor.visit(this);
    }

    
    
}
