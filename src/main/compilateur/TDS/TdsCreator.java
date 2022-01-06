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

    public ErrorAggregator getErrors() {
        return this.errors;
    }

    @Override public void visit(Fichier fichier, Tds tds){
        tds.addnumRegion(0);
        if (fichier.instructions == null) return;
        for (Ast ast:fichier.instructions){
            ast.accept(this,tds);
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

        Tds structTds = tds.nouvelleSousTDS("struct_"+idf);
        symboleStruct.setTds(structTds);

        try {
            tds.addSymbole("struct_"+idf, symboleStruct);
        } catch (SymbolAlreadyExistsException e) {
            errors.addError(e);
        }

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

        Tds tdsFunction = tds.nouvelleSousTDS("fn_"+name); // Création d'une nouvelle Tds
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

        Tds tdsFunction = tds.nouvelleSousTDS("fn_"+functionName); // Création d'une nouvelle Tds
        SymboleFonction symboleFonction = new SymboleFonction(functionName, tdsFunction);
        symboleFonction.setReturnType("struct_"+structName);
        symboleFonction.addDefinitionLine(declFctStruct.line);

        if(!(tds.findSymbole(structName) instanceof SymboleStructContent)) {
            // Si le type de struct n'existe pas
            errors.addError(new UndefinedStructureException(structName, declFctStruct.line));
        }

        if (declFctStruct.param != null) {
            for (Ast ast: ((ParamListMulti)declFctStruct.param).paramList){    
                if(ast instanceof ParamInt || ast instanceof ParamStruct){
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

    }


    @Override public void visit(IdfParenthesis idfParenthesis, Tds tds){

    }


    @Override public void visit(IdfParenthesisEmpty idfParenthesisEmpty, Tds tds){
       
    }


    @Override public void visit(IfThen ifThen, Tds tds){
        Tds newTds = tds.nouvelleSousTDS("thenblock");
        ifThen.thenBlock.accept(this, newTds);
    }


    @Override public void visit(IfThenElse ifThenElse, Tds tds){
        Tds newTds = tds.nouvelleSousTDS("thenblock");
        Tds newTdsElse = tds.nouvelleSousTDS("elseblock");
        ifThenElse.thenBlock.accept(this, newTds);
        ifThenElse.elseBlock.accept(this, newTdsElse);
    }

    @Override public void visit(While while1, Tds tds){
        Tds newTds = tds.nouvelleSousTDS("whileblock");
        while1.doBlock.accept(this, newTds);
    }


    @Override public void visit(Return return1, Tds tds){

    }

    @Override public void visit(Bloc bloc, Tds tds){
        if (bloc.instList == null) return;

        for (Ast ast: bloc.instList){    
            if(ast instanceof DeclVarInt || ast instanceof DeclVarStruct || ast instanceof IfThen || ast instanceof IfThenElse || ast instanceof While){
                ast.accept(this, tds); 
            } else if(ast instanceof Bloc){
                Tds newTds = tds.nouvelleSousTDS("anonblock");
                ast.accept(this, newTds);
            }
        }
    }

    @Override public void visit(CharNode charNode, Tds tds){

    }

    @Override public void visit(IntNode intNode, Tds tds){

    }

    @Override public void visit(Affectation affectation, Tds tds){
        if (tds.findSymbole(affectation.left.toString()) == null) {
            errors.addError(new UndefinedSymboleException(affectation.left.toString(), affectation.line));
        }
    }

    @Override public void visit(Expr_ou expr_ou, Tds tds){
        if (tds.findSymbole(expr_ou.left.toString()) == null) {
            errors.addError(new UndefinedSymboleException(expr_ou.left.toString(), expr_ou.line));
        }
    }

    @Override public void visit(Expr_et expr_et, Tds tds){
        if (tds.findSymbole(expr_et.left.toString()) == null) {
            errors.addError(new UndefinedSymboleException(expr_et.left.toString(), expr_et.line));
        }
    }

    @Override public void visit(Egal egal, Tds tds){
        if (tds.findSymbole(egal.left.toString()) == null) {
            errors.addError(new UndefinedSymboleException(egal.left.toString(), egal.line));
        }
    }

    @Override public void visit(Different dif, Tds tds){
        if (tds.findSymbole(dif.left.toString()) == null) {
            errors.addError(new UndefinedSymboleException(dif.left.toString(), dif.line));
        }
    }

    @Override public void visit(Inferieur inf, Tds tds){
        if (tds.findSymbole(inf.left.toString()) == null) {
            errors.addError(new UndefinedSymboleException(inf.left.toString(), inf.line));
        }
    }

    @Override public void visit (InferieurEgal infEgal, Tds tds){
        if (tds.findSymbole(infEgal.left.toString()) == null) {
            errors.addError(new UndefinedSymboleException(infEgal.left.toString(), infEgal.line));
        }
    }

    @Override public void visit (Superieur sup, Tds tds){
        if (tds.findSymbole(sup.left.toString()) == null) {
            errors.addError(new UndefinedSymboleException(sup.left.toString(), sup.line));
        }
    }

    @Override public void visit (SuperieurEgal supEgal, Tds tds){
        if (tds.findSymbole(supEgal.left.toString()) == null) {
            errors.addError(new UndefinedSymboleException(supEgal.left.toString(), supEgal.line));
        }
    }

    @Override public void visit (Plus plus, Tds tds){
        if (tds.findSymbole(plus.left.toString()) == null) {
            errors.addError(new UndefinedSymboleException(plus.left.toString(), plus.line));
        }
    }

    @Override public void visit (Minus minus, Tds tds){
        if (tds.findSymbole(minus.left.toString()) == null) {
            errors.addError(new UndefinedSymboleException(minus.left.toString(), minus.line));
        }
    }

    @Override public void visit (Division div, Tds tds){
        if (tds.findSymbole(div.left.toString()) == null) {
            errors.addError(new UndefinedSymboleException(div.left.toString(), div.line));
        }
    }

    @Override public void visit (Multiplication mult, Tds tds){
        if (tds.findSymbole(mult.left.toString()) == null) {
            errors.addError(new UndefinedSymboleException(mult.left.toString(), mult.line));
        }
    }

    @Override public void visit (Fleche fleche, Tds tds){
        if (tds.findSymbole(fleche.left.toString()) == null) {
            errors.addError(new UndefinedSymboleException(fleche.left.toString(), fleche.line));
        }
    }

    @Override public void visit (MoinsUnaire unaire, Tds tds){
        if (tds.findSymbole(unaire.noeud.toString()) == null) {
            errors.addError(new UndefinedSymboleException(unaire.noeud.toString(), unaire.line));
        }
    }
    
    @Override public void visit (Negation unaire, Tds tds){
        if (tds.findSymbole(unaire.noeud.toString()) == null) {
            errors.addError(new UndefinedSymboleException(unaire.noeud.toString(), unaire.line));
        }
    }

    @Override
    public void visit(Semicolon semicolon, Tds tds) {

    }
    
}
