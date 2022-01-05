package compilateur.TDS;


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
import compilateur.utils.ErrorAggregator;

public class TdsCreator implements TdsVisitor{

    private ErrorAggregator errors = new ErrorAggregator();


    @Override public void visit(Fichier fichier, Tds tds){

        tds.addnumRegion(0);

        if (fichier.instructions != null) {
            for (Ast ast:fichier.instructions){
                ast.accept(this,tds);
            }
        }
    }

    @Override public void visit(Idf idf, Tds tds){
        // Ne sera jamais visité
    }

    @Override public void visit(DeclVarInt declVarInt, Tds tds){
        for(Ast identifiants : declVarInt.idf){
            String intName = ((Idf) identifiants).name;
            Symbole symbole = new SymboleInt(intName);
            symbole.addDefinitionLine(declVarInt.line);
            try {
                tds.addSymbole(intName, symbole);
            } catch (SymbolAlreadyExistsException e) {
                errors.addError(e);
            }
        }
    }

    @Override public void visit(DeclVarStruct declVarStruct, Tds tds){

        ArrayList<Ast> liste =  declVarStruct.idf;

        String structName = ((Idf)liste.get(0)).name;
    
        // Récupération de la structure
        SymboleStructContent struct;
        struct = tds.findSymboleStruct("struct_"+structName);

        if(struct == null) {
            errors.addError(new UndefinedStructureException(structName, declVarStruct.line));
        }
       
        tds.findSymbole(structName);

        liste.remove(0);      
        for(Ast identifiants :liste){
            String idfName = ((Idf) identifiants).name;
            Symbole symbole = new SymboleStruct(struct,idfName);
            symbole.addDefinitionLine(declVarStruct.line);
            try {
                tds.addSymbole(idfName, symbole);
            } catch (SymbolAlreadyExistsException e) {
                errors.addError(e);
            }
        }
    }

    @Override public void visit(Decl_typ decl_typ, Tds tds){
        // Création d'une struct
        String idf = ((Idf)decl_typ.idf).name;

        SymboleStructContent symboleStruct = new SymboleStructContent(idf);
        symboleStruct.addDefinitionLine(decl_typ.line);

        Tds structTds = symboleStruct.getTds();

        for (Ast ast:decl_typ.decl){
            if(ast instanceof DeclVarInt || ast instanceof DeclVarStruct) {
                ast.accept(this, structTds);
            } else {
                // TODO : erreur sémantique, la struct contient une instruction incorrecte
            }
        }
    }



    @Override public void visit(DeclFctInt declFctInt, Tds tds){
        String name = ((Idf)declFctInt.Idf).name;

        Tds tdsFunction = new Tds(tds); // Création d'une nouvelle Tds
        SymboleFonction symboleFonction = new SymboleFonction(name, tdsFunction);
        symboleFonction.setReturnType("int");
        symboleFonction.addDefinitionLine(declFctInt.line);
        
        try {
            tds.addSymbole(name, symboleFonction);
        } catch (SymbolAlreadyExistsException e) {
            errors.addError(e);
        }

        if (declFctInt.param != null) {
            for (Ast ast: ((ParamListMulti)declFctInt.param).paramList){    
                if(ast instanceof ParamInt){
                    ast.accept(this, tdsFunction); 
                } else if(ast instanceof ParamStruct){
                    ast.accept(this, tdsFunction);
                } else{
                    throw new Error("Erreur de remontée des symboles dans visit(DeclFctInt...), symbole doit être SymboleInt ou SymboleStruct)");
                }
            }
        }

        if (declFctInt.bloc != null){
            declFctInt.bloc.accept(this,tdsFunction);
        }
    }


