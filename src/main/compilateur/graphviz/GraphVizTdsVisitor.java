package compilateur.graphviz;

import java.io.FileOutputStream;
import java.io.IOException;
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


    // private void addNameSpaceStruct(Tds tds){
    //     NameSpaceStruct nameSpace = tds.getNameSpaceStruct();
    //     HashMap<String,SymboleDeclStruct> hashmap = nameSpace.getHashMap();
    //     for(String keys: hashmap.keySet()){
    //         SymboleDeclStruct 
    //     }
    // }

    private void addTds(String node, Tds tds){
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
                String idf = sym.getName() + region + imbrication;
                content = content + String.format("| { < %s > int | %s | %d } ", idf, sym.getName(), sym.getDeplacement() );
            }

            else if(symbole instanceof SymboleStruct){
                SymboleStruct sym = (SymboleStruct) symbole;
                String idf = sym.getName() + region + imbrication;
                content = content + String.format("| { < %s > Struct %s | %s | %d } ", idf, sym.getStruct().getName(), sym.getName(), sym.getDeplacement() );
            }

            else if ( symbole instanceof SymboleBloc){
                if(symbole instanceof SymboleFonction){
                    SymboleFonction sym = (SymboleFonction) symbole ; 
                    String idf = sym.getName()  + region + imbrication;
                    content = content + String.format("| {<%s> %s | return : %s } ", idf, sym.getName(), sym.getReturnType());
                    String newNode = this.nextState(); // Change with recursive node creation 
                    Tds newTds = sym.getTds();
                    addTds(newNode, newTds);
                    int newRegion = newTds.getNumRegion();
                    this.linkBuffer = linkBuffer + String.format("\t%s:%s -> %s:%d;\n",node,idf,newNode,newRegion);

                }

                else if(symbole instanceof SymboleBlocAnonyme){
                    SymboleBlocAnonyme sym = (SymboleBlocAnonyme) symbole ; 
                    String idf = symboleKey  + region + imbrication;
                    content = content + String.format("{<%s> %s }", idf,symboleKey);
                    String newNode = this.nextState(); // Change with recursive node creation 
                    Tds newTds = sym.getTds();
                    addTds(newNode, newTds);
                    int newRegion = newTds.getNumRegion();
                    this.linkBuffer = linkBuffer + String.format("\t%s:%s -> %s:%d;\n",node,idf,newNode,newRegion);
                }
            }

        }

        this.nodeBuffer += String.format("\t%s [shape=\"record\",label=\" <%s> %s | %s %s \"];\n", node, region,region,imbrication, content);
    }


    public void createGraph(Tds tds){
        addTds(this.nextState(), tds);
    }
    
}
