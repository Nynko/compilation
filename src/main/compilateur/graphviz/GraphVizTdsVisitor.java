package compilateur.graphviz;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


import compilateur.TDS.Symbole;
import compilateur.TDS.SymboleStructContent;
import compilateur.TDS.SymboleVar;
import compilateur.TDS.SymboleFonction;
import compilateur.TDS.SymboleInt;
import compilateur.TDS.SymboleStruct;
import compilateur.TDS.Tds;


public class GraphVizTdsVisitor {

    private int state;
    private String nodeBuffer;
    private String linkBuffer;

    public GraphVizTdsVisitor(){
        this.state = 0;
        this.nodeBuffer = "digraph \"tds\"{\n\n\tnodesep=1;\n\tranksep=1;\n\n";
        this.linkBuffer = "\n";
    }

    public void dumpGraph(String filepath) throws IOException{
            
        FileOutputStream output = new FileOutputStream(filepath);

        String buffer = this.nodeBuffer + this.linkBuffer + "}";
        byte[] strToBytes = buffer.getBytes();

        output.write(strToBytes);

        output.close();

    }


    private String nextState(){
        int returnedState = this.state;
        this.state++;
        return "N"+ returnedState;
    }


    private void addTds(String name, String node, Tds tds){
        int numRegion = tds.getNumRegion();
        int numImbrication = tds.getImbrication();
        String region = Integer.toString(numRegion);
        String imbrication = Integer.toString(numImbrication);

        HashMap<String,Symbole> hashmap = tds.getListeSymboles();

        String tableContent = "";

        for(String symboleKey : hashmap.keySet()){
            Symbole symbole = hashmap.get(symboleKey);

            if(symbole instanceof SymboleInt){
                SymboleInt sym = (SymboleInt) symbole;
                tableContent += String.format("<tr><td> %s </td> <td> int </td> <td> %d </td> <td> %s </td> <td> line . %d  </td> </tr>", sym.getName(), sym.getDeplacement(), sym.isParam()? "oui (index: " + sym.getParamIndex()+ ")":"non", sym.getDefinitionLine() );
            } else if(symbole instanceof SymboleStruct){
                SymboleStruct sym = (SymboleStruct) symbole;
                tableContent += String.format("<tr><td> %s </td> <td> struct %s </td> <td> %d </td> <td> %s </td> <td> line . %d  </td> </tr>", sym.getName(), sym.getStruct()!=null ? sym.getStruct().getName():"undefined struct", sym.getDeplacement(), sym.isParam()? "oui (index: " + sym.getParamIndex()+ ")":"non", sym.getDefinitionLine() );
            } else if(symbole instanceof SymboleStructContent){
                SymboleStructContent sym = (SymboleStructContent) symbole;
                tableContent += String.format("<tr><td> struct_%s </td> <td> struct_type </td> <td> </td> <td> </td>  <td> line . %d  </td> </tr>", sym.getName(), sym.getDefinitionLine() );
            } else if(symbole instanceof SymboleFonction) {
                SymboleFonction sym = (SymboleFonction) symbole;
                tableContent += String.format("<tr><td> %s </td> <td> function (returns %s) </td> <td></td> <td> %s </td> <td> line . %d  </td> </tr>" , sym.getName(), sym.getReturnType(), sym.getName()+paramlistToString(sym.getTds().getParams()), sym.getDefinitionLine() );
            }
        }

        for(Tds subTds: tds.getSousTDS()) {
            String newNode = this.nextState(); // Change with recursive node creation 
            addTds(subTds.getName(), newNode, subTds);
            this.linkBuffer = linkBuffer + String.format("\t%s -> %s;\n",node,newNode);
        }

        this.nodeBuffer += String.format("""
            \t%s [shape=\"plaintext\",label=<
            <table border='1' cellborder='1' cellspacing='1'>
                <tr>
                    <td colspan="5"><b>%s, Région %s, Imbrication %s</b></td>
                </tr>
                <tr>
                    <td>
                        <b> Nom  </b>
                    </td>
                    <td>
                        <b> Type </b>
                    </td>
                    <td>
                        <b> Déplacement </b>
                    </td>
                    <td>
                        <b> Param </b>
                    </td>
                    <td>
                        <b> Ligne de définition </b>
                    </td>
                </tr>
                %s
            </table>
            >];\n""",
            node, name, region, imbrication, tableContent);
    } 

    public String paramlistToString(ArrayList<SymboleVar> params) {
        String res = "(";
        if(params.size() > 0) {
            for(SymboleVar sym: params) res += sym.getName() + ", ";
            res = res.substring(0, res.length()-2);
        }
        res += ")";
        return res;
    }

    public void createGraph(Tds tds){
        addTds(tds.getName(),this.nextState(), tds);
    }
    
}