    @Override public void visit(DeclFctStruct declFctStruct, Tds tds){
        String structName = ((Idf)declFctStruct.Idf0).name;
        String functionName = ((Idf)declFctStruct.Idf1).name;

        Tds tdsFunction = new Tds(tds); // Création d'une nouvelle Tds
        SymboleFonction symboleFonction = new SymboleFonction(functionName, tdsFunction);
        symboleFonction.addDefinitionLine(declFctStruct.line);

        if(!(tds.findSymbole(structName) instanceof SymboleStructContent)) {
            // Si le type de struct n'existe pas
            errors.addError(new UndefinedStructureException(structName, declFctStruct.line));
        }

        if (declFctStruct.param != null) {
            for (Ast ast: ((ParamListMulti)declFctStruct.param).paramList){    
                if(ast instanceof ParamInt){
                    ast.accept(this, tdsFunction); 
                } else if(ast instanceof ParamStruct){
                    ast.accept(this, tdsFunction);
                } else{
                    throw new Error("Erreur de remontée des symboles dans visit(declFctStruct...), symbole doit être SymboleInt ou SymboleStruct)");
                }
            }
        }

        if (declFctStruct.bloc != null){
            declFctStruct.bloc.accept(this,tdsFunction);
        }
    }


    @Override public void visit(ParamListMulti paramListMulti, Tds tds){
        // Ne sera jamais visité
    }

    @Override public void visit(ParamInt paramInt, Tds tds){
        String name = ((Idf)paramInt.name).name;
        SymboleInt symboleInt = new SymboleInt(name);   
        symboleInt.addDefinitionLine(paramInt.line);

        try {
            tds.addSymboleParam(name, symboleInt);
        } catch (SymbolAlreadyExistsException e) {
            errors.addError(e);
        }
    }

    @Override public void visit(ParamStruct paramStruct, Tds tds){
        String structName = ((Idf)paramStruct.idf0).name;
    
        // Récupération de la structure
        SymboleStructContent struct;
        struct = tds.findSymboleStruct("struct_"+structName);

        if(struct == null) {
            errors.addError(new UndefinedStructureException(structName, paramStruct.line));
        }

        String nameVar = ((Idf)paramStruct.idf1).name;
        
        SymboleStruct symboleStruct = new SymboleStruct(struct, nameVar);
        symboleStruct.addDefinitionLine(paramStruct.line);

        try {
            tds.addSymboleParam(nameVar, symboleStruct);
        } catch (SymbolAlreadyExistsException e) {
            errors.addError(e);
        }
    }


    @Override public void visit(Sizeof sizeof, Tds tds){
        // Symbole nodeIdentifier = this.nextState();
        // this.addNode(nodeIdentifier, "Sizeof");
        // this.addTransition(nodeIdentifier, sizeof.name.accept(this));
    }


    @Override public void visit(IdfParenthesis idfParenthesis, Tds tds){
        // Symbole nodeIdentifier = this.nextState();
        // this.addTransition(nodeIdentifier, idfParenthesis.idf.accept(this));
        // for (Ast ast:idfParenthesis.exprList, Tds tds){
        //     Symbole astState = ast.accept(this);
        //     this.addTransition(nodeIdentifier, astState);
        // }
    }


    @Override public void visit(IdfParenthesisEmpty idfParenthesisEmpty, Tds tds){
        // Symbole nodeIdentifier = this.nextState();
        // this.addTransition(nodeIdentifier, idfParenthesisEmpty.idf.accept(this));
    }


    @Override public void visit(IfThen ifThen, Tds tds){
        Tds newTds = new Tds(tds);
        SymboleBlocAnonyme bloc = new SymboleBlocAnonyme(newTds);
        bloc.addDefinitionLine(ifThen.line);
        try {
            tds.addSymbole("ifThen", bloc); // il n'y aura qu'au plus un symbole nommé ifThen dans la tds
        } catch (SymbolAlreadyExistsException e) {
            errors.addError(e);
        } 
        ifThen.thenBlock.accept(this, newTds);
    }


