package compilateur.TDS;

import java.util.ArrayList;
import java.util.HashMap;
import compilateur.Offset;

public class Tds {
    // Variables de classe
    private static int compteur;
    private static int compteurSymbole;

    // Variables d'instances
    private String name;
    private int imbrication;
    private int numRegion = compteur++;
    private Tds pointeurPere;
    private int deplacement = 0;
    private int deplacementParam = -Offset.OFFSET;
    private HashMap<String,Symbole> listeSymboles;
    private ArrayList<Tds> sousTDS = new ArrayList<>();
    

    public Tds(String name) {
        this.name = name;
        this.imbrication = 0;
        this.pointeurPere = null;
        this.listeSymboles = new HashMap<String,Symbole>();
    }

    public Tds(String name, Tds pointeurPere){
        this.name = name;
        this.imbrication = pointeurPere.getImbrication()+1;
        this.pointeurPere = pointeurPere;
        this.listeSymboles = new HashMap<String,Symbole>();
    }

    public HashMap<String,Symbole> getListeSymboles(){
        return this.listeSymboles;
    }

    public static int getCompteurSymbole() {
        return compteurSymbole;
    }

    public void addPere(Tds tds){
        this.pointeurPere = tds;
    }

    public void addnumRegion(int numRegion){
        this.numRegion = numRegion;
    }

    public String getName() {
        return this.name;
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
            s = table.listeSymboles.get(name);
            if(s!=null) return s;
            table = table.getPere();
        }
        return null;
    }

    public SymboleStructContent findSymboleStruct(String name) {
        Tds table = this;
        while(table.getImbrication() != 0 && table.pointeurPere != null) {
            table = table.getPere();
        }
        SymboleStructContent s = (SymboleStructContent) table.listeSymboles.get(name);
        return s;
    }

    public void addSymbole(String name, Symbole symbole) throws SymbolAlreadyExistsException {
        if(this.listeSymboles.get(name) != null) {
            throw new SymbolAlreadyExistsException(name,symbole.getDefinitionLine(), listeSymboles.get(name).getDefinitionLine());
        }
        if(symbole instanceof SymboleInt) {
            ((SymboleInt)symbole).setDeplacement(this.deplacement);
            this.deplacement += Offset.OFFSET;
        } else if(symbole instanceof SymboleStruct) {
            ((SymboleStruct)symbole).setDeplacement(this.deplacement);
            this.deplacement += Offset.OFFSET;
        }
        this.listeSymboles.put(name, symbole);
        compteurSymbole++;
    }

    public void addSymboleParam(String name, Symbole symbole) throws SymbolAlreadyExistsException {
        if(this.listeSymboles.get(name) != null) {
            throw new SymbolAlreadyExistsException(name,symbole.getDefinitionLine(), listeSymboles.get(name).getDefinitionLine());
        }
        if(symbole instanceof SymboleInt) {
            ((SymboleInt)symbole).setDeplacement(this.deplacementParam);
            this.deplacementParam -= Offset.OFFSET;
        } else if(symbole instanceof SymboleStruct) {
            ((SymboleStruct)symbole).setDeplacement(this.deplacementParam);
            this.deplacementParam -= Offset.OFFSET;
        }
        this.listeSymboles.put(name, symbole);
        compteurSymbole++;
    }

    public Tds nouvelleSousTDS(String name) {
        Tds nouvelleTds = new Tds(name, this);
        this.sousTDS.add(nouvelleTds);
        return nouvelleTds;
    }

    public ArrayList<Tds> getSousTDS() {
        return this.sousTDS;
    }
}
