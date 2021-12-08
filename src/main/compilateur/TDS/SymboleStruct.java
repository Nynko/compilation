package compilateur.TDS;


public class SymboleStruct {
    private Tds tds;
    private String nom;

    public SymboleStruct(String nom, Tds tds){
        this.tds = tds;
        this.nom = nom;
    }

    public Tds getTds(){
        return this.tds;
    }

    public Boolean isNamed(String nom){
        return nom.equals(this.nom);
    }
}
