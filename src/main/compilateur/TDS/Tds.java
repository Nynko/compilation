package compilateur.TDS;

import java.util.HashMap;

import compilateur.ast.AstVisitor;

public class Tds {

    private int imbrication;
    private String natureSymbole;
    private String nomSymbole;
    private int deplacement;
    private Tds pointeurPere;
    

    public Tds(){
        
    }

    public Tds(int imbrication, Tds pointeurPere){
        this.imbrication = imbrication;
        this.pointeurPere = pointeurPere;

    }

    public void addPere(Tds tds){
        this.pointeurPere = tds;
    }


    public void accept(){

    }

    
}