    @Override public void visit(IfThenElse ifThenElse, Tds tds){

        Tds newTds = new Tds(tds);
        Tds newTdsElse = new Tds(tds);
        SymboleBlocAnonyme blocThen = new SymboleBlocAnonyme(newTds);
        SymboleBlocAnonyme blocElse = new SymboleBlocAnonyme(newTdsElse);
        blocThen.addDefinitionLine(ifThenElse.lineIf);
        blocElse.addDefinitionLine(ifThenElse.lineElse);
        try {
            tds.addSymbole("ifThen", blocThen); // il n'y aura qu'au plus un symbole nommé ifThenElse dans la tds
            tds.addSymbole("ifElse", blocElse); // il n'y aura qu'au plus un symbole nommé ifElse dans la tds
        } catch (SymbolAlreadyExistsException e) {
            errors.addError(e);
        } 
        
        ifThenElse.thenBlock.accept(this, newTds);
        ifThenElse.elseBlock.accept(this, newTdsElse);
    }

    @Override public void visit(While while1, Tds tds){
        Tds newTds = new Tds(tds);
        SymboleBlocAnonyme bloc = new SymboleBlocAnonyme(newTds);
        bloc.addDefinitionLine(while1.line);
        try {
            tds.addSymbole("While", bloc); // il n'y aura qu'au plus un symbole nommé While dans la tds
        } catch (SymbolAlreadyExistsException e) {
            errors.addError(e);
        } 
        
        while1.doBlock.accept(this, newTds);
    }


    @Override public void visit(Return return1, Tds tds){
        // Symbole nodeIdentifier = this.nextState();
        // this.addNode(nodeIdentifier, "Return");
        // this.addTransition(nodeIdentifier, return1.expr.accept(this));
    }

    @Override public void visit(Bloc bloc, Tds tds){
        int i = 0;
        ArrayList<Ast> listeAst =  bloc.instList ; 
        int longueurListe = listeAst.size();
        Ast ast = listeAst.get(i);
        
        while( i < longueurListe && ((ast instanceof DeclVarInt) || (ast instanceof DeclVarStruct))){ // Tant qu'objet de types decl_vars
            ast.accept(this, tds);
            ast = listeAst.get(i++);
        }

        // types instructions

        while(i < longueurListe){
            Ast astInstruction = listeAst.get(i);
            i++;
            // On ne va modifier la TDS que pour les instructions générants un nouveau bloc
            if(astInstruction instanceof IfThen){
                astInstruction.accept(this, tds);
            } else if (astInstruction instanceof IfThenElse){
                astInstruction.accept(this, tds);
            } else if (astInstruction instanceof While){
                astInstruction.accept(this, tds);
            } else if (astInstruction instanceof Bloc){
                Tds newTds = new Tds(tds);
                astInstruction.accept(this, newTds);
                SymboleBlocAnonyme symbole = new SymboleBlocAnonyme(newTds);
                try {
                    tds.addSymbole("Bloc", symbole); // il n'y aura qu'au plus un symbole nommé bloc dans la tds
                } catch (SymbolAlreadyExistsException e) {
                    errors.addError(e);
                }
            }

        }
    }

    @Override public void visit(CharNode charNode, Tds tds){

    }

    @Override public void visit(IntNode intNode, Tds tds){

    }

    @Override public void visit(Affectation affectation, Tds tds){

    }

    @Override public void visit(Expr_ou expr_ou, Tds tds){

    }

    @Override public void visit(Expr_et expr_et, Tds tds){

    }

    @Override public void visit(Egal egal, Tds tds){

    }

    @Override public void visit(Different dif, Tds tds){

    }

    @Override public void visit(Inferieur inf, Tds tds){

    }

    @Override public void visit (InferieurEgal infEgal, Tds tds){

    }

    @Override public void visit (Superieur sup, Tds tds){

    }

    @Override public void visit (SuperieurEgal supEgal, Tds tds){

    }

    @Override public void visit (Plus plus, Tds tds){
 
    }

    @Override public void visit (Minus minus, Tds tds){

    }

    @Override public void visit (Division div, Tds tds){

    }

    @Override public void visit (Multiplication mult, Tds tds){

    }

    @Override public void visit (Fleche fleche, Tds tds){

    }

    @Override public void visit (MoinsUnaire unaire, Tds tds){

    }
    
    @Override public void visit (Negation unaire, Tds tds){

    }

    @Override
    public void visit(Semicolon semicolon, Tds tds) {

    }
    
}
