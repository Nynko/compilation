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
        Tds newTds = tds.nouvelleSousTDS("thenblock");
        ifThen.thenBlock.accept(this, newTds);
        return null;
    }

    @Override
    public Void visit(IfThenElse ifThenElse, Tds tds) {
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
        String leftType = affectation.left.accept(visitor, tds);
        String rightType = affectation.right.accept(visitor, tds);
        if (leftType == null) {
            errors.addError(new UndefinedSymboleException(affectation.left.toString(), affectation.line)); 
            //TODO corriger les erreurs moches
            return null;
        }
        if (!leftType.equals(rightType)) {
            errors.addError(new TypeException(affectation.line, rightType, leftType));
        }
        return null;
    }

    @Override
    public Void visit(Expr_ou expr_ou, Tds tds) {
        Idf idfLeft = (Idf) expr_ou.left;
        if (tds.findSymbole(idfLeft.name) == null) {
            errors.addError(new UndefinedSymboleException(expr_ou.left.toString(), expr_ou.line));
            return null;
        }
        String leftType = expr_ou.left.accept(visitor, tds);
        String rightType = expr_ou.right.accept(visitor, tds);
        if (!leftType.equals(rightType)) {
            errors.addError(new TypeException(expr_ou.line, rightType, leftType));
        }
        return null;
    }

    @Override
    public Void visit(Expr_et expr_et, Tds tds) {
        Idf idfLeft = (Idf) expr_et.left;
        if (tds.findSymbole(idfLeft.name) == null) {
            errors.addError(new UndefinedSymboleException(expr_et.left.toString(), expr_et.line));
            return null;
        }
        String leftType = expr_et.left.accept(visitor, tds);
        String rightType = expr_et.right.accept(visitor, tds);
        if (!leftType.equals(rightType)) {
            errors.addError(new TypeException(expr_et.line, rightType, leftType));
        }
        return null;
    }

    @Override
    public Void visit(Egal egal, Tds tds) {
        Idf idfLeft = (Idf) egal.left;
        if (tds.findSymbole(idfLeft.name) == null) {
            errors.addError(new UndefinedSymboleException(egal.left.toString(), egal.line));
            return null;
        }
        String leftType = egal.left.accept(visitor, tds);
        String rightType = egal.right.accept(visitor, tds);
        if (!leftType.equals(rightType)) {
            errors.addError(new TypeException(egal.line, rightType, leftType));
        }
        return null;
    }

    @Override
    public Void visit(Different dif, Tds tds) {
        Idf idfLeft = (Idf) dif.left;
        if (tds.findSymbole(idfLeft.name) == null) {
            errors.addError(new UndefinedSymboleException(dif.left.toString(), dif.line));
            return null;
        }
        String leftType = dif.left.accept(visitor, tds);
        String rightType = dif.right.accept(visitor, tds);
        if (!leftType.equals(rightType)) {
            errors.addError(new TypeException(dif.line, rightType, leftType));
        }
        return null;
    }

    @Override
    public Void visit(Inferieur inf, Tds tds) {
        Idf idfLeft = (Idf) inf.left;
        if (tds.findSymbole(idfLeft.name) == null) {
            errors.addError(new UndefinedSymboleException(inf.left.toString(), inf.line));
            return null;
        }
        String leftType = inf.left.accept(visitor, tds);
        String rightType = inf.right.accept(visitor, tds);
        if (!leftType.equals(rightType)) {
            errors.addError(new TypeException(inf.line, rightType, leftType));
        }
        return null;
    }

    @Override
    public Void visit(InferieurEgal infEgal, Tds tds) {
        Idf idfLeft = (Idf) infEgal.left;
        if (tds.findSymbole(idfLeft.name) == null) {
            errors.addError(new UndefinedSymboleException(infEgal.left.toString(), infEgal.line));
            return null;
        }
        String leftType = infEgal.left.accept(visitor, tds);
        String rightType = infEgal.right.accept(visitor, tds);
        if (!leftType.equals(rightType)) {
            errors.addError(new TypeException(infEgal.line, rightType, leftType));
        }
        return null;
    }

    @Override
    public Void visit(Superieur sup, Tds tds) {
        Idf idfLeft = (Idf) sup.left;
        if (tds.findSymbole(idfLeft.name) == null) {
            errors.addError(new UndefinedSymboleException(sup.left.toString(), sup.line));
            return null;
        }
        String leftType = sup.left.accept(visitor, tds);
        String rightType = sup.right.accept(visitor, tds);
        if (!leftType.equals(rightType)) {
            errors.addError(new TypeException(sup.line, rightType, leftType));
        }
        return null;
    }

    @Override
    public Void visit(SuperieurEgal supEgal, Tds tds) {
        Idf idfLeft = (Idf) supEgal.left;
        if (tds.findSymbole(idfLeft.name) == null) {
            errors.addError(new UndefinedSymboleException(supEgal.left.toString(), supEgal.line));
            return null;
        }
        String leftType = supEgal.left.accept(visitor, tds);
        String rightType = supEgal.right.accept(visitor, tds);
        if (!leftType.equals(rightType)) {
            errors.addError(new TypeException(supEgal.line, rightType, leftType));
        }
        return null;
    }

    @Override
    public Void visit(Plus plus, Tds tds) {
        Idf idfLeft = (Idf) plus.left;
        if (tds.findSymbole(idfLeft.name) == null) {
            errors.addError(new UndefinedSymboleException(plus.left.toString(), plus.line));
            return null;
        }
        String leftType = plus.left.accept(visitor, tds);
        String rightType = plus.right.accept(visitor, tds);
        if (!leftType.equals(rightType)) {
            errors.addError(new TypeException(plus.line, rightType, leftType));
        }
        return null;
    }

    @Override
    public Void visit(Minus minus, Tds tds) {
        Idf idfLeft = (Idf) minus.left;
        if (tds.findSymbole(idfLeft.name) == null) {
            errors.addError(new UndefinedSymboleException(minus.left.toString(), minus.line));
            return null;
        }
        String leftType = minus.left.accept(visitor, tds);
        String rightType = minus.right.accept(visitor, tds);
        if (!leftType.equals(rightType)) {
            errors.addError(new TypeException(minus.line, rightType, leftType));
        }
        return null;
    }

    @Override
    public Void visit(Division div, Tds tds) {
        Idf idfLeft = (Idf) div.left;
        if (tds.findSymbole(idfLeft.name) == null) {
            errors.addError(new UndefinedSymboleException(div.left.toString(), div.line));
            return null;
        }
        String leftType = div.left.accept(visitor, tds);
        String rightType = div.right.accept(visitor, tds);
        if (!leftType.equals(rightType)) {
            errors.addError(new TypeException(div.line, rightType, leftType));
        }
        return null;
    }

    @Override
    public Void visit(Multiplication mult, Tds tds) {
        Idf idfLeft = (Idf) mult.left;
        if (tds.findSymbole(idfLeft.name) == null) {
            errors.addError(new UndefinedSymboleException(mult.left.toString(), mult.line));
            return null;
        }
        String leftType = mult.left.accept(visitor, tds);
        String rightType = mult.right.accept(visitor, tds);
        if (!leftType.equals(rightType)) {
            errors.addError(new TypeException(mult.line, rightType, leftType));
        }
        return null;
    }

    @Override
    public Void visit(Fleche fleche, Tds tds) {
        Idf idfLeft = (Idf) fleche.left;
        if (tds.findSymbole(idfLeft.name) == null) {
            errors.addError(new UndefinedSymboleException(fleche.left.toString(), fleche.line));
            return null;
        }
        String leftType = fleche.left.accept(visitor, tds);
        Tds tdsStruct = tds.findSymboleStruct(leftType).getTds();

        String rightType = fleche.right.accept(visitor, tdsStruct);
        if (rightType == null) {
            errors.addError(new TypeException(fleche.line, rightType, leftType));
        }
        return null;
    }

    @Override
    public Void visit(MoinsUnaire unaire, Tds tds) {
        String type = unaire.accept(visitor, tds);
        if (!type.equals("int")) {
            return null;
        }
        errors.addError(new UnauthorizedOperationException(unaire.line));
        return null;
    }

    @Override
    public Void visit(Negation unaire, Tds tds) {
        String type = unaire.accept(visitor, tds);
        if (type == null) {
            errors.addError(new UnauthorizedOperationException(unaire.line));
        }
        return null;
    }

    @Override
    public Void visit(Semicolon semicolon, Tds tds) {
        return null;
    }

}
