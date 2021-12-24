package compilateur.TDS;


public class SymboleStruct extends SymboleBlocNomme {
    private Tds tds;
    private String nom;

    public SymboleStruct(String name, Tds tds){
        super(name,tds);
    }

    public SymboleStruct(Tds tds){
        super(tds);
    }
    

}
