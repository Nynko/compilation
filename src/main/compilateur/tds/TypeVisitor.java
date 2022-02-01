package compilateur.tds;

import java.util.ArrayList;

import compilateur.ast.Affectation;
import compilateur.ast.Ast;
import compilateur.ast.Bloc;
import compilateur.ast.CharNode;
import compilateur.ast.Comparaison;
import compilateur.ast.DeclFctInt;
import compilateur.ast.DeclFctStruct;
import compilateur.ast.DeclVarInt;
import compilateur.ast.DeclVarStruct;
import compilateur.ast.Decl_typ;
import compilateur.ast.Division;
import compilateur.ast.Expr_et;
import compilateur.ast.Expr_ou;
import compilateur.ast.Fichier;
import compilateur.ast.Fleche;
import compilateur.ast.Idf;
import compilateur.ast.IdfParenthesis;
import compilateur.ast.IdfParenthesisEmpty;
import compilateur.ast.IfThen;
import compilateur.ast.IfThenElse;
import compilateur.ast.IntNode;
import compilateur.ast.Minus;
import compilateur.ast.MoinsUnaire;
import compilateur.ast.Multiplication;
import compilateur.ast.Negation;
import compilateur.ast.Operateur;
import compilateur.ast.ParamInt;
import compilateur.ast.ParamListMulti;
import compilateur.ast.ParamStruct;
import compilateur.ast.Plus;
import compilateur.ast.Return;
import compilateur.ast.Semicolon;
import compilateur.ast.Sizeof;
import compilateur.ast.While;
import compilateur.utils.ErrorAggregator;

public class TypeVisitor implements TdsVisitor<String> {

    private ErrorAggregator errors = new ErrorAggregator();

    public ErrorAggregator getErrors() {
        return this.errors;
    }

    @Override
    public String visit(Fichier fichier, Tds tds) {
        return null;
    }

    public void setErrorAggregator(ErrorAggregator agg) {
        this.errors = agg;
    }

    @Override
    public String visit(Idf idf, Tds tds) {

        Symbole sym = tds.findSymbole(idf.name);
        if (sym instanceof SymboleFonction symfct) {
            return symfct.getReturnType();
        } else if (sym instanceof SymboleInt) {
            return "int";
        } else if (sym instanceof SymboleStruct symstruct) {
            return TYPESTRUCT + symstruct.getStruct().getName();
        } else if (sym instanceof SymboleStructContent symstructcontent) {
            return TYPESTRUCT + symstructcontent.getName();
        } else {
            errors.addError(new UndefinedSymboleException(idf.name, idf.line));
        }
        return null;
    }

    @Override
    public String visit(DeclVarInt declVarInt, Tds tds) {
        return null;
    }

    @Override
    public String visit(DeclVarStruct declVarStruct, Tds tds) {
        return null;
    }

    @Override
    public String visit(Decl_typ decl_typ, Tds tds) {
        return null;
    }

    @Override
    public String visit(DeclFctInt declFctInt, Tds tds) {
        return null;
    }

    @Override
    public String visit(DeclFctStruct declFctStruct, Tds tds) {
        return null;
    }

    @Override
    public String visit(ParamListMulti paramListMulti, Tds tds) {
        return null;
    }

    @Override
    public String visit(ParamInt paramInt, Tds tds) {
        return paramInt.name.accept(this, tds);
    }

    @Override
    public String visit(ParamStruct paramStruct, Tds tds) {
        return paramStruct.idf1.accept(this, tds);
    }

    @Override
    public String visit(Sizeof sizeof, Tds tds) {
        return "int";
    }

    @Override
    public String visit(IdfParenthesis idfParenthesis, Tds tds) {
        int nbParam = idfParenthesis.exprList.size();
        String fctName = ((Idf) idfParenthesis.idf).name;
        Symbole sym = tds.findSymbole(fctName);
        if (sym != null) {
            SymboleFonction symf = (SymboleFonction) sym;
            ArrayList<SymboleVar> param = symf.getTds().getParams();
            int min = param.size();
            if (min != nbParam) {
                this.errors.addError(new NumberParameterException(sym.getName(), idfParenthesis.line, min, nbParam));
                if (nbParam < min) {
                    min = nbParam;
                }
            }
            for (int i = 0; i < min; i++) {
                SymboleVar s = param.get(i);
                String paramTypeRef = "";
                if (s instanceof SymboleInt) {
                    paramTypeRef = "int";
                } else if (s instanceof SymboleStruct symstruct) {
                    paramTypeRef = TYPESTRUCT + symstruct.getStruct().getName();
                }
                String paramType = idfParenthesis.exprList.get(i).accept(this, tds);
                if (!paramType.equals(paramTypeRef)) {
                    this.errors.addError(new TypeException(idfParenthesis.line, paramType, paramTypeRef));
                }
            }
        }
        return idfParenthesis.idf.accept(this, tds);
    }

