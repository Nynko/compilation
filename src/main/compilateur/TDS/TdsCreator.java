package compilateur.TDS;


import compilateur.ast.AstVisitor;

import java.util.ArrayList;

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
import compilateur.ast.Semicolon;
import compilateur.ast.Sizeof;
import compilateur.ast.Superieur;
import compilateur.ast.SuperieurEgal;
import compilateur.ast.MoinsUnaire;
import compilateur.ast.Negation;
import compilateur.ast.While;

public class TdsCreator implements AstVisitor<Symbole>{

    @Override public Symbole visit(Fichier fichier){
        // Symbole symbole = new Symbole();

        // if (fichier.instructions != null) {

        //     for (Ast ast:fichier.instructions){
        //         Symbole instructionsState = ast.accept(this);
        //         if(instructionsState!= null){
        //             instructionsState.linkPere(symbole);
        //         }
        //     }
        // }

        return new Str("");
    }

    @Override public Symbole visit(Idf idf){
        return new Str(""); new ValueString(idf.name);
        return new Str(idf.name);
    }

    @Override public Symbole visit(DeclVarInt declVarInt){

        ArrayList<Ast> liste = declVarInt.idf;
        int size = liste.size();

        Tds tds = new Tds();

        for(int i = 0 ; i < size -1 ; i++){
            tds.addSymbole(liste.get(i).accept(this));             
        }
        
        return new Str("");
    }
    @Override public Symbole visit(DeclVarStruct declVarStruct){
        // Symbole nodeIdentifier = this.nextState();
        // this.addNode(nodeIdentifier, "DeclVarStruct");
        
        // for (Ast ast:declVarStruct.idf){

        //     Symbole astState = ast.accept(this);
        //     this.addTransition(nodeIdentifier, astState);

        // }

        return new Str("");
    }
    @Override public Symbole visit(Decl_typ decl_typ){
        // Symbole nodeIdentifier = this.nextState();
        // this.addNode(nodeIdentifier, "DeclTyp");
        // Symbole idf = decl_typ.idf.accept(this);
        // this.addTransition(nodeIdentifier, idf);
        // for (Ast ast:decl_typ.decl){
        //     Symbole astState = ast.accept(this);
        //     this.addTransition(nodeIdentifier, astState);
        // }

        return new Str("");
    }
    @Override public Symbole visit(DeclFctInt declFctInt){
        // Symbole nodeIdentifier = this.nextState();
        // this.addNode(nodeIdentifier, "DeclFctInt");
        // this.addTransition(nodeIdentifier, declFctInt.Idf.accept(this));
        // if (declFctInt.param != null) {
        //     this.addTransition(nodeIdentifier, declFctInt.param.accept(this));
        // }
        // this.addTransition(nodeIdentifier, declFctInt.bloc.accept(this));
        return new Str("");
    }
    @Override public Symbole visit(DeclFctStruct declFctStruct){
        // Symbole nodeIdentifier = this.nextState();
        // this.addNode(nodeIdentifier, "DeclFctStruct");
        // this.addTransition(nodeIdentifier, declFctStruct.Idf0.accept(this));
        // this.addTransition(nodeIdentifier, declFctStruct.Idf1.accept(this));
        // this.addTransition(nodeIdentifier, declFctStruct.param.accept(this));
        // this.addTransition(nodeIdentifier, declFctStruct.bloc.accept(this));
        return new Str(""); 
    }
    @Override public Symbole visit(ParamListMulti paramListMulti){
        // Symbole nodeIdentifier = this.nextState();
        // this.addNode(nodeIdentifier, "ParamListMulti");

        // for (Ast ast:paramListMulti.paramList){
        //     Symbole astState = ast.accept(this);
        //     this.addTransition(nodeIdentifier, astState);
        // }
        
        return new Str(""); 
    }
    @Override public Symbole visit(ParamInt paramInt){
        // Symbole nodeIdentifier = this.nextState();
        // this.addNode(nodeIdentifier, "ParamInt");
        // this.addTransition(nodeIdentifier, paramInt.name.accept(this));
        return new Str(""); 
    }
    @Override public Symbole visit(ParamStruct paramStruct){
        // Symbole nodeIdentifier = this.nextState();
        // this.addNode(nodeIdentifier, "ParamStruct");
        // this.addTransition(nodeIdentifier, paramStruct.idf0.accept(this));
        // this.addTransition(nodeIdentifier, paramStruct.idf1.accept(this));
        return new Str(""); 
    }
    @Override public Symbole visit(Sizeof sizeof){
        // Symbole nodeIdentifier = this.nextState();
        // this.addNode(nodeIdentifier, "Sizeof");
        // this.addTransition(nodeIdentifier, sizeof.name.accept(this));
        return new Str(""); 
    }
    @Override public Symbole visit(IdfParenthesis idfParenthesis){
        // Symbole nodeIdentifier = this.nextState();
        // this.addTransition(nodeIdentifier, idfParenthesis.idf.accept(this));
        // for (Ast ast:idfParenthesis.exprList){
        //     Symbole astState = ast.accept(this);
        //     this.addTransition(nodeIdentifier, astState);
        // }
        return new Str(""); 
    }
    @Override public Symbole visit(IdfParenthesisEmpty idfParenthesisEmpty){
        // Symbole nodeIdentifier = this.nextState();
        // this.addTransition(nodeIdentifier, idfParenthesisEmpty.idf.accept(this));
        return new Str(""); 
    }
    @Override public Symbole visit(IfThen ifThen){
        // Symbole nodeIdentifier = this.nextState();

        // Symbole conditionState = ifThen.condition.accept(this);
        // Symbole thenBlockState = ifThen.thenBlock.accept(this);

        // this.addNode(nodeIdentifier, "IfThen");

        // this.addTransition(nodeIdentifier, conditionState);
        // this.addTransition(nodeIdentifier, thenBlockState);

        return new Str(""); 
    }
    @Override public Symbole visit(IfThenElse ifThenElse){
        // Symbole nodeIdentifier = this.nextState();

        // Symbole conditionState = ifThenElse.condition.accept(this);
        // Symbole thenBlockState = ifThenElse.thenBlock.accept(this);
        // Symbole elseBlockState = ifThenElse.elseBlock.accept(this);
        
        // this.addNode(nodeIdentifier, "IfThenElse");

        // this.addTransition(nodeIdentifier, conditionState);
        // this.addTransition(nodeIdentifier, thenBlockState);
        // this.addTransition(nodeIdentifier, elseBlockState);

        return new Str(""); 
    }
    @Override public Symbole visit(While while1){
        // Symbole nodeIdentifier = this.nextState();
        // this.addNode(nodeIdentifier, "While");
        // this.addTransition(nodeIdentifier, while1.condition.accept(this));
        // this.addTransition(nodeIdentifier, while1.doBlock.accept(this));
        return new Str(""); 
    }
    @Override public Symbole visit(Return return1){
        // Symbole nodeIdentifier = this.nextState();
        // this.addNode(nodeIdentifier, "Return");
        // this.addTransition(nodeIdentifier, return1.expr.accept(this));
        return new Str(""); 
    }
    @Override public Symbole visit(Bloc bloc){
        // Symbole nodeIdentifier = this.nextState();
        // this.addNode(nodeIdentifier, "Bloc");
        
        // for (Ast ast:bloc.instList){
        //     Symbole astState = ast.accept(this);
        //     this.addTransition(nodeIdentifier, astState);
        // }

        return new Str(""); 
    }
    @Override public Symbole visit(CharNode charNode){
        // Symbole nodeIdentifier = this.nextState();
        // this.addNode(nodeIdentifier, charNode.Symbole);
        
        return new Str(""); 
    }
    @Override public Symbole visit(IntNode intNode){
        Symbole nodeIdentifier = this.nextState();

        // this.addNode(nodeIdentifier, Symbole.valueOf(intNode.parseInt));

        return new Str(""); 

    }
    @Override public Symbole visit(Affectation affectation){
        // Symbole nodeIdentifier = this.nextState();

        // Symbole idfState = affectation.left.accept(this);
        // Symbole expressionState = affectation.right.accept(this);

        // this.addNode(nodeIdentifier, "Affectation");
        // this.addTransition(nodeIdentifier, idfState);
        // this.addTransition(nodeIdentifier, expressionState);

        return new Str(""); 
    }
    @Override public Symbole visit(Expr_ou expr_ou){
        // Symbole nodeIdentifier = this.nextState();
        // this.addNode(nodeIdentifier, "||");
        // this.addTransition(nodeIdentifier, expr_ou.left.accept(this));
        // this.addTransition(nodeIdentifier, expr_ou.right.accept(this));
        return new Str(""); 
    }
    @Override public Symbole visit(Expr_et expr_et){
        // Symbole nodeIdentifier = this.nextState();
        // this.addNode(nodeIdentifier, "&&");
        // this.addTransition(nodeIdentifier, expr_et.left.accept(this));
        // this.addTransition(nodeIdentifier, expr_et.right.accept(this));
        return new Str(""); 
    }
    @Override public Symbole visit(Egal egal){
        // Symbole nodeIdentifier = this.nextState();
        // this.addNode(nodeIdentifier, "==");
        // this.addTransition(nodeIdentifier, egal.left.accept(this));
        // this.addTransition(nodeIdentifier, egal.right.accept(this));
        return new Str(""); 
    }
    @Override public Symbole visit(Different dif){
        // Symbole nodeIdentifier = this.nextState();
        // this.addNode(nodeIdentifier, "!=");
        // this.addTransition(nodeIdentifier, dif.left.accept(this));
        // this.addTransition(nodeIdentifier, dif.right.accept(this));
        return new Str(""); 
    }
    @Override public Symbole visit(Inferieur inf){
        // Symbole nodeIdentifier = this.nextState();
        // this.addNode(nodeIdentifier, "<");
        // this.addTransition(nodeIdentifier, inf.left.accept(this));
        // this.addTransition(nodeIdentifier, inf.right.accept(this));
        return new Str(""); 
    }
    @Override public Symbole visit (InferieurEgal infEgal){
        // Symbole nodeIdentifier = this.nextState();
        // this.addNode(nodeIdentifier, "<=");
        // this.addTransition(nodeIdentifier, infEgal.left.accept(this));
        // this.addTransition(nodeIdentifier, infEgal.right.accept(this));
        return new Str(""); 
    }
    @Override public Symbole visit (Superieur sup){
        // Symbole nodeIdentifier = this.nextState();
        // this.addNode(nodeIdentifier, ">");
        // this.addTransition(nodeIdentifier, sup.left.accept(this));
        // this.addTransition(nodeIdentifier, sup.right.accept(this));
        return new Str(""); 
    }
    @Override public Symbole visit (SuperieurEgal supEgal){
        // Symbole nodeIdentifier = this.nextState();
        // this.addNode(nodeIdentifier, ">=");
        // this.addTransition(nodeIdentifier, supEgal.left.accept(this));
        // this.addTransition(nodeIdentifier, supEgal.right.accept(this));
        return new Str(""); 
    }
    @Override public Symbole visit (Plus plus){
        // Symbole nodeIdentifier = this.nextState();

        // Symbole leftState = plus.left.accept(this);
        // Symbole rightState = plus.right.accept(this);

        // this.addNode(nodeIdentifier, "+");
        
        // this.addTransition(nodeIdentifier, leftState);
        // this.addTransition(nodeIdentifier, rightState);

        return new Str(""); 
    }
    @Override public Symbole visit (Minus minus){
        // Symbole nodeIdentifier = this.nextState();

        // Symbole leftState = minus.left.accept(this);
        // Symbole rightState = minus.right.accept(this);

        // this.addNode(nodeIdentifier, "-");
        
        // this.addTransition(nodeIdentifier, leftState);
        // this.addTransition(nodeIdentifier, rightState);

        return new Str(""); 
    }
    @Override public Symbole visit (Division div){
        // Symbole nodeIdentifier = this.nextState();

        // Symbole leftState = div.left.accept(this);
        // Symbole rightState = div.right.accept(this);

        // this.addNode(nodeIdentifier, "/");
        
        // this.addTransition(nodeIdentifier, leftState);
        // this.addTransition(nodeIdentifier, rightState);

        return new Str(""); 
    }
    @Override public Symbole visit (Multiplication mult){
        // Symbole nodeIdentifier = this.nextState();

        // Symbole leftState = mult.left.accept(this);
        // Symbole rightState = mult.right.accept(this);

        // this.addNode(nodeIdentifier, "*");
        
        // this.addTransition(nodeIdentifier, leftState);
        // this.addTransition(nodeIdentifier, rightState);

        return new Str(""); 
    }
    @Override public Symbole visit (Fleche fleche){
        // Symbole nodeIdentifier = this.nextState();
        // this.addNode(nodeIdentifier, "->");
        // this.addTransition(nodeIdentifier, fleche.left.accept(this));
        // this.addTransition(nodeIdentifier, fleche.right.accept(this));
        return new Str(""); 
    }
    @Override public Symbole visit (MoinsUnaire unaire){
        // Symbole nodeIdentifier = this.nextState();
        // this.addNode(nodeIdentifier, "-");
        // this.addTransition(nodeIdentifier, unaire.noeud.accept(this));
        return new Str(""); 
    }
    @Override public Symbole visit (Negation unaire){
        // Symbole nodeIdentifier = this.nextState();
        // this.addNode(nodeIdentifier, "!");
        // this.addTransition(nodeIdentifier, unaire.noeud.accept(this));
        return new Str(""); 
    }

    @Override
    public Symbole visit(Semicolon semicolon) {
        // Symbole nodeIdentifier = this.nextState();
        // this.addNode(nodeIdentifier, "InstEmpty");
        return new Str(""); 
    }
    
}
