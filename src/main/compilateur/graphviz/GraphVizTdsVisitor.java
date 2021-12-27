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

    private String addStruct(Symbole symbole){
        SymboleStruct symboleStruct =  (SymboleStruct) symbole;
        String content = String.format("| Struct %s * %s ", symboleStruct.getStruct().getName(),symboleStruct.getName());
        return content;
    }


    private void addNameSpaceStruct(String node,Tds tds){
        String content = "";
        NameSpaceStruct nameSpace = tds.getNameSpaceStruct();
        HashMap<String,SymboleDeclStruct> hashmap = nameSpace.getHashMap();
        for(String key: hashmap.keySet()){
            SymboleDeclStruct symbole = hashmap.get(key);
            content = content + String.format("| {Struct %s", symbole.getName());

            ArrayList<Symbole> declVars = symbole.getListDeclVars();
            Boolean nonEmpty = ! declVars.isEmpty();
            // if(nonEmpty) content = content + "|{";
            for(Symbole symbole2 : declVars){
                if(symbole2 instanceof SymboleStruct){
                    content = content + addStruct(symbole2);
                }

                else if(symbole2 instanceof SymboleInt){
                    SymboleInt symboleInt = (SymboleInt) symbole2;
                    content = content + String.format("| int %s", symboleInt.getName());
                }
            }
            if(nonEmpty) content = content + "}";
        }
        // if(!hashmap.isEmpty()){
        //     content = content + "}";
        // }

        this.nodeBuffer += String.format("\t%s [shape=\"record\",label=\" NameSpaceStruct %s \"];\n", node, content);
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
                    // content = content + String.format("| {<%s> %s | return : %s } ", idf, sym.getName(), sym.getReturnType());
                    String newNode = this.nextState(); 
                    Tds newTds = sym.getTds();
                    addTds(sym.getName(),newNode, newTds);
                    int newRegion = newTds.getNumRegion();
                    // this.linkBuffer = linkBuffer + String.format("\t%s:%s -> %s:%d;\n",node,idf,newNode,newRegion);
                    this.linkBuffer = linkBuffer + String.format("\t%s -> %s:%d;\n",node,newNode,newRegion);
                }

                else if(symbole instanceof SymboleBlocAnonyme){
                    SymboleBlocAnonyme sym = (SymboleBlocAnonyme) symbole ; 
                    String idf = symboleKey  + region + imbrication;
                    // content = content + String.format("{<%s> %s }", idf,symboleKey);
                    String newNode = this.nextState(); // Change with recursive node creation 
                    Tds newTds = sym.getTds();
                    addTds(symboleKey, newNode, newTds);
                    int newRegion = newTds.getNumRegion();
                    // this.linkBuffer = linkBuffer + String.format("\t%s:%s -> %s:%d;\n",node,idf,newNode,newRegion);
                    this.linkBuffer = linkBuffer + String.format("\t%s -> %s:%d;\n",node,newNode,newRegion);
                }
            }

        }

        this.nodeBuffer += String.format("\t%s [shape=\"record\",label=\" <%s> %s | %s | %s %s \"];\n", node, region,region,imbrication, name, content);
    }


    public void createGraph(Tds tds){
        addNameSpaceStruct(this.nextState(),tds);
        addTds("",this.nextState(), tds);
    }
    
}
