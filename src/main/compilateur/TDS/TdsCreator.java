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

public class TdsCreator implements TdsVisitor<Void> {

    private ErrorAggregator errors = new ErrorAggregator();
    private TypeVisitor visitor = new TypeVisitor();

    public ErrorAggregator getErrors() {
        return this.errors;
    }

    public void setErrorAggregator(ErrorAggregator agg) {
        this.errors = agg;
        this.visitor.setErrorAggregator(agg);
    }

    @Override
    public Void visit(Fichier fichier, Tds tds) {
        tds.addnumRegion(0);
        if (fichier.instructions == null)
            return null;
        for (Ast ast : fichier.instructions) {
            ast.accept(this, tds);
        }
        return null;
    }

    @Override
    public Void visit(Idf idf, Tds tds) {
        return null;
        // Ne sera jamais visité
    }

    @Override
    public Void visit(DeclVarInt declVarInt, Tds tds) {
        for (Ast identifiants : declVarInt.idf) {
            String intName = ((Idf) identifiants).name;
            Symbole symbole = new SymboleInt(intName);
            symbole.addDefinitionLine(declVarInt.line);
            try {
                tds.addSymbole(intName, symbole);
            } catch (SymbolAlreadyExistsException e) {
                errors.addError(e);
            }
        }
        return null;
    }

    @Override
    public Void visit(DeclVarStruct declVarStruct, Tds tds) {

        ArrayList<Ast> liste = declVarStruct.idf;

        String structName = ((Idf) liste.get(0)).name;

        // Récupération de la structure
        SymboleStructContent struct;
        struct = tds.findSymboleStruct("struct_" + structName);

        if (struct == null) {
            errors.addError(new UndefinedStructureException(structName, declVarStruct.line));
        }

        liste.remove(0);
        for (Ast identifiants : liste) {
            String idfName = ((Idf) identifiants).name;
            Symbole symbole = new SymboleStruct(struct, idfName);
            symbole.addDefinitionLine(declVarStruct.line);
            try {
                tds.addSymbole(idfName, symbole);
            } catch (SymbolAlreadyExistsException e) {
                errors.addError(e);
            }
        }
        return null;
    }

    @Override
    public Void visit(Decl_typ decl_typ, Tds tds) {
        // Création d'une struct
        String idf = ((Idf) decl_typ.idf).name;

        SymboleStructContent symboleStruct = new SymboleStructContent(idf);
        symboleStruct.addDefinitionLine(decl_typ.line);

        Tds structTds = tds.nouvelleSousTDS("struct_" + idf);
        symboleStruct.setTds(structTds);

        try {
            tds.addSymbole("struct_" + idf, symboleStruct);
        } catch (SymbolAlreadyExistsException e) {
            errors.addError(e);
        }

        for (Ast ast : decl_typ.decl) {
            if (ast instanceof DeclVarInt || ast instanceof DeclVarStruct) {
                ast.accept(this, structTds);
            }
        }
        return null;
    }

    @Override
    public Void visit(DeclFctInt declFctInt, Tds tds) {
        String name = ((Idf) declFctInt.Idf).name;

        Tds tdsFunction = tds.nouvelleSousTDS("fn_" + name); // Création d'une nouvelle Tds
        SymboleFonction symboleFonction = new SymboleFonction(name, tdsFunction);
        symboleFonction.setReturnType("int");
        symboleFonction.addDefinitionLine(declFctInt.line);

        try {
            tds.addSymbole(name, symboleFonction);
        } catch (SymbolAlreadyExistsException e) {
            errors.addError(e);
        }

        if (declFctInt.param != null) {
            for (Ast ast : ((ParamListMulti) declFctInt.param).paramList) {
                if (ast instanceof ParamInt || ast instanceof ParamStruct) {
                    ast.accept(this, tdsFunction);
                }
            }
        }

        if (declFctInt.bloc != null) {
            declFctInt.bloc.accept(this, tdsFunction);
        }
        return null;
    }

    @Override
    public Void visit(DeclFctStruct declFctStruct, Tds tds) {
        String structName = ((Idf) declFctStruct.Idf0).name;
        String functionName = ((Idf) declFctStruct.Idf1).name;

        Tds tdsFunction = tds.nouvelleSousTDS("fn_" + functionName); // Création d'une nouvelle Tds
        SymboleFonction symboleFonction = new SymboleFonction(functionName, tdsFunction);
        symboleFonction.setReturnType("struct_" + structName);
        symboleFonction.addDefinitionLine(declFctStruct.line);

        try {
            tds.addSymbole(functionName, symboleFonction);
        } catch (SymbolAlreadyExistsException e) {
            errors.addError(e);
        }

        if (!(tds.findSymbole(structName) instanceof SymboleStructContent)) {
            // Si le type de struct n'existe pas
            errors.addError(new UndefinedStructureException(structName, declFctStruct.line));
        }

        if (declFctStruct.param != null) {
            for (Ast ast : ((ParamListMulti) declFctStruct.param).paramList) {
                if (ast instanceof ParamInt || ast instanceof ParamStruct) {
                    ast.accept(this, tdsFunction);
                }
            }
        }

        if (declFctStruct.bloc != null) {
            declFctStruct.bloc.accept(this, tdsFunction);
        }
        return null;
    }

    @Override
    public Void visit(ParamListMulti paramListMulti, Tds tds) {
        return null;
        // Ne sera jamais visité
    }

    @Override
    public Void visit(ParamInt paramInt, Tds tds) {
        String name = ((Idf) paramInt.name).name;
        SymboleInt symboleInt = new SymboleInt(name);
        symboleInt.addDefinitionLine(paramInt.line);

        try {
            tds.addSymboleParam(name, symboleInt);
        } catch (SymbolAlreadyExistsException e) {
            errors.addError(e);
        }
        return null;
    }

