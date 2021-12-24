package compilateur.TDS;

public abstract class Symbole {
    private int definitionLine;

    public Symbole(){
    }

    // public Symbole(int definitionLine){
    //     this.definitionLine = definitionLine;
    // }
    
    public int getDefinitionLine() {
        return this.definitionLine;
    }

    public void addDefinitionLine(int definitionLine){
        this.definitionLine = definitionLine;
    }
}
