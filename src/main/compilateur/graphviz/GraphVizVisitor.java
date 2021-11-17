package compilateur.graphviz;

import java.io.FileOutputStream;
import java.io.IOException;

import compilateur.ast.AstVisitor;
import compilateur.ast.Affectation;
import compilateur.ast.Bloc;
import compilateur.ast.CharNode;
import compilateur.ast.Decl;
import compilateur.ast.DeclFctInt;
import compilateur.ast.DeclFctStruct;
import compilateur.ast.DeclVarInt;
import compilateur.ast.DeclVarStruct;
import compilateur.ast.Decl_typ;
import compilateur.ast.Different;
import compilateur.ast.Division;
import compilateur.ast.Egal;
import compilateur.ast.Expr_et;
import compilateur.ast.Expr_ou;
import compilateur.ast.Fichier;
import compilateur.ast.Fleche;
import compilateur.ast.Idf;
import compilateur.ast.IdfParenthesis;
import compilateur.ast.IdfParenthesisEmpty;
import compilateur.ast.IfThen;
import compilateur.ast.IfThenElse;
import compilateur.ast.Inferieur;
import compilateur.ast.InferieurEgal;
import compilateur.ast.IntNode;
import compilateur.ast.Minus;
import compilateur.ast.Multiplication;
import compilateur.ast.ParamInt;
import compilateur.ast.ParamListMulti;
import compilateur.ast.ParamStruct;
import compilateur.ast.Plus;
import compilateur.ast.Return;
import compilateur.ast.Sizeof;
import compilateur.ast.Superieur;
import compilateur.ast.SuperieurEgal;
import compilateur.ast.MoinsUnaire;
import compilateur.ast.Negation;
import compilateur.ast.While;

public class GraphVizVisitor implements AstVisitor<String> {

    private int state;
    private String nodeBuffer;
    private String linkBuffer;

    public GraphVizVisitor(){
        this.state = 0;
        this.nodeBuffer = "digraph \"ast\"{\n\n\tnodesep=1;\n\tranksep=1;\n\n";
        this.linkBuffer = "\n";
    }

    public void dumpGraph(String filepath) throws IOException{
            
        FileOutputStream output = new FileOutputStream(filepath);

        String buffer = this.nodeBuffer + this.linkBuffer;
        byte[] strToBytes = buffer.getBytes();

        output.write(strToBytes);

        output.close();

    }


    private String nextState(){
        int returnedState = this.state;
        this.state++;
        return "N"+ returnedState;
    }

    private void addTransition(String from,String dest){
        this.linkBuffer += String.format("\t%s -> %s; \n", from,dest);

    }

    private void addNode(String node,String label){
        this.nodeBuffer += String.format("\t%s [label=\"%s\", shape=\"box\"];\n", node,label);

    }
    
