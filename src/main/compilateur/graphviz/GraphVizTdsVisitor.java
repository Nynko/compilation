package compilateur.graphviz;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;


import compilateur.TDS.Symbole;
import compilateur.TDS.SymboleBloc;
import compilateur.TDS.SymboleBlocAnonyme;
import compilateur.TDS.SymboleStructContent;
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

        String content = "";

        for(String symboleKey : hashmap.keySet()){
            Symbole symbole = hashmap.get(symboleKey);

            if(symbole instanceof SymboleInt){
                SymboleInt sym = (SymboleInt) symbole;
                content = content + String.format("<tr><td> int </td> <td> %s </td> <td> %d </td> <td> line . %d  </td> </tr>", sym.getName(), sym.getDeplacement(), sym.getDefinitionLine() );
            } else if(symbole instanceof SymboleStruct){
                SymboleStruct sym = (SymboleStruct) symbole;
                content = content + String.format("<tr><td> Struct %s </td> <td> %s </td> <td> %d </td> <td> line . %d  </td> </tr>", sym.getStruct().getName(), sym.getName(), sym.getDeplacement(),sym.getDefinitionLine() );
            } else if(symbole instanceof SymboleStructContent){
                SymboleStructContent sym = (SymboleStructContent) symbole;
                content = content + String.format("<tr><td> Struct %s </td> <td> %s </td> <td> line . %d  </td> </tr>", sym.getName(), sym.getName(),sym.getDefinitionLine() );
                String newNode = this.nextState(); 
                addTds(sym.getName(),newNode, sym.getTds());
                this.linkBuffer = linkBuffer + String.format("\t%s -> %s;\n",node,newNode);
            } else if ( symbole instanceof SymboleBloc){
                if(symbole instanceof SymboleFonction){
                    SymboleFonction sym = (SymboleFonction) symbole ; 
                    String newNode = this.nextState(); 
                    Tds newTds = sym.getTds();
                    addTds(sym.getName(),newNode, newTds);
                    this.linkBuffer = linkBuffer + String.format("\t%s -> %s;\n",node,newNode);
                }  else if(symbole instanceof SymboleBlocAnonyme){
                    SymboleBlocAnonyme sym = (SymboleBlocAnonyme) symbole ; 
                    String newNode = this.nextState(); // Change with recursive node creation 
                    Tds newTds = sym.getTds();
                    addTds(symboleKey, newNode, newTds);
                    this.linkBuffer = linkBuffer + String.format("\t%s -> %s;\n",node,newNode);
                }
            }

        }

        this.nodeBuffer += String.format("\t%s [shape=\"plaintext\",label=<<table border='1' cellborder='1' cellspacing='1'> <tr><td> <b> %s  </b></td>  <td> <b> Region %s </b></td> <td> <b> Imbrication %s </b></td> <td></td>  </tr> %s </table>>];\n", node, name, region, imbrication, content);
    } 


    public void createGraph(Tds tds){
        addTds("",this.nextState(), tds);
    }
    
}
