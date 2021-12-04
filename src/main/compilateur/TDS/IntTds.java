package compilateur.TDS;

import compilateur.ast.AstVisitor;

public class IntTds implements Tds {

    public int value;

    public <T> T accept(Tds visitor);

    public IntTds(int value){
        this.value = value;
    }
    
}
