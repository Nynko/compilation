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

    @Override
    public Void visit(Fichier fichier, Tds tds) {

        tds.addnumRegion(0);

        if (fichier.instructions != null) {
            for (Ast ast : fichier.instructions) {
                ast.accept(this, tds);
            }
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

        Tds structTds = new Tds(tds);
        symboleStruct.setTds(structTds);

        try {
            tds.addSymbole("struct_" + idf, symboleStruct);
        } catch (SymbolAlreadyExistsException e) {
            errors.addError(e);
        }

        for (Ast ast : decl_typ.decl) {
            if (ast instanceof DeclVarInt || ast instanceof DeclVarStruct) {
                ast.accept(this, structTds);
            } else {
                // TODO : erreur sémantique, la struct contient une instruction incorrecte
            }
        }
        return null;
    }

    @Override
    public Void visit(DeclFctInt declFctInt, Tds tds) {
        String name = ((Idf) declFctInt.Idf).name;

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
            for (Ast ast : ((ParamListMulti) declFctInt.param).paramList) {
                if (ast instanceof ParamInt) {
                    ast.accept(this, tdsFunction);
                } else if (ast instanceof ParamStruct) {
                    ast.accept(this, tdsFunction);
                } else {
                    throw new Error(
                            "Erreur de remontée des symboles dans visit(DeclFctInt...), symbole doit être SymboleInt ou SymboleStruct)");
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

        Tds tdsFunction = new Tds(tds); // Création d'une nouvelle Tds
        SymboleFonction symboleFonction = new SymboleFonction(functionName, tdsFunction);
        symboleFonction.addDefinitionLine(declFctStruct.line);

        if (!(tds.findSymbole(structName) instanceof SymboleStructContent)) {
            // Si le type de struct n'existe pas
            errors.addError(new UndefinedStructureException(structName, declFctStruct.line));
        }

        if (declFctStruct.param != null) {
            for (Ast ast : ((ParamListMulti) declFctStruct.param).paramList) {
                if (ast instanceof ParamInt) {
                    ast.accept(this, tdsFunction);
                } else if (ast instanceof ParamStruct) {
                    ast.accept(this, tdsFunction);
                } else {
                    throw new Error(
                            "Erreur de remontée des symboles dans visit(declFctStruct...), symbole doit être SymboleInt ou SymboleStruct)");
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
        return null;

    }

    @Override
    public Void visit(IdfParenthesis idfParenthesis, Tds tds) {
        return null;

    }

    @Override
    public Void visit(IdfParenthesisEmpty idfParenthesisEmpty, Tds tds) {
        return null;

    }

    @Override
    public Void visit(IfThen ifThen, Tds tds) {
        Tds newTds = new Tds(tds);
        SymboleBlocAnonyme bloc = new SymboleBlocAnonyme(newTds);
        bloc.addDefinitionLine(ifThen.line);
        try {
            tds.addSymbole("ifThen", bloc); // il n'y aura qu'au plus un symbole nommé ifThen dans la tds
        } catch (SymbolAlreadyExistsException e) {
            errors.addError(e);
        }
        ifThen.thenBlock.accept(this, newTds);
        return null;
    }

    @Override
    public Void visit(IfThenElse ifThenElse, Tds tds) {
        Tds newTds = new Tds(tds);
        Tds newTdsElse = new Tds(tds);
        SymboleBlocAnonyme blocThen = new SymboleBlocAnonyme(newTds);
        SymboleBlocAnonyme blocElse = new SymboleBlocAnonyme(newTdsElse);
        blocThen.addDefinitionLine(ifThenElse.lineIf);
        blocElse.addDefinitionLine(ifThenElse.lineElse);
        try {
            tds.addSymbole("ifThen" + Tds.getCompteurSymbole(), blocThen);
        } catch (SymbolAlreadyExistsException e) {
            errors.addError(e);
        }

        try {
            tds.addSymbole("ifElse" + Tds.getCompteurSymbole(), blocElse);
        } catch (SymbolAlreadyExistsException e) {
            errors.addError(e);
        }
        System.out.println("SALUT");
        ifThenElse.thenBlock.accept(this, newTds);
        ifThenElse.elseBlock.accept(this, newTdsElse);
        System.out.println("AU REVOIR");
        return null;

    }

    @Override
    public Void visit(While while1, Tds tds) {
        Tds newTds = new Tds(tds);
        SymboleBlocAnonyme bloc = new SymboleBlocAnonyme(newTds);
        bloc.addDefinitionLine(while1.line);
        try {
            tds.addSymbole("While" + Tds.getCompteurSymbole(), bloc);
        } catch (SymbolAlreadyExistsException e) {
            errors.addError(e);
        }

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
                // System.out.println();
                // System.out.println(symfct.getReturnType() +", " + return1.accept(visitor, tds)+ ", " + symfct.getName());
                if (typeRetour == null) {
                    System.out.println("symbole inconnu ligne : " + return1.line);
                    // TODO erreur symbole inconnu 
                } else if (!symfct.getReturnType().equals(typeRetour)) {
                    System.out.println("erreur type retour incorrect ligne : " + return1.line);
                    // TODO erreur type de retour incorrect
                } 
                return null;
            }
        }
        return null;
    }

    @Override
    public Void visit(Bloc bloc, Tds tds) {
        int i = 0;
        ArrayList<Ast> listeAst = bloc.instList;
        int longueurListe = listeAst.size();
        Ast ast = null;
        if (i < longueurListe)
            ast = listeAst.get(i++);

        while (i <= longueurListe && ((ast instanceof DeclVarInt) || (ast instanceof DeclVarStruct))) { // Tant qu'objet de types decl_vars
            ast.accept(this, tds);
            if (i == longueurListe)
                break;
            ast = listeAst.get(i++);
        }

        // types instructions
        while (i <= longueurListe) {
            // On ne va modifier la TDS que pour les instructions générants un nouveau bloc
            if (ast instanceof IfThen) {
                ast.accept(this, tds);
            } else if (ast instanceof IfThenElse) {
                ast.accept(this, tds);
            } else if (ast instanceof While) {
                ast.accept(this, tds);
            } else if (ast instanceof Bloc) {
                Tds newTds = new Tds(tds);
                SymboleBlocAnonyme symbole = new SymboleBlocAnonyme(newTds);
                try {
                    tds.addSymbole("Bloc" + Tds.getCompteurSymbole(), symbole); // il n'y aura qu'au plus un symbole nommé bloc dans la tds
                } catch (SymbolAlreadyExistsException e) {
                    errors.addError(e);
                }
                ast.accept(this, newTds);
            } else if (ast instanceof Return) {
                ast.accept(this, tds);
            } else if (ast instanceof Affectation) {
                ast.accept(this, tds);
            } else if (ast instanceof Expr_ou) {
                ast.accept(this, tds);
            } else if (ast instanceof Expr_et) {
                ast.accept(this, tds);
            } else if (ast instanceof Egal) {
                ast.accept(this, tds);
            } else if (ast instanceof Different) {
                ast.accept(this, tds);
            } else if (ast instanceof Inferieur) {
                ast.accept(this, tds);
            } else if (ast instanceof InferieurEgal) {
                ast.accept(this, tds);
            } else if (ast instanceof Superieur) {
                ast.accept(this, tds);
            } else if (ast instanceof SuperieurEgal) {
                ast.accept(this, tds);
            } else if (ast instanceof Plus) {
                ast.accept(this, tds);
            } else if (ast instanceof Minus) {
                ast.accept(this, tds);
            } else if (ast instanceof Multiplication) {
                ast.accept(this, tds);
            } else if (ast instanceof Division) {
                ast.accept(this, tds);
            } else if (ast instanceof Fleche) {
                ast.accept(this, tds);
            } else if (ast instanceof MoinsUnaire) {
                ast.accept(this, tds);
            } else if (ast instanceof Negation) {
                ast.accept(this, tds);
            } 
            if (i == longueurListe)
                break;
            ast = listeAst.get(i++);
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
        if (!leftType.equals(rightType)) {
            System.out.println(leftType + ",, " + rightType);
            // TODO erreur de type a l'affectation
        }
        return null;
    }

    @Override
    public Void visit(Expr_ou expr_ou, Tds tds) {
        String leftType = expr_ou.left.accept(visitor, tds);
        String rightType = expr_ou.right.accept(visitor, tds);
        if (!leftType.equals(rightType)) {
            System.out.println(leftType + ",, " + rightType);
            // TODO erreur de type
        }
        return null;
    }

    @Override
    public Void visit(Expr_et expr_et, Tds tds) {
        String leftType = expr_et.left.accept(visitor, tds);
        String rightType = expr_et.right.accept(visitor, tds);
        if (!leftType.equals(rightType)) {
            System.out.println(leftType + ",, " + rightType);
            // TODO erreur de type
        }
        return null;
    }

    @Override
    public Void visit(Egal egal, Tds tds) {
        String leftType = egal.left.accept(visitor, tds);
        String rightType = egal.right.accept(visitor, tds);
        if (!leftType.equals(rightType)) {
            System.out.println(leftType + ",, " + rightType);
            // TODO erreur de type
        }
        return null;
    }

    @Override
    public Void visit(Different dif, Tds tds) {
        String leftType = dif.left.accept(visitor, tds);
        String rightType = dif.right.accept(visitor, tds);
        if (!leftType.equals(rightType)) {
            System.out.println(leftType + ",, " + rightType);
            // TODO erreur de type
        }
        return null;
    }

    @Override
    public Void visit(Inferieur inf, Tds tds) {
        String leftType = inf.left.accept(visitor, tds);
        String rightType = inf.right.accept(visitor, tds);
        if (!leftType.equals(rightType)) {
            System.out.println(leftType + ",, " + rightType);
            // TODO erreur de type
        }
        return null;
    }

    @Override
    public Void visit(InferieurEgal infEgal, Tds tds) {
        String leftType = infEgal.left.accept(visitor, tds);
        String rightType = infEgal.right.accept(visitor, tds);
        if (!leftType.equals(rightType)) {
            System.out.println(leftType + ",, " + rightType);
            // TODO erreur de type
        }
        return null;
    }

    @Override
    public Void visit(Superieur sup, Tds tds) {
        String leftType = sup.left.accept(visitor, tds);
        String rightType = sup.right.accept(visitor, tds);
        if (!leftType.equals(rightType)) {
            System.out.println(leftType + ",, " + rightType);
            // TODO erreur de type
        }
        return null;
    }

    @Override
    public Void visit(SuperieurEgal supEgal, Tds tds) {
        String leftType = supEgal.left.accept(visitor, tds);
        String rightType = supEgal.right.accept(visitor, tds);
        if (!leftType.equals(rightType)) {
            System.out.println(leftType + ",, " + rightType);
            // TODO erreur de type
        }
        return null;
    }

    @Override
    public Void visit(Plus plus, Tds tds) {
        String leftType = plus.left.accept(visitor, tds);
        String rightType = plus.right.accept(visitor, tds);
        if (!leftType.equals(rightType)) {
            System.out.println(leftType + ",, " + rightType);
            // TODO erreur de type
        }
        return null;
    }

    @Override
    public Void visit(Minus minus, Tds tds) {
        String leftType = minus.left.accept(visitor, tds);
        String rightType = minus.right.accept(visitor, tds);
        if (!leftType.equals(rightType)) {
            System.out.println(leftType + ",, " + rightType);
            // TODO erreur de type
        }
        return null;
    }

    @Override
    public Void visit(Division div, Tds tds) {
        String leftType = div.left.accept(visitor, tds);
        String rightType = div.right.accept(visitor, tds);
        if (!leftType.equals(rightType)) {
            System.out.println(leftType + ",, " + rightType);
            // TODO erreur de type
        }
        return null;
    }

    @Override
    public Void visit(Multiplication mult, Tds tds) {
        String leftType = mult.left.accept(visitor, tds);
        String rightType = mult.right.accept(visitor, tds);
        if (!leftType.equals(rightType)) {
            System.out.println(leftType + ",, " + rightType);
            // TODO erreur de type
        }
        return null;

    }

    @Override
    public Void visit(Fleche fleche, Tds tds) {
        String leftType = fleche.left.accept(visitor, tds);
        Tds tdsStruct = tds.findSymboleStruct(leftType).getTds();

        String rightType = fleche.right.accept(visitor, tdsStruct);
        if (rightType == null) {
            // TODO erreur
        }
        return null;
    }

    @Override
    public Void visit(MoinsUnaire unaire, Tds tds) {
        String type = unaire.accept(visitor, tds);
        if (type == null) {
            // TODO erreur type
        }
        return null;
    }

    @Override
    public Void visit(Negation unaire, Tds tds) {
        String type = unaire.accept(visitor, tds);
        if (type == null) {
            // TODO erreur type
        }
        return null;
    }

    @Override
    public Void visit(Semicolon semicolon, Tds tds) {
        return null;
    }

}
