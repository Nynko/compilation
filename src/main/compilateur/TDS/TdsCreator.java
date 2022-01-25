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
import compilateur.ast.Fichier;
import compilateur.ast.Fleche;
import compilateur.ast.Idf;
import compilateur.ast.IdfParenthesis;
import compilateur.ast.IdfParenthesisEmpty;
import compilateur.ast.IfThen;
import compilateur.ast.IfThenElse;
import compilateur.ast.IntNode;
import compilateur.ast.MoinsUnaire;
import compilateur.ast.Negation;
import compilateur.ast.Operateur;
import compilateur.ast.ParamInt;
import compilateur.ast.ParamListMulti;
import compilateur.ast.ParamStruct;
import compilateur.ast.Return;
import compilateur.ast.Semicolon;
import compilateur.ast.Sizeof;
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

        try {
            SymboleInt n = new SymboleInt("n");
            n.addDefinitionLine(-1);
            // ajout print
            Tds tds_print = tds.nouvelleSousTDS("print"); // Création d'une nouvelle Tds
            tds_print.addSymboleParam("n", n);
            SymboleFonction symbole_print = new SymboleFonction("print", tds_print);
            symbole_print.setReturnType("void");
            symbole_print.addDefinitionLine(-1);
            tds.addSymbole("print", symbole_print);

            // ajout malloc
            Tds tds_malloc = tds.nouvelleSousTDS("malloc"); // Création d'une nouvelle Tds
            tds_malloc.addSymboleParam("n", n);
            SymboleFonction symbole_malloc = new SymboleFonction("malloc", tds_print);
            symbole_malloc.setReturnType("void_*");
            symbole_malloc.addDefinitionLine(-1);
            tds.addSymbole("malloc", symbole_malloc);
        } catch (Exception e) {
            // TODO: handle exception
        }

        for (Ast ast : fichier.instructions) {
            ast.accept(this, tds);
        }

        Symbole main = tds.findSymbole("main");
        if (main == null) {
            this.errors.addError(new MainNotFoundException());
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

        boolean asReturn = false;
        if (declFctInt.bloc != null) {
            for (Ast ast : ((Bloc) declFctInt.bloc).instList) {
                if (ast instanceof Return) {
                    asReturn = true;
                    break;
                }
            }
            if (!asReturn) {
                this.errors.addError(new ReturnFunctionException(declFctInt.line));
            }
            declFctInt.bloc.accept(this, tdsFunction);
        } else {
            this.errors.addError(new ReturnFunctionException(declFctInt.line));
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
        if (tds.findSymboleStruct("struct_" + structName) == null) {
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

        boolean asReturn = false;
        if (declFctStruct.bloc != null) {
            for (Ast ast : ((Bloc) declFctStruct.bloc).instList) {
                if (ast instanceof Return) {
                    asReturn = true;
                    break;
                }
            }
            if (!asReturn) {
                this.errors.addError(new ReturnFunctionException(declFctStruct.line));
            }
            declFctStruct.bloc.accept(this, tdsFunction);
        } else {
            this.errors.addError(new ReturnFunctionException(declFctStruct.line));
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

    @Override
    public Void visit(Operateur operateur, Tds tds) {
        operateur.accept(visitor, tds);
        return null;
    }

}