    @Override
    public String visit(IdfParenthesisEmpty idfParenthesisEmpty, Tds tds) {

        String fctName = ((Idf) idfParenthesisEmpty.idf).name;
        Symbole sym = tds.findSymbole(fctName);
        if (sym != null) {
            SymboleFonction symf = (SymboleFonction) sym;
            if (!symf.getTds().getParams().isEmpty()) {
                this.errors.addError(new NumberParameterException(symf.getName(), idfParenthesisEmpty.line,
                        symf.getTds().getParams().size(), 0));
            }
        }
        return idfParenthesisEmpty.idf.accept(this, tds);
    }

    @Override
    public String visit(IfThen ifThen, Tds tds) {
        return null;
    }

    @Override
    public String visit(IfThenElse ifThenElse, Tds tds) {
        return null;
    }

    @Override
    public String visit(While while1, Tds tds) {
        return null;
    }

    @Override
    public String visit(Return return1, Tds tds) {
        return return1.expr.accept(this, tds);
    }

    @Override
    public String visit(Bloc bloc, Tds tds) {
        return null;
    }

    @Override
    public String visit(CharNode charNode, Tds tds) {
        return "int";
    }

    @Override
    public String visit(IntNode intNode, Tds tds) {
        return "int";
    }

    @Override
    public String visit(Affectation affectation, Tds tds) {
        String leftType = affectation.left.accept(this, tds);
        String rightType = affectation.right.accept(this, tds);
        // erreur a gauche
        if (leftType == null) {
            return null;
        }
        // erreur a droite
        if (rightType == null) {
            this.errors.addError(new BadOperandTypeException("affectation", affectation.line));
            return null;
        }
        // membre droit est initialisé si c'est un idf ?
        if (!this.testIfIsIdfAndInitialized(affectation.right, tds, affectation.line)) {
            return leftType;
        }

        // affectation reussie
        if (this.isCompatible(leftType, rightType) || leftType.equals("int") && this.isPointer(rightType)
                || this.isPointer(leftType) && rightType.equals("int") || this.isPointer(leftType) && this.isPointer(rightType)) {
            if (leftType.equals("int") && this.isPointer(rightType)
                    || this.isPointer(leftType) && rightType.equals("int") || this.isPointer(leftType) && this.isPointer(rightType) && !this.isCompatible(leftType, rightType)) {
                errors.addError(new TypeWarningException(affectation.line, rightType, leftType));
            }
            if (affectation.left instanceof Idf idf) {
                Symbole s = tds.findSymbole(idf.name);
                if (s instanceof SymboleVar sv) {
                    sv.setInitalized(true);
                }
            } else if (affectation.left instanceof Fleche fleche) {
                Idf idf = (Idf) fleche.right;
                Symbole s = tds.findSymbole(idf.name);
                if (s instanceof SymboleVar sv) {
                    sv.setInitalized(true);
                }
            }
            return leftType;
        }

        this.errors.addError(new BadOperandTypeException("affectation", affectation.line));
        return null;
    }

    @Override
    public String visit(Fleche fleche, Tds tds) {
        String leftType = fleche.left.accept(this, tds);
        if (leftType == null) {
            return null;
        }
        Tds tdsStruct = tds.findSymboleStruct(leftType).getTds();
        String rightType = fleche.right.accept(this, tdsStruct);

        // si a droite c'est un idf
        if (rightType == null || !this.testIfIsIdfAndInitialized(fleche.right, tds, fleche.line)) {
            return null;
        }
        // si a gauche c'est un idf
        if (!this.testIfIsIdfAndInitialized(fleche.left, tds, fleche.line)) {
            return null;
        }

        return rightType;
    }

