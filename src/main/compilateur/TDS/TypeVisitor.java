package compilateur.TDS;

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
import compilateur.ast.MoinsUnaire;
import compilateur.ast.Multiplication;
import compilateur.ast.Negation;
import compilateur.ast.ParamInt;
import compilateur.ast.ParamListMulti;
import compilateur.ast.ParamStruct;
import compilateur.ast.Plus;
import compilateur.ast.Return;
import compilateur.ast.Semicolon;
import compilateur.ast.Sizeof;
import compilateur.ast.Superieur;
import compilateur.ast.SuperieurEgal;
import compilateur.ast.While;
import compilateur.utils.ErrorAggregator;

public class TypeVisitor implements TdsVisitor<String> {

    private ErrorAggregator errors = new ErrorAggregator();

    public ErrorAggregator getErrors() {
        return this.errors;
    }


    @Override
    public String visit(Fichier fichier, Tds tds) {
        // TODO Auto-generated method stub
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
            return "struct_" + symstruct.getStruct().getName();
        } else if (sym instanceof SymboleStructContent symstructcontent) {
            return "struct_" + symstructcontent.getName();
        } else {
            errors.addError(new UndefinedSymboleException(idf.name, idf.line));
        }
        return null;
    }

    @Override
    public String visit(DeclVarInt declVarInt, Tds tds) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(DeclVarStruct declVarStruct, Tds tds) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(Decl_typ decl_typ, Tds tds) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(DeclFctInt declFctInt, Tds tds) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(DeclFctStruct declFctStruct, Tds tds) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(ParamListMulti paramListMulti, Tds tds) {
        // TODO Auto-generated method stub
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
        String fctName = ((Idf)idfParenthesis.idf).name;
        SymboleFonction sym = (SymboleFonction)tds.findSymbole(fctName);
        // TODO nombre de parametre 
        // sym.getTds().get
        for (int i = 0; i < nbParam; i++) {
             String paramTypeRef = "";
            // =  sym.getParam(i)
            String paramType = idfParenthesis.exprList.get(i).accept(this, tds);
            if (!paramType.equals(paramTypeRef)) {
                //TODO erreur type parametre
            }
        }
        return idfParenthesis.idf.accept(this, tds);
    }

    @Override
    public String visit(IdfParenthesisEmpty idfParenthesisEmpty, Tds tds) {
        String fctName = ((Idf)idfParenthesisEmpty.idf).name;
        SymboleFonction sym = (SymboleFonction)tds.findSymbole(fctName);
        // if (sym.getParam() != 0) {
        //     // TODO erreur nombre d'arguments
        // }
        return idfParenthesisEmpty.idf.accept(this, tds);
    }

    @Override
    public String visit(IfThen ifThen, Tds tds) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(IfThenElse ifThenElse, Tds tds) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(While while1, Tds tds) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(Return return1, Tds tds) {
        return return1.expr.accept(this, tds);
    }

    @Override
    public String visit(Bloc bloc, Tds tds) {
        // TODO Auto-generated method stub
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
        if (leftType.equals(rightType)) {
            System.out.println(leftType + ", " + rightType);
            return leftType;
        } else {
            errors.addError(new TypeException(affectation.line, rightType, leftType));
        }
        return null;
    }

    @Override
    public String visit(Expr_ou expr_ou, Tds tds) {
        String leftType = expr_ou.left.accept(this, tds);
        String rightType = expr_ou.right.accept(this, tds);
        if (leftType.equals(rightType)) {
            return leftType;
        } else {
            errors.addError(new TypeException(expr_ou.line, rightType, leftType));
        }
        return null;
    }

    @Override
    public String visit(Expr_et expr_et, Tds tds) {
        String leftType = expr_et.left.accept(this, tds);
        String rightType = expr_et.right.accept(this, tds);
        if (leftType.equals(rightType)) {
            return leftType;
        } else {
            errors.addError(new TypeException(expr_et.line, rightType, leftType));
        }
        return null;
    }

    @Override
    public String visit(Egal egal, Tds tds) {
        String leftType = egal.left.accept(this, tds);
        String rightType = egal.right.accept(this, tds);
        if (leftType.equals(rightType)) {
            return leftType;
        } else {
            errors.addError(new TypeException(egal.line, rightType, leftType));
        }
        return null;
    }

    @Override
    public String visit(Different dif, Tds tds) {
        String leftType = dif.left.accept(this, tds);
        String rightType = dif.right.accept(this, tds);
        if (leftType.equals(rightType)) {
            return "int";
        } else {
            errors.addError(new TypeException(dif.line, rightType, leftType));
        }
        return null;
    }

    @Override
    public String visit(Inferieur inf, Tds tds) {
        String leftType = inf.left.accept(this, tds);
        String rightType = inf.right.accept(this, tds);
        if (leftType.equals(rightType)) {
            return "int";
        } else {
            errors.addError(new TypeException(inf.line, rightType, leftType));
        }
        return null;
    }

    @Override
    public String visit(InferieurEgal infEgal, Tds tds) {
        String leftType = infEgal.left.accept(this, tds);
        String rightType = infEgal.right.accept(this, tds);
        if (leftType.equals(rightType)) {
            return "int";
        } else {
            errors.addError(new TypeException(infEgal.line, rightType, leftType));
        }
        return null;
    }

    @Override
    public String visit(Superieur sup, Tds tds) {
        String leftType = sup.left.accept(this, tds);
        String rightType = sup.right.accept(this, tds);
        if (leftType.equals(rightType)) {
            return "int";
        } else {
            errors.addError(new TypeException(sup.line, rightType, leftType));
        }
        return null;
    }

    @Override
    public String visit(SuperieurEgal supEgal, Tds tds) {
        String leftType = supEgal.left.accept(this, tds);
        String rightType = supEgal.right.accept(this, tds);
        if (leftType.equals(rightType)) {
            return "int";
        } else {
            errors.addError(new TypeException(supEgal.line, rightType, leftType));
        }
        return null;
    }

    @Override
    public String visit(Plus plus, Tds tds) {
        String leftType = plus.left.accept(this, tds);
        String rightType = plus.right.accept(this, tds);
        if (leftType.equals(rightType)) {
            return leftType;
        } else {
            errors.addError(new TypeException(plus.line, rightType, leftType));
        }
        return null;
    }

    @Override
    public String visit(Minus minus, Tds tds) {
        String leftType = minus.left.accept(this, tds);
        String rightType = minus.right.accept(this, tds);
        if (leftType.equals(rightType)) {
            return leftType;
        } else {
            errors.addError(new TypeException(minus.line, rightType, leftType));
        }
        return null;
    }

    @Override
    public String visit(Division div, Tds tds) {
        String leftType = div.left.accept(this, tds);
        String rightType = div.right.accept(this, tds);
        if (leftType.equals(rightType)) {
            return leftType;
        } else {
            errors.addError(new TypeException(div.line, rightType, leftType));
        }
        return null;
    }

    @Override
    public String visit(Multiplication mult, Tds tds) {
        String leftType = mult.left.accept(this, tds);
        String rightType = mult.right.accept(this, tds);
        if (leftType.equals(rightType)) {
            return leftType;
        } else {
            errors.addError(new TypeException(mult.line, rightType, leftType));
        }
        return null;
    }

    @Override
    public String visit(Fleche fleche, Tds tds) {
        String leftType = fleche.left.accept(this, tds);
        Tds tdsStruct = tds.findSymboleStruct(leftType).getTds();

        String rightType = fleche.right.accept(this, tdsStruct);
        if (rightType != null) {
            return rightType;
        } else {
            errors.addError(new TypeException(fleche.line, rightType, leftType));
        }
        return null;
    }

    @Override
    public String visit(MoinsUnaire unaire, Tds tds) {
        return unaire.noeud.accept(this, tds);
    }

    @Override
    public String visit(Negation unaire, Tds tds) {
        return unaire.noeud.accept(this, tds);
    }

    @Override
    public String visit(Semicolon semicolon, Tds tds) {
        // TODO Auto-generated method stub
        return null;
    }

}
