package compilateur.tds;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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
    private int deplacement = 12;
    private int compteurParams = 0;
    private int deplacementParam = -Offset.OFFSET;
    private HashMap<String,Symbole> listeSymboles;
    private ArrayList<Tds> sousTDS = new ArrayList<>();
    
    /** Créer une nouvelle TDS
     * 
     * @param name le nom de la TDS
     */
    public Tds(String name) {
        this.name = name;
        this.imbrication = 0;
        this.pointeurPere = null;
        this.listeSymboles = new HashMap<String,Symbole>();
    }

    /** Créer une nouvelle TDS
     * 
     * @param name le nom de la TDS
     * @param pointeurPere la TDS du bloc englobant
     */
    public Tds(String name, Tds pointeurPere){
        this.name = name;
        this.imbrication = pointeurPere.getImbrication()+1;
        this.pointeurPere = pointeurPere;
        this.listeSymboles = new HashMap<String,Symbole>();
    }

    /** Récuperer la liste des symboles contenus dans la table
     * 
     * @return la liste des symboles contenus dans la TDS
     */
    public HashMap<String,Symbole> getListeSymboles(){
        return this.listeSymboles;
    }

    /** Retourne le nombre de symboles du programme
     * 
     * @return le nombre de symboles
     */
    public static int getCompteurSymbole() {
        return compteurSymbole;
    }

    /** Ajouter un pointeur vers la TDS du bloc englobant
     * 
     * @param tds la TDS du bloc englobant
     */
    public void addPere(Tds tds){
        this.pointeurPere = tds;
    }

    /** Ajouter un numéro de région
     * 
     * @param numRegion le numéro de région
     */
    public void addnumRegion(int numRegion){
        this.numRegion = numRegion;
    }

    /** Récupérer le nom de la TDS
     * 
     * @return le nom de la table
     */
    public String getName() {
        return this.name;
    }

    /** Récupère la TDS du bloc englobant
     * 
     * @return un pointeur vers la TDS du bloc englobant
     */
    public Tds getPere(){
        return this.pointeurPere;
    }

    /** Récupère le numémo d'imbrication
     * 
     * @return le numéro d'imbrication
     */
    public int getImbrication() {
        return this.imbrication;
    }

    /** Récupère le numémo de région
     * 
     * @return le numéro de région
     */
    public int getNumRegion() {
        return this.numRegion;
    }

    /** Trouve un symbole dans la TDS ou les TDS des blocs englobants
     * 
     * @param name le nom du symbole
     * @return le symbole si il existe ou `null`
     */
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

    /** A partir d'un nom de symbole, retourne le numéro d'imbrication de la tds dans laquelle se trouve le symbole 
     * 
     * @param name le nom du symbole
     * @return le numéro d'imbrication de la tds ou retourne -1
     */
    public int findImbrication(String name) {
        Symbole s = this.listeSymboles.get(name);
        if(s!=null) return this.getImbrication();
        Tds table = this.getPere();
        while(table != null) {
            s = table.listeSymboles.get(name);
            if(s!=null) return table.getImbrication();
            table = table.getPere();
        }
        return -1;
    }

    /** Trouver un symbole de déclaration de type struct
     * 
     * @param name le nom du symbole de struct
     * @return le symbole de struct ou `null`
     */
    public SymboleStructContent findSymboleStruct(String name) {
        Tds table = this;
        while(table.getImbrication() != 0 && table.pointeurPere != null) {
            table = table.getPere();
        }
        return (SymboleStructContent) table.listeSymboles.get(name);
    }

    /** Retourne la valeur du déplacement maximum par rapport au pointeur de base
     * 
     * @return le deplacement
     */
    public int getDeplacement() {
        return this.deplacement;
    }

    /** Ajouter un symbole à la TDS
     * 
     * @param name le nom du symbole
     * @param symbole le symbole
     * @throws SymbolAlreadyExistsException si un symbole de même nom est déjà délaré dans le bloc
     */
    public void addSymbole(String name, Symbole symbole) throws SymbolAlreadyExistsException {
        if(this.listeSymboles.get(name) != null) {
            throw new SymbolAlreadyExistsException(name,symbole.getDefinitionLine(), listeSymboles.get(name).getDefinitionLine());
        }
        if(symbole instanceof SymboleVar sym) {
            sym.setDeplacement(this.deplacement);
            this.deplacement += Offset.OFFSET;
        }
        this.listeSymboles.put(name, symbole);
        Tds.incrementNumberSymbole();
    }

    public static void incrementNumberSymbole() {
        compteurSymbole++;
    }

    /** Ajouter un symbole de paramètre à la TDS
     * 
     * @param name le nom du symbole
     * @param symbole le symbole
     * @throws SymbolAlreadyExistsException si un symbole de même nom est déjà délaré dans le bloc
     */
    public void addSymboleParam(String name, Symbole symbole) throws SymbolAlreadyExistsException {
        if(this.listeSymboles.get(name) != null) {
            throw new SymbolAlreadyExistsException(name,symbole.getDefinitionLine(), listeSymboles.get(name).getDefinitionLine());
        }
        if(symbole instanceof SymboleVar sym) {
            sym.setParam(this.compteurParams);
            sym.setDeplacement(this.deplacementParam);
            this.deplacementParam -= Offset.OFFSET;
            this.compteurParams += 1;
            sym.setInitalized(true);
        }
        this.listeSymboles.put(name, symbole);
        Tds.incrementNumberSymbole();
    }

    /** Récupère les paramètres d'une fonction
     * 
     * @return la liste de symboles paramètres de la fonction
     */
    public ArrayList<SymboleVar> getParams() {
        ArrayList<SymboleVar> params = new ArrayList<>();
        for (Map.Entry<String, Symbole> set : this.listeSymboles.entrySet()) {
            Symbole sym = set.getValue();

            if(sym instanceof SymboleVar symvar && symvar.isParam()) {
                    params.add(symvar);
            }
        }
        params.sort((o1, o2) -> Integer.compare(o1.getParamIndex(),o2.getParamIndex()));
        return params;
    }

    /** Créer une nouvelle sous-TDS
     * 
     * @param name le nom de la nouvelle TDS
     * @return la nouvelle sous-TDS
     */
    public Tds nouvelleSousTDS(String name) {
        Tds nouvelleTds = new Tds(name, this);
        this.sousTDS.add(nouvelleTds);
        return nouvelleTds;
    }

    /** Récupèrer la liste des sous-TDS d'une TDS
     * 
     * @return la liste des sous-TDS
     */
    public ArrayList<Tds> getSousTDS() {
        return this.sousTDS;
    }
}
