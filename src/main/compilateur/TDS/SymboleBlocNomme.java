package compilateur.TDS;

public abstract class SymboleBlocNomme extends SymboleBloc{

    private String name;

    public SymboleBlocNomme(Tds tds){
        super(tds);
    }

    public SymboleBlocNomme(String name, Tds tds){
        super(tds);
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

    public Boolean isNamed(String name){
        return name.equals(this.name);
    }
    
    
}
