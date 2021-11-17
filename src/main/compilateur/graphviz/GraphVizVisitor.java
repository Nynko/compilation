package compilateur.graphviz;

import java.io.FileOutputStream;
import java.io.IOException;

import compilateur.ast.AstVisitor;
import compilateur.ast.Affectation;
import compilateur.ast.Ast;
import compilateur.ast.Bloc;
import compilateur.ast.CharNode;
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

    private void addTransition(String from,String dest){
        this.linkBuffer += String.format("\t%s -> %s; \n", from,dest);

    }

    private void addNode(String node,String label){
        this.nodeBuffer += String.format("\t%s [label=\"%s\", shape=\"box\"];\n", node,label);

    }
    
    @Override public String visit(Fichier fichier){
        String nodeIdentifier = this.nextState();
        this.addNode(nodeIdentifier, "Fichier");
        try {
            String instructionsState =fichier.instructions.accept(this);
            this.addTransition(nodeIdentifier, instructionsState);
        } catch (NullPointerException e) {
            // nothing
        }
        return nodeIdentifier;
    }
    @Override public String visit(Idf idf){
        String nodeIdentifier = this.nextState();

        this.addNode(nodeIdentifier, idf.name);

        return nodeIdentifier;
    }
    @Override public String visit(DeclVarInt declVarInt){
        String nodeIdentifier = this.nextState();
        this.addNode(nodeIdentifier, "DeclVarInt");
        for (Ast ast:declVarInt.idf){

            String astState = ast.accept(this);
            this.addTransition(nodeIdentifier, astState);

        }

                return nodeIdentifier;
    }
    @Override public String visit(DeclVarStruct declVarStruct){
        String nodeIdentifier = this.nextState();
        this.addNode(nodeIdentifier, "DeclVarStruct");
        
        for (Ast ast:declVarStruct.idf){

            String astState = ast.accept(this);
            this.addTransition(nodeIdentifier, astState);

        }

        return nodeIdentifier;
    }
    @Override public String visit(Decl_typ decl_typ){
        String nodeIdentifier = this.nextState();
        this.addNode(nodeIdentifier, "DeclTyp");
        String idf = decl_typ.idf.accept(this);
        this.addTransition(nodeIdentifier, idf);
        for (Ast ast:decl_typ.decl){
            String astState = ast.accept(this);
            this.addTransition(nodeIdentifier, astState);
        }

        return nodeIdentifier;
    }
    @Override public String visit(DeclFctInt declFctInt){
        String nodeIdentifier = this.nextState();
        this.addNode(nodeIdentifier, "DeclFctInt");
        this.addTransition(nodeIdentifier, declFctInt.Idf.accept(this));
        this.addTransition(nodeIdentifier, declFctInt.param.accept(this));
        this.addTransition(nodeIdentifier, declFctInt.bloc.accept(this));
        return nodeIdentifier;
    }
    @Override public String visit(DeclFctStruct declFctStruct){
        String nodeIdentifier = this.nextState();
        this.addNode(nodeIdentifier, "DeclFctStruct");
        this.addTransition(nodeIdentifier, declFctStruct.Idf0.accept(this));
        this.addTransition(nodeIdentifier, declFctStruct.Idf1.accept(this));
        this.addTransition(nodeIdentifier, declFctStruct.param.accept(this));
        this.addTransition(nodeIdentifier, declFctStruct.bloc.accept(this));
        return nodeIdentifier;
    }
    @Override public String visit(ParamListMulti paramListMulti){
        String nodeIdentifier = this.nextState();
        this.addNode(nodeIdentifier, "ParamListMulti");

        for (Ast ast:paramListMulti.paramList){
            String astState = ast.accept(this);
            this.addTransition(nodeIdentifier, astState);
        }
        
        return nodeIdentifier;
    }
    @Override public String visit(ParamInt paramInt){
        String nodeIdentifier = this.nextState();
        this.addNode(nodeIdentifier, "ParamInt");
        this.addTransition(nodeIdentifier, paramInt.name.accept(this));
        return nodeIdentifier;
    }
    @Override public String visit(ParamStruct paramStruct){
        String nodeIdentifier = this.nextState();
        this.addNode(nodeIdentifier, "ParamStruct");
        this.addTransition(nodeIdentifier, paramStruct.idf0.accept(this));
        this.addTransition(nodeIdentifier, paramStruct.idf1.accept(this));
        return nodeIdentifier;
    }
    @Override public String visit(Sizeof sizeof){
        String nodeIdentifier = this.nextState();
        this.addNode(nodeIdentifier, "Sizeof");
        this.addTransition(nodeIdentifier, sizeof.name.accept(this));
        return nodeIdentifier;
    }
    @Override public String visit(IdfParenthesis idfParenthesis){
        String nodeIdentifier = this.nextState();
        this.addTransition(nodeIdentifier, idfParenthesis.idf.accept(this));
        for (Ast ast:idfParenthesis.exprList){
            String astState = ast.accept(this);
            this.addTransition(nodeIdentifier, astState);
        }
        return nodeIdentifier;
    }
    @Override public String visit(IdfParenthesisEmpty idfParenthesisEmpty){
        String nodeIdentifier = this.nextState();
        this.addTransition(nodeIdentifier, idfParenthesisEmpty.idf.accept(this));
        return nodeIdentifier;
    }
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
    @Override public String visit(While while1){
        String nodeIdentifier = this.nextState();
        this.addNode(nodeIdentifier, "While");
        this.addTransition(nodeIdentifier, while1.condition.accept(this));
        this.addTransition(nodeIdentifier, while1.doBlock.accept(this));
        return nodeIdentifier;
    }
    @Override public String visit(Return return1){
        String nodeIdentifier = this.nextState();
        this.addNode(nodeIdentifier, "Return");
        this.addTransition(nodeIdentifier, return1.expr.accept(this));
        return nodeIdentifier;
    }
    @Override public String visit(Bloc bloc){
        String nodeIdentifier = this.nextState();
        this.addNode(nodeIdentifier, "Bloc");
        
        for (Ast ast:bloc.instList){
            String astState = ast.accept(this);
            this.addTransition(nodeIdentifier, astState);
        }

        return nodeIdentifier;
    }
    @Override public String visit(CharNode charNode){
        String nodeIdentifier = this.nextState();
        this.addTransition(nodeIdentifier, charNode.string);
        
        return nodeIdentifier;
    }
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
    @Override public String visit(Expr_ou expr_ou){
        String nodeIdentifier = this.nextState();
        this.addNode(nodeIdentifier, "||");
        this.addTransition(nodeIdentifier, expr_ou.left.accept(this));
        this.addTransition(nodeIdentifier, expr_ou.right.accept(this));
        return nodeIdentifier;
    }
    @Override public String visit(Expr_et expr_et){
        String nodeIdentifier = this.nextState();
        this.addNode(nodeIdentifier, "&&");
        this.addTransition(nodeIdentifier, expr_et.left.accept(this));
        this.addTransition(nodeIdentifier, expr_et.right.accept(this));
        return nodeIdentifier;
    }
    @Override public String visit(Egal egal){
        String nodeIdentifier = this.nextState();
        this.addNode(nodeIdentifier, "==");
        this.addTransition(nodeIdentifier, egal.left.accept(this));
        this.addTransition(nodeIdentifier, egal.right.accept(this));
        return nodeIdentifier;
    }
    @Override public String visit(Different dif){
        String nodeIdentifier = this.nextState();
        this.addNode(nodeIdentifier, "!=");
        this.addTransition(nodeIdentifier, dif.left.accept(this));
        this.addTransition(nodeIdentifier, dif.right.accept(this));
        return nodeIdentifier;
    }
    @Override public String visit(Inferieur inf){
        String nodeIdentifier = this.nextState();
        this.addNode(nodeIdentifier, "<");
        this.addTransition(nodeIdentifier, inf.left.accept(this));
        this.addTransition(nodeIdentifier, inf.right.accept(this));
        return nodeIdentifier;
    }
    @Override public String visit (InferieurEgal infEgal){
        String nodeIdentifier = this.nextState();
        this.addNode(nodeIdentifier, "<=");
        this.addTransition(nodeIdentifier, infEgal.left.accept(this));
        this.addTransition(nodeIdentifier, infEgal.right.accept(this));
        return nodeIdentifier;
    }
    @Override public String visit (Superieur sup){
        String nodeIdentifier = this.nextState();
        this.addNode(nodeIdentifier, ">");
        this.addTransition(nodeIdentifier, sup.left.accept(this));
        this.addTransition(nodeIdentifier, sup.right.accept(this));
        return nodeIdentifier;
    }
    @Override public String visit (SuperieurEgal supEgal){
        String nodeIdentifier = this.nextState();
        this.addNode(nodeIdentifier, ">=");
        this.addTransition(nodeIdentifier, supEgal.left.accept(this));
        this.addTransition(nodeIdentifier, supEgal.right.accept(this));
        return nodeIdentifier;
    }
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
    @Override public String visit (Fleche fleche){
        String nodeIdentifier = this.nextState();
        this.addNode(nodeIdentifier, "->");
        this.addTransition(nodeIdentifier, fleche.left.accept(this));
        this.addTransition(nodeIdentifier, fleche.right.accept(this));
        return nodeIdentifier;
    }
    @Override public String visit (MoinsUnaire unaire){
        String nodeIdentifier = this.nextState();
        this.addNode(nodeIdentifier, "-");
        this.addTransition(nodeIdentifier, unaire.noeud.accept(this));
        return nodeIdentifier;
    }
    @Override public String visit (Negation unaire){
        String nodeIdentifier = this.nextState();
        this.addNode(nodeIdentifier, "!");
        this.addTransition(nodeIdentifier, unaire.noeud.accept(this));
        return nodeIdentifier;
    }


    // @Override
    // public String visit(Print print) {

    //     String nodeIdentifier = this.nextState();

    //     String valueState = print.value.accept(this);

    //     this.addNode(nodeIdentifier, "print");
    //     this.addTransition(nodeIdentifier, valueState);

    //     return nodeIdentifier;
    // }
    
}
