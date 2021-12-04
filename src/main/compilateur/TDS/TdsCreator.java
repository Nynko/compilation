package compilateur.TDS;


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
import compilateur.ast.Semicolon;
import compilateur.ast.Sizeof;
import compilateur.ast.Superieur;
import compilateur.ast.SuperieurEgal;
import compilateur.ast.MoinsUnaire;
import compilateur.ast.Negation;
import compilateur.ast.While;

public class TdsVisitor implements AstVisitor<Tds>{

    @Override public Tds visit(Fichier fichier){
        Tds tds = new TableSymbole();

        if (fichier.instructions != null) {

            for (Ast ast:fichier.instructions){
                Tds instructionsState = ast.accept(this);
                if(instructionsState!= null){
                    instructionsState.linkPere(tds);
                }
            }
        }

        return tds;
    }

    @Override public Tds visit(Idf idf){
        return new ValueString(idf.name);
    }
    @Override public Tds visit(DeclVarInt declVarInt){
        
        declVarInt.

      
        // Tds nodeIdentifier = this.nextState();
        // this.addNode(nodeIdentifier, "DeclVarInt");
        // for (Ast ast:declVarInt.idf){

        //     Tds astState = ast.accept(this);
        //     this.addTransition(nodeIdentifier, astState);

        // }

        //         return nodeIdentifier;
    }
    @Override public Tds visit(DeclVarStruct declVarStruct){
        // Tds nodeIdentifier = this.nextState();
        // this.addNode(nodeIdentifier, "DeclVarStruct");
        
        // for (Ast ast:declVarStruct.idf){

        //     Tds astState = ast.accept(this);
        //     this.addTransition(nodeIdentifier, astState);

        // }

        // return nodeIdentifier;
    }
    @Override public Tds visit(Decl_typ decl_typ){
        // Tds nodeIdentifier = this.nextState();
        // this.addNode(nodeIdentifier, "DeclTyp");
        // Tds idf = decl_typ.idf.accept(this);
        // this.addTransition(nodeIdentifier, idf);
        // for (Ast ast:decl_typ.decl){
        //     Tds astState = ast.accept(this);
        //     this.addTransition(nodeIdentifier, astState);
        // }

        // return nodeIdentifier;
    }
    @Override public Tds visit(DeclFctInt declFctInt){
        // Tds nodeIdentifier = this.nextState();
        // this.addNode(nodeIdentifier, "DeclFctInt");
        // this.addTransition(nodeIdentifier, declFctInt.Idf.accept(this));
        // if (declFctInt.param != null) {
        //     this.addTransition(nodeIdentifier, declFctInt.param.accept(this));
        // }
        // this.addTransition(nodeIdentifier, declFctInt.bloc.accept(this));
        // return nodeIdentifier;
    }
    @Override public Tds visit(DeclFctStruct declFctStruct){
        // Tds nodeIdentifier = this.nextState();
        // this.addNode(nodeIdentifier, "DeclFctStruct");
        // this.addTransition(nodeIdentifier, declFctStruct.Idf0.accept(this));
        // this.addTransition(nodeIdentifier, declFctStruct.Idf1.accept(this));
        // this.addTransition(nodeIdentifier, declFctStruct.param.accept(this));
        // this.addTransition(nodeIdentifier, declFctStruct.bloc.accept(this));
        // return nodeIdentifier;
    }
    @Override public Tds visit(ParamListMulti paramListMulti){
        // Tds nodeIdentifier = this.nextState();
        // this.addNode(nodeIdentifier, "ParamListMulti");

        // for (Ast ast:paramListMulti.paramList){
        //     Tds astState = ast.accept(this);
        //     this.addTransition(nodeIdentifier, astState);
        // }
        
        // return nodeIdentifier;
    }
    @Override public Tds visit(ParamInt paramInt){
        // Tds nodeIdentifier = this.nextState();
        // this.addNode(nodeIdentifier, "ParamInt");
        // this.addTransition(nodeIdentifier, paramInt.name.accept(this));
        // return nodeIdentifier;
    }
    @Override public Tds visit(ParamStruct paramStruct){
        // Tds nodeIdentifier = this.nextState();
        // this.addNode(nodeIdentifier, "ParamStruct");
        // this.addTransition(nodeIdentifier, paramStruct.idf0.accept(this));
        // this.addTransition(nodeIdentifier, paramStruct.idf1.accept(this));
        // return nodeIdentifier;
    }
    @Override public Tds visit(Sizeof sizeof){
        // Tds nodeIdentifier = this.nextState();
        // this.addNode(nodeIdentifier, "Sizeof");
        // this.addTransition(nodeIdentifier, sizeof.name.accept(this));
        // return nodeIdentifier;
    }
    @Override public Tds visit(IdfParenthesis idfParenthesis){
        // Tds nodeIdentifier = this.nextState();
        // this.addTransition(nodeIdentifier, idfParenthesis.idf.accept(this));
        // for (Ast ast:idfParenthesis.exprList){
        //     Tds astState = ast.accept(this);
        //     this.addTransition(nodeIdentifier, astState);
        // }
        // return nodeIdentifier;
    }
    @Override public Tds visit(IdfParenthesisEmpty idfParenthesisEmpty){
        // Tds nodeIdentifier = this.nextState();
        // this.addTransition(nodeIdentifier, idfParenthesisEmpty.idf.accept(this));
        // return nodeIdentifier;
    }
    @Override public Tds visit(IfThen ifThen){
        // Tds nodeIdentifier = this.nextState();

        // Tds conditionState = ifThen.condition.accept(this);
        // Tds thenBlockState = ifThen.thenBlock.accept(this);

        // this.addNode(nodeIdentifier, "IfThen");

        // this.addTransition(nodeIdentifier, conditionState);
        // this.addTransition(nodeIdentifier, thenBlockState);

        // return nodeIdentifier;
    }
    @Override public Tds visit(IfThenElse ifThenElse){
        // Tds nodeIdentifier = this.nextState();

        // Tds conditionState = ifThenElse.condition.accept(this);
        // Tds thenBlockState = ifThenElse.thenBlock.accept(this);
        // Tds elseBlockState = ifThenElse.elseBlock.accept(this);
        
        // this.addNode(nodeIdentifier, "IfThenElse");

        // this.addTransition(nodeIdentifier, conditionState);
        // this.addTransition(nodeIdentifier, thenBlockState);
        // this.addTransition(nodeIdentifier, elseBlockState);

        // return nodeIdentifier;
    }
    @Override public Tds visit(While while1){
        // Tds nodeIdentifier = this.nextState();
        // this.addNode(nodeIdentifier, "While");
        // this.addTransition(nodeIdentifier, while1.condition.accept(this));
        // this.addTransition(nodeIdentifier, while1.doBlock.accept(this));
        // return nodeIdentifier;
    }
    @Override public Tds visit(Return return1){
        // Tds nodeIdentifier = this.nextState();
        // this.addNode(nodeIdentifier, "Return");
        // this.addTransition(nodeIdentifier, return1.expr.accept(this));
        // return nodeIdentifier;
    }
    @Override public Tds visit(Bloc bloc){
        // Tds nodeIdentifier = this.nextState();
        // this.addNode(nodeIdentifier, "Bloc");
        
        // for (Ast ast:bloc.instList){
        //     Tds astState = ast.accept(this);
        //     this.addTransition(nodeIdentifier, astState);
        // }

        // return nodeIdentifier;
    }
    @Override public Tds visit(CharNode charNode){
        // Tds nodeIdentifier = this.nextState();
        // this.addNode(nodeIdentifier, charNode.Tds);
        
        // return nodeIdentifier;
    }
    @Override public Tds visit(IntNode intNode){
        Tds nodeIdentifier = this.nextState();

        // this.addNode(nodeIdentifier, Tds.valueOf(intNode.parseInt));

        // return nodeIdentifier;

    }
    @Override public Tds visit(Affectation affectation){
        // Tds nodeIdentifier = this.nextState();

        // Tds idfState = affectation.left.accept(this);
        // Tds expressionState = affectation.right.accept(this);

        // this.addNode(nodeIdentifier, "Affectation");
        // this.addTransition(nodeIdentifier, idfState);
        // this.addTransition(nodeIdentifier, expressionState);

        // return nodeIdentifier;
    }
    @Override public Tds visit(Expr_ou expr_ou){
        // Tds nodeIdentifier = this.nextState();
        // this.addNode(nodeIdentifier, "||");
        // this.addTransition(nodeIdentifier, expr_ou.left.accept(this));
        // this.addTransition(nodeIdentifier, expr_ou.right.accept(this));
        // return nodeIdentifier;
    }
    @Override public Tds visit(Expr_et expr_et){
        // Tds nodeIdentifier = this.nextState();
        // this.addNode(nodeIdentifier, "&&");
        // this.addTransition(nodeIdentifier, expr_et.left.accept(this));
        // this.addTransition(nodeIdentifier, expr_et.right.accept(this));
        // return nodeIdentifier;
    }
    @Override public Tds visit(Egal egal){
        // Tds nodeIdentifier = this.nextState();
        // this.addNode(nodeIdentifier, "==");
        // this.addTransition(nodeIdentifier, egal.left.accept(this));
        // this.addTransition(nodeIdentifier, egal.right.accept(this));
        // return nodeIdentifier;
    }
    @Override public Tds visit(Different dif){
        // Tds nodeIdentifier = this.nextState();
        // this.addNode(nodeIdentifier, "!=");
        // this.addTransition(nodeIdentifier, dif.left.accept(this));
        // this.addTransition(nodeIdentifier, dif.right.accept(this));
        // return nodeIdentifier;
    }
    @Override public Tds visit(Inferieur inf){
        // Tds nodeIdentifier = this.nextState();
        // this.addNode(nodeIdentifier, "<");
        // this.addTransition(nodeIdentifier, inf.left.accept(this));
        // this.addTransition(nodeIdentifier, inf.right.accept(this));
        // return nodeIdentifier;
    }
    @Override public Tds visit (InferieurEgal infEgal){
        // Tds nodeIdentifier = this.nextState();
        // this.addNode(nodeIdentifier, "<=");
        // this.addTransition(nodeIdentifier, infEgal.left.accept(this));
        // this.addTransition(nodeIdentifier, infEgal.right.accept(this));
        // return nodeIdentifier;
    }
    @Override public Tds visit (Superieur sup){
        // Tds nodeIdentifier = this.nextState();
        // this.addNode(nodeIdentifier, ">");
        // this.addTransition(nodeIdentifier, sup.left.accept(this));
        // this.addTransition(nodeIdentifier, sup.right.accept(this));
        // return nodeIdentifier;
    }
    @Override public Tds visit (SuperieurEgal supEgal){
        // Tds nodeIdentifier = this.nextState();
        // this.addNode(nodeIdentifier, ">=");
        // this.addTransition(nodeIdentifier, supEgal.left.accept(this));
        // this.addTransition(nodeIdentifier, supEgal.right.accept(this));
        // return nodeIdentifier;
    }
    @Override public Tds visit (Plus plus){
        // Tds nodeIdentifier = this.nextState();

        // Tds leftState = plus.left.accept(this);
        // Tds rightState = plus.right.accept(this);

        // this.addNode(nodeIdentifier, "+");
        
        // this.addTransition(nodeIdentifier, leftState);
        // this.addTransition(nodeIdentifier, rightState);

        // return nodeIdentifier;
    }
    @Override public Tds visit (Minus minus){
        // Tds nodeIdentifier = this.nextState();

        // Tds leftState = minus.left.accept(this);
        // Tds rightState = minus.right.accept(this);

        // this.addNode(nodeIdentifier, "-");
        
        // this.addTransition(nodeIdentifier, leftState);
        // this.addTransition(nodeIdentifier, rightState);

        // return nodeIdentifier;
    }
    @Override public Tds visit (Division div){
        // Tds nodeIdentifier = this.nextState();

        // Tds leftState = div.left.accept(this);
        // Tds rightState = div.right.accept(this);

        // this.addNode(nodeIdentifier, "/");
        
        // this.addTransition(nodeIdentifier, leftState);
        // this.addTransition(nodeIdentifier, rightState);

        // return nodeIdentifier;
    }
    @Override public Tds visit (Multiplication mult){
        // Tds nodeIdentifier = this.nextState();

        // Tds leftState = mult.left.accept(this);
        // Tds rightState = mult.right.accept(this);

        // this.addNode(nodeIdentifier, "*");
        
        // this.addTransition(nodeIdentifier, leftState);
        // this.addTransition(nodeIdentifier, rightState);

        // return nodeIdentifier;
    }
    @Override public Tds visit (Fleche fleche){
        // Tds nodeIdentifier = this.nextState();
        // this.addNode(nodeIdentifier, "->");
        // this.addTransition(nodeIdentifier, fleche.left.accept(this));
        // this.addTransition(nodeIdentifier, fleche.right.accept(this));
        // return nodeIdentifier;
    }
    @Override public Tds visit (MoinsUnaire unaire){
        // Tds nodeIdentifier = this.nextState();
        // this.addNode(nodeIdentifier, "-");
        // this.addTransition(nodeIdentifier, unaire.noeud.accept(this));
        // return nodeIdentifier;
    }
    @Override public Tds visit (Negation unaire){
        // Tds nodeIdentifier = this.nextState();
        // this.addNode(nodeIdentifier, "!");
        // this.addTransition(nodeIdentifier, unaire.noeud.accept(this));
        // return nodeIdentifier;
    }

    @Override
    public Tds visit(Semicolon semicolon) {
        // Tds nodeIdentifier = this.nextState();
        // this.addNode(nodeIdentifier, "InstEmpty");
        // return nodeIdentifier;
    }
    
}
