package compilateur.TDS;

import java.util.HashMap;

import compilateur.ast.AstVisitor;

public class TableSymbole implements Tds{

    private int imbrication;

    private String natureSymbole;
    private String nomSymbole;
    private int deplacement;

    private Tds pointeurPere;
    

    public TableSymbole(){

    }

    public <T> T accept(AstVisitor<T> visitor){
        return visitor.visit(this);
    }


    public void linkPere(Tds tds){
        this.pointeurPere = tds;
    }

    public void accept(){

    }

    
}