    @Override
    public String visit(MoinsUnaire unaire, Tds tds) {
        String type = unaire.noeud.accept(this, tds);
        if (type == null || !this.testIfIsIdfAndInitialized(unaire.noeud, tds, unaire.line)) {
            return null;
        }
        if (this.isPointer(type)) {
            this.errors.addError(new UnauthorizedOperationException(unaire.line));
            return null;
        }
        return type;
    }

    @Override
    public String visit(Negation unaire, Tds tds) {
        String type = unaire.noeud.accept(this, tds);
        if (type == null) {
            this.errors.addError(new UnauthorizedOperationException(unaire.line));
            return null;
        }
        if (!this.testIfIsIdfAndInitialized(unaire.noeud, tds, unaire.line)) {
            return null;
        }
        return "int";
    }

    @Override
    public String visit(Semicolon semicolon, Tds tds) {
        return null;
    }

    @Override
    public String visit(Operateur operateur, Tds tds) {
        String leftType = operateur.left.accept(this, tds);
        String rightType = operateur.right.accept(this, tds);
        if (leftType == null || rightType == null) {
            return null;
        }
        // test si a gauche c'est un idf, si oui, on regarde s'il est initialisé
        if (!this.testIfIsIdfAndInitialized(operateur.left, tds, operateur.line)) {
            return null;
        }

        // test si a droite c'est un idf, si oui, on regarde s'il est initialisé
        if (!this.testIfIsIdfAndInitialized(operateur.right, tds, operateur.line)) {
            return null;
        }

        // division par 0
        if (operateur instanceof Division && operateur.right instanceof IntNode intr && intr.parseInt == 0) {
            this.errors.addError(new DivisionByZeroException(operateur.line));
        }

        // si les types des operandes sont compatibles sans warnning
        if (this.isCompatible(leftType, rightType) && !leftType.equals("void_*") && !rightType.equals("void_*")) {
            // addition, multiplication, division entre pointeur
            if (this.isPointer(leftType) && (operateur instanceof Plus || operateur instanceof Multiplication
                    || operateur instanceof Division)) {
                this.errors.addError(new UnauthorizedOperationException(operateur.line));
                return null;
            }
            // comparaison + soustraction entre pointeur
            if (operateur instanceof Comparaison || operateur instanceof Expr_et || operateur instanceof Expr_ou
                    || (this.isPointer(leftType) && operateur instanceof Minus)) {
                return "int";
            }
            return leftType;
        } else {
            if (operateur instanceof Expr_et || operateur instanceof Expr_ou) {
                return "int";
            }
            // arithmétique de pointeur
            if (operateur instanceof Plus) {
                if (leftType.equals("int") && this.isPointer(rightType)) {
                    return rightType;
                } else if (rightType.equals("int") && this.isPointer(leftType)) {
                    return leftType;
                }
            } else if (rightType.equals("int") && this.isPointer(leftType) && operateur instanceof Minus) {
                return leftType;
            }
            // comparaison entre 2 types différents
            if (operateur instanceof Comparaison) {
                this.errors.addError(new ComparisonWarningException(operateur.line, leftType, rightType));
                return "int";
            }
            // soustraction par un pointeur, multiplication ou division avec un pointeur
            if ((this.isPointer(rightType) && operateur instanceof Minus) || ((operateur instanceof Multiplication
                    || operateur instanceof Division) && (this.isPointer(rightType) || this.isPointer(leftType)))) {
                this.errors.addError(new UnauthorizedOperationException(operateur.line));
                return null;
            }
        }
        return null;
    }

    private boolean isCompatible(String leftType, String rightType) {
        return leftType.equals(rightType) || this.isPointer(leftType) && rightType.equals("void_*");
    }

    private boolean isPointer(String type) {
        return type.startsWith(TYPESTRUCT) || type.endsWith("*");
    }

    private boolean testIfIsIdfAndInitialized(Ast ast, Tds tds, int line) {
        if (ast instanceof Idf idf) {
            Symbole s = tds.findSymbole(idf.name);
            if (s instanceof SymboleVar sv && !sv.isInitalized()) {
                this.errors.addError(new VarNotInitializedException(line, sv.getName(), sv.getDefinitionLine()));
                return false;
            }
        }
        return true;
    }
}
