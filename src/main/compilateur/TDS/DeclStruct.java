package compilateur.TDS;

import java.util.ArrayList;

public class DeclStruct extends Symbole implements CloningStructPrototype{

    private String name;
    private ArrayList<Symbole> declVars;

    public DeclStruct(String name){
        this.name = name;
        this.declVars = new ArrayList<Symbole>();
    }

    public DeclStruct(String name, ArrayList<Symbole> declVars){
        this.name = name;
        this.declVars = declVars;
    }

    public String getName(){
        return this.name;
    }
    
    public void addDeclVars(Symbole symbole){
        this.declVars.add(symbole);
    }

    public ArrayList<Symbole> getListDeclVars(){
        return this.declVars;
    }

    public Symbole clone(){
        DeclStruct declStructClone =  new DeclStruct(this.name);
        for(Symbole symbole : declVars){

            if( symbole instanceof SymboleInt ){
                declStructClone.getListDeclVars().add(((SymboleInt)symbole).clone());
            }

            else if (symbole instanceof SymboleStruct){
                declStructClone.getListDeclVars().add(((SymboleStruct)symbole).clone());
            }

            else{
                throw new Error("La structure " + this.name + " contient des symboles différents de SymboleInt et SymboleStruct");
            }
        }

        return declStructClone;
    }
}