    @Override public String visit(Fichier fichier){
        String nodeIdentifier = this.nextState();

        String instructionsState =fichier.instructions.accept(this);

        this.addNode(nodeIdentifier, "Fichier");
        this.addTransition(nodeIdentifier, instructionsState);

        return nodeIdentifier;
    }
    @Override public String visit(Idf idf){
        String nodeIdentifier = this.nextState();

        this.addNode(nodeIdentifier, idf.name);

        return nodeIdentifier;
    }
    @Override public String visit(DeclVarInt declVarInt){ return ""; }
    @Override public String visit(Decl decl){ return ""; }
    @Override public String visit(DeclVarStruct declVarStruct){ return ""; }
    @Override public String visit(Decl_typ decl_typ){ return ""; }
    @Override public String visit(DeclFctInt declFctInt){ return ""; }
    @Override public String visit(DeclFctStruct declFctStruct){ return ""; }
    @Override public String visit(ParamListMulti paramListMulti){ return ""; }
    @Override public String visit(ParamInt paramInt){ return ""; }
    @Override public String visit(ParamStruct paramStruct){ return ""; }
    @Override public String visit(Sizeof sizeof){ return ""; }
    @Override public String visit(IdfParenthesis idfParenthesis){ return ""; }
    @Override public String visit(IdfParenthesisEmpty idfParenthesisEmpty){ return ""; }
    @Override public String visit(IfThen ifThen){
        String nodeIdentifier = this.nextState();

        String conditionState = ifThen.condition.accept(this);
        String thenBlockState = ifThen.thenBlock.accept(this);

        this.addNode(nodeIdentifier, "IfThen");

        this.addTransition(nodeIdentifier, conditionState);
        this.addTransition(nodeIdentifier, thenBlockState);

        return nodeIdentifier;
    }
    @Override public String visit(IfThenElse ifThenElse){
        String nodeIdentifier = this.nextState();

        String conditionState = ifThenElse.condition.accept(this);
        String thenBlockState = ifThenElse.thenBlock.accept(this);
        String elseBlockState = ifThenElse.elseBlock.accept(this);
        
        this.addNode(nodeIdentifier, "IfThenElse");

        this.addTransition(nodeIdentifier, conditionState);
        this.addTransition(nodeIdentifier, thenBlockState);
        this.addTransition(nodeIdentifier, elseBlockState);

        return nodeIdentifier;
    }
    @Override public String visit(While while1){ return ""; }
    @Override public String visit(Return return1){ return ""; }
    @Override public String visit(Bloc bloc){ return ""; }
    @Override public String visit(CharNode charNode){ return ""; }
    @Override public String visit(IntNode intNode){
        String nodeIdentifier = this.nextState();

        this.addNode(nodeIdentifier, String.valueOf(intNode.parseInt));

        return nodeIdentifier;

    }
    @Override public String visit(Affectation affectation){
        String nodeIdentifier = this.nextState();

        String idfState = affectation.left.accept(this);
        String expressionState = affectation.right.accept(this);

        this.addNode(nodeIdentifier, "Affectation");
        this.addTransition(nodeIdentifier, idfState);
        this.addTransition(nodeIdentifier, expressionState);

        return nodeIdentifier;
    }
    @Override public String visit(Expr_ou expr_ou){ return ""; }
    @Override public String visit(Expr_et expr_et){ return ""; }
    @Override public String visit(Egal egal){ return ""; }
    @Override public String visit(Different dif){ return ""; }
    @Override public String visit(Inferieur inf){ return ""; }
    @Override public String visit (InferieurEgal infEgal){ return ""; }
    @Override public String visit (Superieur sup){ return ""; }
    @Override public String visit (SuperieurEgal supEgal){ return ""; }
    @Override public String visit (Plus plus){
        String nodeIdentifier = this.nextState();

        String leftState = plus.left.accept(this);
        String rightState = plus.right.accept(this);

        this.addNode(nodeIdentifier, "+");
        
        this.addTransition(nodeIdentifier, leftState);
        this.addTransition(nodeIdentifier, rightState);

        return nodeIdentifier;
    }
    @Override public String visit (Minus minus){
        String nodeIdentifier = this.nextState();

        String leftState = minus.left.accept(this);
        String rightState = minus.right.accept(this);

        this.addNode(nodeIdentifier, "-");
        
        this.addTransition(nodeIdentifier, leftState);
        this.addTransition(nodeIdentifier, rightState);

        return nodeIdentifier;
    }
    @Override public String visit (Division div){
        String nodeIdentifier = this.nextState();

        String leftState = div.left.accept(this);
        String rightState = div.right.accept(this);

        this.addNode(nodeIdentifier, "/");
        
        this.addTransition(nodeIdentifier, leftState);
        this.addTransition(nodeIdentifier, rightState);

        return nodeIdentifier;
    }
    @Override public String visit (Multiplication mult){
        String nodeIdentifier = this.nextState();

        String leftState = mult.left.accept(this);
        String rightState = mult.right.accept(this);

        this.addNode(nodeIdentifier, "*");
        
        this.addTransition(nodeIdentifier, leftState);
        this.addTransition(nodeIdentifier, rightState);

        return nodeIdentifier;
    }
    @Override public String visit (Fleche fleche){ return ""; }
    @Override public String visit (MoinsUnaire unaire){ return ""; }
    @Override public String visit (Negation unaire){ return ""; }


    // @Override
    // public String visit(Print print) {

    //     String nodeIdentifier = this.nextState();

    //     String valueState = print.value.accept(this);

    //     this.addNode(nodeIdentifier, "print");
    //     this.addTransition(nodeIdentifier, valueState);

    //     return nodeIdentifier;
    // }
    
}