    @Override
    public Void visit(ParamStruct paramStruct, Tds tds) {
        String structName = ((Idf) paramStruct.idf0).name;

        // Récupération de la structure
        SymboleStructContent struct;
        struct = tds.findSymboleStruct("struct_" + structName);

        if (struct == null) {
            errors.addError(new UndefinedStructureException(structName, paramStruct.line));
        }

        String nameVar = ((Idf) paramStruct.idf1).name;

        SymboleStruct symboleStruct = new SymboleStruct(struct, nameVar);
        symboleStruct.addDefinitionLine(paramStruct.line);

        try {
            tds.addSymboleParam(nameVar, symboleStruct);
        } catch (SymbolAlreadyExistsException e) {
            errors.addError(e);
        }
        return null;
    }

    @Override
    public Void visit(Sizeof sizeof, Tds tds) {
        sizeof.name.accept(visitor, tds);
        return null;

    }

    @Override
    public Void visit(IdfParenthesis idfParenthesis, Tds tds) {
        idfParenthesis.accept(visitor, tds);
        return null;
    }

    @Override
    public Void visit(IdfParenthesisEmpty idfParenthesisEmpty, Tds tds) {
        idfParenthesisEmpty.accept(visitor, tds);
        return null;
    }

    @Override
    public Void visit(IfThen ifThen, Tds tds) {
        ifThen.condition.accept(visitor, tds);
        Tds newTds = tds.nouvelleSousTDS("thenblock");
        ifThen.thenBlock.accept(this, newTds);
        return null;
    }

    @Override
    public Void visit(IfThenElse ifThenElse, Tds tds) {
        ifThenElse.condition.accept(visitor, tds);
        Tds newTds = tds.nouvelleSousTDS("thenblock");
        Tds newTdsElse = tds.nouvelleSousTDS("elseblock");
        ifThenElse.thenBlock.accept(this, newTds);
        ifThenElse.elseBlock.accept(this, newTdsElse);

        return null;
    }

    @Override
    public Void visit(While while1, Tds tds) {
        Tds newTds = tds.nouvelleSousTDS("whileblock");
        while1.doBlock.accept(this, newTds);
        return null;
    }

    @Override
    public Void visit(Return return1, Tds tds) {
        Tds temp = tds;
        while (temp.getImbrication() != 1) {
            temp = temp.getPere();
        }

        // verification type de retour et donc de l'existance de la fonction
        for (Symbole sym : temp.getPere().getListeSymboles().values()) {
            if (sym instanceof SymboleFonction symfct && symfct.getTds().equals(temp)) {
                String typeRetour = return1.accept(visitor, tds);
                if (typeRetour == null) {
                    errors.addError(new UndefinedSymboleException(sym.getName(), return1.line));
                } else if (!symfct.getReturnType().equals(typeRetour)) {
                    errors.addError(new TypeException(return1.line, typeRetour, symfct.getReturnType()));
                }
                return null;
            }
        }
        return null;
    }

    @Override
    public Void visit(Bloc bloc, Tds tds) {
        if (bloc.instList == null)
            return null;

        for (Ast ast : bloc.instList) {
            if (ast instanceof Bloc) {
                Tds newTds = tds.nouvelleSousTDS("anonblock");
                ast.accept(this, newTds);
            } else {
                ast.accept(this, tds);
            }
        }
        return null;
    }

    @Override
    public Void visit(CharNode charNode, Tds tds) {
        return null;
    }

    @Override
    public Void visit(IntNode intNode, Tds tds) {
        return null;
    }

    @Override
    public Void visit(Affectation affectation, Tds tds) {
       affectation.accept(visitor, tds);
        return null;
    }

    @Override
    public Void visit(Expr_ou expr_ou, Tds tds) {
        expr_ou.accept(visitor, tds);
        return null;
    }

    @Override
    public Void visit(Expr_et expr_et, Tds tds) {
        expr_et.accept(visitor, tds);
        return null;
    }

    @Override
    public Void visit(Egal egal, Tds tds) {
        egal.accept(visitor, tds);
        return null;
    }

    @Override
    public Void visit(Different dif, Tds tds) {
        dif.accept(visitor, tds);
        return null;
    }

    @Override
    public Void visit(Inferieur inf, Tds tds) {
        inf.accept(visitor, tds);
        return null;
    }

    @Override
    public Void visit(InferieurEgal infEgal, Tds tds) {
        infEgal.accept(visitor, tds);
        return null;
    }

    @Override
    public Void visit(Superieur sup, Tds tds) {
        sup.accept(visitor, tds);
        return null;
    }

    @Override
    public Void visit(SuperieurEgal supEgal, Tds tds) {
       supEgal.accept(visitor, tds);
        return null;
    }

    @Override
    public Void visit(Plus plus, Tds tds) {
        plus.accept(visitor, tds);
        return null;
    }

    @Override
    public Void visit(Minus minus, Tds tds) {
        minus.accept(visitor, tds);
        return null;
    }

    @Override
    public Void visit(Division div, Tds tds) {
        div.accept(visitor, tds);
        return null;
    }

    @Override
    public Void visit(Multiplication mult, Tds tds) {
        mult.accept(visitor, tds);
        return null;
    }

    @Override
    public Void visit(Fleche fleche, Tds tds) {
        fleche.accept(visitor, tds);
        return null;
    }

    @Override
    public Void visit(MoinsUnaire unaire, Tds tds) {
        unaire.accept(visitor, tds);
        return null;
    }

    @Override
    public Void visit(Negation unaire, Tds tds) {
        unaire.accept(visitor, tds);
        return null;
    }

    @Override
    public Void visit(Semicolon semicolon, Tds tds) {
        return null;
    }

}
