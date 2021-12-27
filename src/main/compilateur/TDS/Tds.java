package compilateur.TDS;

import java.util.HashMap;

public class Tds {
    private int imbrication;
    private static int compteur;
    private int numRegion = compteur++;
    private Tds pointeurPere;
    private NameSpaceStruct nameSpaceStruct; 

    private HashMap<String,Symbole> listeSymboles;
    

    public Tds(){
        this.imbrication = 0;
        this.pointeurPere = null;
        this.listeSymboles = new HashMap<String,Symbole>();
    }

    public Tds(Tds pointeurPere){
        this.imbrication = pointeurPere.getImbrication()+1;
        this.pointeurPere = pointeurPere;
        this.listeSymboles = new HashMap<String,Symbole>();
        this.nameSpaceStruct = pointeurPere.getNameSpaceStruct();
    }

    public HashMap<String,Symbole> getListeSymboles(){
        return this.listeSymboles;
    }

    public void addNameSpaceStruct(NameSpaceStruct nameSpaceStruct){
        this.nameSpaceStruct = nameSpaceStruct;
    }

    public boolean isNameSpaceStructContains(String str){
        return this.nameSpaceStruct.doesContains(str);
    }

    public NameSpaceStruct getNameSpaceStruct(){
        return this.nameSpaceStruct;
    }

    public SymboleDeclStruct getNameSpaceStruct(String name){
        return this.nameSpaceStruct.getStruct(name);
    }

    public void addPere(Tds tds){
        this.pointeurPere = tds;
        this.nameSpaceStruct = pointeurPere.getNameSpaceStruct();
    }

    public void addnumRegion(int numRegion){
        this.numRegion = numRegion;
    }

    public Tds getPere(){
        return this.pointeurPere;
    }

    public int getImbrication() {
        return this.imbrication;
    }

    public int getNumRegion() {
        return this.numRegion;
    }

    public Symbole findSymbole(String name) {
        Symbole s = this.listeSymboles.get(name);
        if(s!=null) return s;
        Tds table = this.getPere();
        while(table != null) {
            s = this.listeSymboles.get(name);
            if(s!=null) return s;
            table = table.getPere();
        }
        return null;
    }

    public void addSymbole(String name, Symbole symbole) throws SymbolAlreadyExistsException {
        if(this.listeSymboles.get(name) != null) {
            throw new SymbolAlreadyExistsException(name,0, listeSymboles.get(name).getDefinitionLine());
        }
        this.listeSymboles.put(name, symbole);
    }

}
