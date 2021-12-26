package compilateur.TDS;

import java.util.ArrayList;

public class SymboleFonction extends SymboleBlocNomme {

    private String returnType;
    private ArrayList<Symbole> args; 

    public SymboleFonction(String name, Tds tds){
        super(name,tds);
        this.args = new ArrayList<Symbole>();
    }

    public SymboleFonction(String name, Tds tds, ArrayList<Symbole> args){
        super(name,tds);
        this.args = args;
    }

    public SymboleFonction(String returnType, String name, Tds tds, ArrayList<Symbole> args){
        super(name,tds);
        this.returnType = returnType;
        this.args = args;
    }

    public void addArgs(ArrayList<Symbole> args){
        this.args = args;
    }

    public void addArg(Symbole arg){
        this.args.add(arg);
    }

    public ArrayList<Symbole> getArgs(){
        return this.args;
    }

    public Tds getTds(){
        return this.tds;
    }

    public Boolean isNamed(String nom){
        return nom.equals(this.name);
    }

    public String getReturnType(){
        return this.returnType;
    }

    
}
