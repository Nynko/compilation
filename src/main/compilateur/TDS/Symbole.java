package compilateur.TDS;

public abstract class Symbole {
    private int definitionLine;
    private String name;

    protected Symbole(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }
    
    public int getDefinitionLine() {
        return this.definitionLine;
    }

    public void addDefinitionLine(int definitionLine){
        this.definitionLine = definitionLine;
    }
}
