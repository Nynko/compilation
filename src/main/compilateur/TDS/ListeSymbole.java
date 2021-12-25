package compilateur.TDS;

import java.util.ArrayList;

public class ListeSymbole extends Symbole{
    /**
     * Cette classe est une liste temporaire afin de transporter des symboles lors de la 
    visite pour la TDS 
     */

    private ArrayList<Symbole> liste;

    public ListeSymbole(){
        this.liste = new ArrayList<Symbole>();
    }

    public void addSymbole(Symbole e){
        if(!liste.contains(e)){
            this.liste.add(e);
        }
    }

    public ArrayList<Symbole> getList(){
        return this.liste;
    }


}
