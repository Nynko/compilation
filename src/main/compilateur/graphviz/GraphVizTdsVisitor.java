package compilateur.graphviz;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


import compilateur.TDS.NameSpaceStruct;
import compilateur.TDS.Symbole;
import compilateur.TDS.SymboleBloc;
import compilateur.TDS.SymboleBlocAnonyme;
import compilateur.TDS.SymboleDeclStruct;
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

    private void addNameSpaceStruct(String node,Tds tds){
        String content = "";
        NameSpaceStruct nameSpace = tds.getNameSpaceStruct();
        HashMap<String,SymboleDeclStruct> hashmap = nameSpace.getHashMap();

        // Get max number of Col
        int numberOfColMax = 2;
        for(String key: hashmap.keySet()){
            int num = hashmap.get(key).getListDeclVars().size();
            if(num + 1 > numberOfColMax) numberOfColMax = num + 1 ;  // + 1 car on indente ensuite ! + 1 car on ajoute la ligne
        }
        String colspan = String.format("colspan='%d'", numberOfColMax);

        for(String key: hashmap.keySet()){
            SymboleDeclStruct symbole = hashmap.get(key);
            content = content + String.format("<tr> <td> line .%d</td> <td>Struct %s </td> %s </tr>",symbole.getDefinitionLine(),symbole.getName(),"<td></td>".repeat(numberOfColMax - 2)) ;
           
            ArrayList<Symbole> declVars = symbole.getListDeclVars();
            int numberOfCol = declVars.size();

            if(numberOfCol > 0){
                content = content + "<tr>" + "<td></td>"; // sur une nouvelle ligne "indentée";
            }

            for(Symbole symbole2 : declVars){
                if(symbole2 instanceof SymboleStruct){
                    SymboleStruct symboleStruct =  (SymboleStruct) symbole2;
                    content = content + String.format("<td> Struct %s * %s </td> ", symboleStruct.getStruct().getName(),symboleStruct.getName());
                }

                else if(symbole2 instanceof SymboleInt){
                    SymboleInt symboleInt = (SymboleInt) symbole2;
                    content = content + String.format("<td> int %s </td>", symboleInt.getName());
                }
            }

            if(numberOfCol > 0){
                if(numberOfColMax - numberOfCol -1 > 0){ // on ajout les cases manquantes
                    content = content + "<td></td>".repeat(numberOfColMax-numberOfCol - 1);
                }
                content = content + "</tr>";
            }
        }

        

        this.nodeBuffer += String.format("\t%s [shape=\"plaintext\",label=<<table border='1' cellborder='1' cellspacing='1'> <tr><td %s> <b> NameSpaceStruct </b></td>  </tr>  %s </table>>];\n", node, colspan, content);
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
            }

            else if(symbole instanceof SymboleStruct){
                SymboleStruct sym = (SymboleStruct) symbole;
                content = content + String.format("<tr><td> Struct %s </td> <td> %s </td> <td> %d </td> <td> line . %d  </td> </tr>", sym.getStruct().getName(), sym.getName(), sym.getDeplacement(),sym.getDefinitionLine() );
            }

            else if ( symbole instanceof SymboleBloc){
                if(symbole instanceof SymboleFonction){
                    SymboleFonction sym = (SymboleFonction) symbole ; 
                    String newNode = this.nextState(); 
                    Tds newTds = sym.getTds();
                    addTds(sym.getName(),newNode, newTds);
                    this.linkBuffer = linkBuffer + String.format("\t%s -> %s;\n",node,newNode);
                }

                else if(symbole instanceof SymboleBlocAnonyme){
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
        addNameSpaceStruct(this.nextState(),tds);
        addTds("",this.nextState(), tds);
    }
    
}
