package compilateur.tds;

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
import compilateur.ast.AstNode;
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

    public static final String PRINT = "print";
    public static final String MALLOC = "malloc";
    public static final String MAIN = "main";

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
        fichier.setTds(tds);
        tds.addnumRegion(0);
        if (fichier.instructions == null)
            return null;

        try {
            SymboleInt n = new SymboleInt("n");
            n.addDefinitionLine(-1);
            // ajout print
            Tds tds_print = tds.nouvelleSousTDS(PRINT); // Création d'une nouvelle Tds
            tds_print.addSymboleParam("n", n);
            SymboleFonction symbole_print = new SymboleFonction(PRINT, tds_print);
            symbole_print.setReturnType("void");
            symbole_print.addDefinitionLine(-1);
            tds.addSymbole(PRINT, symbole_print);

            // ajout malloc
            Tds tds_malloc = tds.nouvelleSousTDS(MALLOC); // Création d'une nouvelle Tds
            tds_malloc.addSymboleParam("n", n);
            SymboleFonction symbole_malloc = new SymboleFonction(MALLOC, tds_print);
            symbole_malloc.setReturnType("void_*");
            symbole_malloc.addDefinitionLine(-1);
            tds.addSymbole(MALLOC, symbole_malloc);
        } catch (SymbolAlreadyExistsException e) {
            this.errors.addError(e);
        }

        for (Ast ast : fichier.instructions) {
            ast.accept(this, tds);
        }

        // test que la fonction main est présente
        Symbole main = tds.findSymbole(MAIN);
        if (main == null) {
            this.errors.addError(new MainNotFoundException());
            return null;
        }
        SymboleFonction f = ((SymboleFonction) main);
        // test si le type de retour est bien int
        if (!f.getReturnType().equals("int")) {
            this.errors.addError(new TypeException(main.getDefinitionLine(), f.getReturnType(), "int"));
        }
        // test si le nombre de parametre est bien nul
        int nb;
        if ((nb = f.getTds().getParams().size()) != 0) {
            this.errors.addError(new NumberParameterException(f.getName(), f.getDefinitionLine(), 0, nb));
        }

        return null;
    }

    @Override
    public Void visit(Idf idf, Tds tds) {
        idf.setTds(tds);
        // System.out.println("visite idf= "+ idf.name + "   -  " + idf.getTds().getName() + " " +  idf);
        return null;
    }

    @Override
    public Void visit(DeclVarInt declVarInt, Tds tds) {
        declVarInt.setTds(tds);
        for(Ast ast: declVarInt.idf){
            ast.accept(this, tds);
        }
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
        declVarStruct.setTds(tds);

        for(Ast ast: declVarStruct.idf){
            ast.accept(this, tds);
        }

        ArrayList<Ast> liste = declVarStruct.idf;

        String structName = ((Idf) liste.get(0)).name;

        // Récupération de la structure
        SymboleStructContent struct;
        struct = tds.findSymboleStruct(TYPESTRUCT + structName);

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
        decl_typ.setTds(tds);

        decl_typ.idf.accept(this, tds);

        // Création d'une struct
        String idf = ((Idf) decl_typ.idf).name;

        SymboleStructContent symboleStruct = new SymboleStructContent(idf);
        symboleStruct.addDefinitionLine(decl_typ.line);

        Tds structTds = tds.nouvelleSousTDS(TYPESTRUCT + idf);
        symboleStruct.setTds(structTds);

        try {
            tds.addSymbole(TYPESTRUCT + idf, symboleStruct);
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
        declFctInt.setTds(tds);

        declFctInt.Idf.accept(this, tds);

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
            if (!this.as_return(declFctInt.bloc)) {
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
        declFctStruct.setTds(tds);
        declFctStruct.Idf1.accept(this, tds);

        String structName = ((Idf) declFctStruct.Idf0).name;
        String functionName = ((Idf) declFctStruct.Idf1).name;

        Tds tdsFunction = tds.nouvelleSousTDS("fn_" + functionName); // Création d'une nouvelle Tds
        SymboleFonction symboleFonction = new SymboleFonction(functionName, tdsFunction);
        symboleFonction.setReturnType(TYPESTRUCT + structName + "_*");
        symboleFonction.addDefinitionLine(declFctStruct.line);

        try {
            tds.addSymbole(functionName, symboleFonction);
        } catch (SymbolAlreadyExistsException e) {
            errors.addError(e);
        }
        if (tds.findSymboleStruct(TYPESTRUCT + structName) == null) {
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
            if (!this.as_return(declFctStruct.bloc)) {
                this.errors.addError(new ReturnFunctionException(declFctStruct.line));
            }
            declFctStruct.bloc.accept(this, tdsFunction);
        } else {
            this.errors.addError(new ReturnFunctionException(declFctStruct.line));
        }
        return null;
    }

    public boolean as_return(Ast ast) {
        boolean asReturn = false;
        if (ast instanceof Return) {
            return true;
        } else if (ast instanceof IfThenElse ifThenElse) {
            return this.as_return(ifThenElse.thenBlock) && this.as_return(ifThenElse.elseBlock);
        } else if (ast instanceof While w) {
            return this.as_return(w.doBlock);
        } else if (ast instanceof Bloc bloc) {
            ArrayList<Integer> lindex = new ArrayList<>();
            // verifie si il y a un return dans le bloc
            for (int index = 0; index < bloc.instList.size(); index++) {
                if (asReturn) {
                    this.errors.addError(new CodeNeverUseWarningException(((AstNode) bloc.instList.get(index)).line));
                } else {
                    Ast a = bloc.instList.get(index);
                    if (a instanceof Return) {
                        asReturn = true;
                    } else if (a instanceof IfThenElse || a instanceof Bloc || a instanceof While) {
                        lindex.add(index);
                    }
                }
            }
            if (asReturn) {
                return true;
            }
            if (lindex.isEmpty()) {
                return false;
            }
            int indexReturn = -1;
            // on verifie que dans tout les sous-blocs il y a un return
            for (int i : lindex) {
                if (this.as_return(bloc.instList.get(i))) {
                    asReturn = true;
                    indexReturn = i;
                    break;
                }
            }
            if (asReturn) {
                for (int i = indexReturn + 1; i < bloc.instList.size(); i++) {
                    this.errors.addError(new CodeNeverUseWarningException(((AstNode) bloc.instList.get(i)).line));
                }
            }
        }
        return asReturn;
    }

    @Override
    public Void visit(ParamListMulti paramListMulti, Tds tds) {
        paramListMulti.setTds(tds);
        return null;
        // Ne sera jamais visité
    }

    @Override
    public Void visit(ParamInt paramInt, Tds tds) {
        paramInt.setTds(tds);
        paramInt.name.accept(this, tds);
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
        paramStruct.setTds(tds);
        paramStruct.idf1.accept(this,tds);


        String structName = ((Idf) paramStruct.idf0).name;

        // Récupération de la structure
        SymboleStructContent struct;
        struct = tds.findSymboleStruct(TYPESTRUCT + structName);

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
        sizeof.setTds(tds);
        sizeof.name.accept(visitor, tds);
        return null;

    }

    @Override
    public Void visit(IdfParenthesis idfParenthesis, Tds tds) {
        idfParenthesis.setTds(tds);
        idfParenthesis.accept(visitor, tds);
        return null;
    }

    @Override
    public Void visit(IdfParenthesisEmpty idfParenthesisEmpty, Tds tds) {
        idfParenthesisEmpty.setTds(tds);
        idfParenthesisEmpty.accept(visitor, tds);
        return null;
    }

    @Override
    public Void visit(IfThen ifThen, Tds tds) {
        ifThen.setTds(tds);
        ifThen.condition.accept(visitor, tds);
        ifThen.condition.accept(this, tds);
        Tds newTds = tds.nouvelleSousTDS("thenblock");
        ifThen.thenBlock.accept(this, newTds);
        return null;
    }

    @Override
    public Void visit(IfThenElse ifThenElse, Tds tds) {
        ifThenElse.setTds(tds);
        ifThenElse.condition.accept(visitor, tds);
        ifThenElse.condition.accept(this, tds);
        Tds newTds = tds.nouvelleSousTDS("thenblock");
        Tds newTdsElse = tds.nouvelleSousTDS("elseblock");
        ifThenElse.thenBlock.accept(this, newTds);
        ifThenElse.elseBlock.accept(this, newTdsElse);

        return null;
    }

    @Override
    public Void visit(While while1, Tds tds) {
        while1.setTds(tds);
        while1.condition.accept(this, tds);
        while1.condition.accept(visitor,tds);
        Tds newTds = tds.nouvelleSousTDS("whileblock");
        while1.doBlock.accept(this, newTds);
        return null;
    }

    @Override
    public Void visit(Return return1, Tds tds) {
        return1.setTds(tds);
        return1.expr.accept(this, tds);
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
        bloc.setTds(tds);
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
        charNode.setTds(tds);
        return null;
    }

    @Override
    public Void visit(IntNode intNode, Tds tds) {
        intNode.setTds(tds);
        return null;
    }

    @Override
    public Void visit(Affectation affectation, Tds tds) {
        affectation.setTds(tds);
        affectation.accept(visitor, tds);
        affectation.left.accept(this, tds);
        affectation.right.accept(this, tds);

        return null;
    }

    @Override
    public Void visit(Fleche fleche, Tds tds) {
        fleche.setTds(tds);
        fleche.accept(visitor, tds);
        fleche.left.accept(this, tds);
        fleche.right.accept(this, tds);   
        return null;
    }

    @Override
    public Void visit(MoinsUnaire unaire, Tds tds) {
        unaire.setTds(tds);
        unaire.accept(visitor, tds);
        unaire.noeud.accept(this, tds);
        return null;
    }

    @Override
    public Void visit(Negation unaire, Tds tds) {
        unaire.setTds(tds);
        unaire.accept(visitor, tds);
        unaire.noeud.accept(this, tds);
        return null;
    }

    @Override
    public Void visit(Semicolon semicolon, Tds tds) {
        semicolon.setTds(tds);
        return null;
    }

    @Override
    public Void visit(Operateur operateur, Tds tds) {
        operateur.setTds(tds);
        operateur.accept(visitor, tds);
        operateur.left.accept(this, tds);
        operateur.right.accept(this, tds);

        return null;
    }

}
