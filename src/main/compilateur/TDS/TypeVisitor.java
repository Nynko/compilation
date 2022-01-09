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
        } else if (sym instanceof SymboleInt symint) {
            // System.out.println("===== " + symint.getName()+ " " + idf.line);
            // if (!symint.isInitalized()) {
            //     this.errors.addError(new VarNotInitializedException(idf.line, idf.name, symint.getDefinitionLine()));
            // }
            return "int";
        } else if (sym instanceof SymboleStruct symstruct) {
            
            // System.out.println("------- " + symstruct.getName()+ " " + idf.line);
            
            // if (!symstruct.isInitalized()) {
            //     this.errors.addError(new VarNotInitializedException(idf.line, idf.name, symstruct.getDefinitionLine()));
            // }
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
        try {
            int nbParam = idfParenthesis.exprList.size();
            String fctName = ((Idf) idfParenthesis.idf).name;
            SymboleFonction sym = (SymboleFonction) tds.findSymbole(fctName);
            ArrayList<SymboleVar> param = sym.getTds().getParams();
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
                    paramTypeRef = "struct_" + symstruct.getStruct().getName();
                }
                String paramType = idfParenthesis.exprList.get(i).accept(this, tds);
                if (!paramType.equals(paramTypeRef)) {
                    this.errors.addError(new TypeException(idfParenthesis.line, paramType, paramTypeRef));
                }
            }
        } catch (Exception e) {
        }
        return idfParenthesis.idf.accept(this, tds);
    }

    @Override
    public String visit(IdfParenthesisEmpty idfParenthesisEmpty, Tds tds) {
        try {
            String fctName = ((Idf) idfParenthesisEmpty.idf).name;
            SymboleFonction sym = (SymboleFonction) tds.findSymbole(fctName);
            if (!sym.getTds().getParams().isEmpty()) {
                this.errors.addError(new NumberParameterException(sym.getName(), idfParenthesisEmpty.line,
                        sym.getTds().getParams().size(), 0));
            }
        } catch (Exception e) {
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
        if (rightType == null){
            this.errors.addError(new BadOperandTypeException("affectation", affectation.line));
            return null;
        }
        // membre droit est initialisé ?
        if (affectation.right instanceof Idf idf) { // si c'est un idf
            Symbole s = tds.findSymbole(idf.name);
            System.out.println(s.getName() + " " + affectation.line);
            if (s instanceof SymboleVar sv && !sv.isInitalized()) {
                this.errors.addError(new VarNotInitializedException(affectation.line, sv.getName(), sv.getDefinitionLine()));
                return leftType;
            }
        }
        
        if (leftType.equals(rightType) || (rightType.equals("void_*") && leftType.startsWith("struct_"))) {
            
            // affectation reussie
            if (affectation.left instanceof Idf idf) {
                Symbole s = tds.findSymbole(idf.name);
                if (s instanceof SymboleVar sv) {
                    sv.setInitalized(true);
                }
            } else if (affectation.left instanceof Fleche fleche) {
                Idf idf = (Idf)fleche.right;
                Symbole s = tds.findSymbole(idf.name);
                if (s instanceof SymboleVar sv) {
                    sv.setInitalized(true);
                }
            }
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
        if (rightType != null && expr_ou.right instanceof Idf idf) { // si a droite c'est un idf
            Symbole s = tds.findSymbole(idf.name);
            if (s instanceof SymboleVar sv && !sv.isInitalized()) {
                this.errors.addError(new VarNotInitializedException(expr_ou.line, sv.getName(), sv.getDefinitionLine()));
                return null;
            }
        }
        if (leftType != null && expr_ou.left instanceof Idf idf) { // si a gauche c'est un idf
            Symbole s = tds.findSymbole(idf.name);
            if (s instanceof SymboleVar sv && !sv.isInitalized()) {
                this.errors.addError(new VarNotInitializedException(expr_ou.line, sv.getName(), sv.getDefinitionLine()));
                return null;
            }
        }

        if (leftType != null && rightType != null) {
            if (leftType.equals(rightType)) {
                return leftType;
            } else {
                errors.addError(new TypeException(expr_ou.line, rightType, leftType));
            }
        }
        return null;
    }

    @Override
    public String visit(Expr_et expr_et, Tds tds) {
        String leftType = expr_et.left.accept(this, tds);
        String rightType = expr_et.right.accept(this, tds);
        if (rightType != null && expr_et.right instanceof Idf idf) { // si a droite c'est un idf
            Symbole s = tds.findSymbole(idf.name);
            if (s instanceof SymboleVar sv && !sv.isInitalized()) {
                this.errors.addError(new VarNotInitializedException(expr_et.line, sv.getName(), sv.getDefinitionLine()));
                return null;
            }
        }
        if (leftType != null && expr_et.left instanceof Idf idf) { // si a gauche c'est un idf
            Symbole s = tds.findSymbole(idf.name);
            if (s instanceof SymboleVar sv && !sv.isInitalized()) {
                this.errors.addError(new VarNotInitializedException(expr_et.line, sv.getName(), sv.getDefinitionLine()));
                return null;
            }
        }
        if (leftType != null && rightType != null) {
            if (leftType.equals(rightType)) {
                return leftType;
            } else {
                errors.addError(new TypeException(expr_et.line, rightType, leftType));
            }
        }
        return null;
    }

    @Override
    public String visit(Egal egal, Tds tds) {
        String leftType = egal.left.accept(this, tds);
        String rightType = egal.right.accept(this, tds);
        if (rightType != null && egal.right instanceof Idf idf) { // si a droite c'est un idf
            Symbole s = tds.findSymbole(idf.name);
            if (s instanceof SymboleVar sv && !sv.isInitalized()) {
                this.errors.addError(new VarNotInitializedException(egal.line, sv.getName(), sv.getDefinitionLine()));
                return null;
            }
        }
        if (leftType != null && egal.left instanceof Idf idf) { // si a gauche c'est un idf
            Symbole s = tds.findSymbole(idf.name);
            if (s instanceof SymboleVar sv && !sv.isInitalized()) {
                this.errors.addError(new VarNotInitializedException(egal.line, sv.getName(), sv.getDefinitionLine()));
                return null;
            }
        }
        if (leftType != null && rightType != null) {
            if (leftType.equals(rightType)) {
                return leftType;
            } else {
                errors.addError(new TypeException(egal.line, rightType, leftType));
            }
        }
        return null;
    }

    @Override
    public String visit(Different dif, Tds tds) {
        String leftType = dif.left.accept(this, tds);
        String rightType = dif.right.accept(this, tds);
        if (rightType != null && dif.right instanceof Idf idf) { // si a droite c'est un idf
            Symbole s = tds.findSymbole(idf.name);
            if (s instanceof SymboleVar sv && !sv.isInitalized()) {
                this.errors.addError(new VarNotInitializedException(dif.line, sv.getName(), sv.getDefinitionLine()));
                return null;
            }
        }
        if (leftType != null && dif.left instanceof Idf idf) { // si a gauche c'est un idf
            Symbole s = tds.findSymbole(idf.name);
            if (s instanceof SymboleVar sv && !sv.isInitalized()) {
                this.errors.addError(new VarNotInitializedException(dif.line, sv.getName(), sv.getDefinitionLine()));
                return null;
            }
        }
        if (leftType != null && rightType != null) {
            if (leftType.equals(rightType)) {
                return "int";
            } else {
                errors.addError(new TypeException(dif.line, rightType, leftType));
            }
        }
        return null;
    }

    @Override
    public String visit(Inferieur inf, Tds tds) {
        String leftType = inf.left.accept(this, tds);
        String rightType = inf.right.accept(this, tds);
        if (rightType != null && inf.right instanceof Idf idf) { // si a droite c'est un idf
            Symbole s = tds.findSymbole(idf.name);
            if (s instanceof SymboleVar sv && !sv.isInitalized()) {
                this.errors.addError(new VarNotInitializedException(inf.line, sv.getName(), sv.getDefinitionLine()));
                return null;
            }
        }
        if (leftType != null && inf.left instanceof Idf idf) { // si a gauche c'est un idf
            Symbole s = tds.findSymbole(idf.name);
            if (s instanceof SymboleVar sv && !sv.isInitalized()) {
                this.errors.addError(new VarNotInitializedException(inf.line, sv.getName(), sv.getDefinitionLine()));
                return null;
            }
        }
        if (leftType != null && rightType != null) {
            if (leftType.equals(rightType)) {
                return "int";
            } else {
                errors.addError(new TypeException(inf.line, rightType, leftType));
            }
        }
        return null;
    }

    @Override
    public String visit(InferieurEgal infEgal, Tds tds) {
        String leftType = infEgal.left.accept(this, tds);
        String rightType = infEgal.right.accept(this, tds);
        if (rightType != null && infEgal.right instanceof Idf idf) { // si a droite c'est un idf
            Symbole s = tds.findSymbole(idf.name);
            if (s instanceof SymboleVar sv && !sv.isInitalized()) {
                this.errors.addError(new VarNotInitializedException(infEgal.line, sv.getName(), sv.getDefinitionLine()));
                return null;
            }
        }
        if (leftType != null && infEgal.left instanceof Idf idf) { // si a gauche c'est un idf
            Symbole s = tds.findSymbole(idf.name);
            if (s instanceof SymboleVar sv && !sv.isInitalized()) {
                this.errors.addError(new VarNotInitializedException(infEgal.line, sv.getName(), sv.getDefinitionLine()));
                return null;
            }
        }
        if (leftType != null && rightType != null) {
            if (leftType.equals(rightType)) {
                return "int";
            } else {
                errors.addError(new TypeException(infEgal.line, rightType, leftType));
            }
        }
        return null;
    }

    @Override
    public String visit(Superieur sup, Tds tds) {
        String leftType = sup.left.accept(this, tds);
        String rightType = sup.right.accept(this, tds);
        if (rightType != null && sup.right instanceof Idf idf) { // si a droite c'est un idf
            Symbole s = tds.findSymbole(idf.name);
            if (s instanceof SymboleVar sv && !sv.isInitalized()) {
                this.errors.addError(new VarNotInitializedException(sup.line, sv.getName(), sv.getDefinitionLine()));
                return null;
            }
        }
        if (leftType != null && sup.left instanceof Idf idf) { // si a gauche c'est un idf
            Symbole s = tds.findSymbole(idf.name);
            if (s instanceof SymboleVar sv && !sv.isInitalized()) {
                this.errors.addError(new VarNotInitializedException(sup.line, sv.getName(), sv.getDefinitionLine()));
                return null;
            }
        }
        if (leftType != null && rightType != null) {
            if (leftType.equals(rightType)) {
                return "int";
            } else {
                errors.addError(new TypeException(sup.line, rightType, leftType));
            }
        }
        return null;
    }

    @Override
    public String visit(SuperieurEgal supEgal, Tds tds) {
        String leftType = supEgal.left.accept(this, tds);
        String rightType = supEgal.right.accept(this, tds);
        if (rightType != null && supEgal.right instanceof Idf idf) { // si a droite c'est un idf
            Symbole s = tds.findSymbole(idf.name);
            if (s instanceof SymboleVar sv && !sv.isInitalized()) {
                this.errors.addError(new VarNotInitializedException(supEgal.line, sv.getName(), sv.getDefinitionLine()));
                return null;
            }
        }
        if (leftType != null && supEgal.left instanceof Idf idf) { // si a gauche c'est un idf
            Symbole s = tds.findSymbole(idf.name);
            if (s instanceof SymboleVar sv && !sv.isInitalized()) {
                this.errors.addError(new VarNotInitializedException(supEgal.line, sv.getName(), sv.getDefinitionLine()));
                return null;
            }
        }
        if (leftType != null && rightType != null) {
            if (leftType.equals(rightType)) {
                return "int";
            } else {
                errors.addError(new TypeException(supEgal.line, rightType, leftType));
            }
        }
        return null;
    }

    @Override
    public String visit(Plus plus, Tds tds) {
        String leftType = plus.left.accept(this, tds);
        String rightType = plus.right.accept(this, tds);
        if (rightType != null && plus.right instanceof Idf idf) { // si a droite c'est un idf
            Symbole s = tds.findSymbole(idf.name);
            if (s instanceof SymboleVar sv && !sv.isInitalized()) {
                this.errors.addError(new VarNotInitializedException(plus.line, sv.getName(), sv.getDefinitionLine()));
                return null;
            }
        }
        if (leftType != null && plus.left instanceof Idf idf) { // si a gauche c'est un idf
            Symbole s = tds.findSymbole(idf.name);
            if (s instanceof SymboleVar sv && !sv.isInitalized()) {
                this.errors.addError(new VarNotInitializedException(plus.line, sv.getName(), sv.getDefinitionLine()));
                return null;
            }
        }
        if (leftType != null && rightType != null) {
            if (leftType.equals(rightType)) {
                return leftType;
            } else {
                errors.addError(new TypeException(plus.line, rightType, leftType));
            }
        }
        return null;
    }

    @Override
    public String visit(Minus minus, Tds tds) {
        String leftType = minus.left.accept(this, tds);
        String rightType = minus.right.accept(this, tds);
        if (rightType != null && minus.right instanceof Idf idf) { // si a droite c'est un idf
            Symbole s = tds.findSymbole(idf.name);
            if (s instanceof SymboleVar sv && !sv.isInitalized()) {
                this.errors.addError(new VarNotInitializedException(minus.line, sv.getName(), sv.getDefinitionLine()));
                return null;
            }
        }
        if (leftType != null && minus.left instanceof Idf idf) { // si a gauche c'est un idf
            Symbole s = tds.findSymbole(idf.name);
            if (s instanceof SymboleVar sv && !sv.isInitalized()) {
                this.errors.addError(new VarNotInitializedException(minus.line, sv.getName(), sv.getDefinitionLine()));
                return null;
            }
        }
        if (leftType != null && rightType != null) {
            if (leftType.equals(rightType)) {
                return leftType;
            } else {
                errors.addError(new TypeException(minus.line, rightType, leftType));
            }
        }
        return null;
    }

    @Override
    public String visit(Division div, Tds tds) {
        String leftType = div.left.accept(this, tds);
        String rightType = div.right.accept(this, tds);
        if (rightType != null && div.right instanceof Idf idf) { // si a droite c'est un idf
            Symbole s = tds.findSymbole(idf.name);
            if (s instanceof SymboleVar sv && !sv.isInitalized()) {
                this.errors.addError(new VarNotInitializedException(div.line, sv.getName(), sv.getDefinitionLine()));
                return null;
            }
        }
        if (leftType != null && div.left instanceof Idf idf) { // si a gauche c'est un idf
            Symbole s = tds.findSymbole(idf.name);
            if (s instanceof SymboleVar sv && !sv.isInitalized()) {
                this.errors.addError(new VarNotInitializedException(div.line, sv.getName(), sv.getDefinitionLine()));
                return null;
            }
        }
        if (div.right instanceof IntNode intr && intr.parseInt == 0) {
            this.errors.addError(new DivisionByZeroException(div.line));
        }
        if (leftType != null && rightType != null) {
            if (leftType.equals(rightType)) {
                return leftType;
            } else {
                errors.addError(new TypeException(div.line, rightType, leftType));
            }
        }
        return null;
    }

    @Override
    public String visit(Multiplication mult, Tds tds) {
        String leftType = mult.left.accept(this, tds);
        String rightType = mult.right.accept(this, tds);
        if (rightType != null && mult.right instanceof Idf idf) { // si a droite c'est un idf
            Symbole s = tds.findSymbole(idf.name);
            if (s instanceof SymboleVar sv && !sv.isInitalized()) {
                this.errors.addError(new VarNotInitializedException(mult.line, sv.getName(), sv.getDefinitionLine()));
                return null;
            }
        }
        if (leftType != null && mult.left instanceof Idf idf) { // si a gauche c'est un idf
            Symbole s = tds.findSymbole(idf.name);
            if (s instanceof SymboleVar sv && !sv.isInitalized()) {
                this.errors.addError(new VarNotInitializedException(mult.line, sv.getName(), sv.getDefinitionLine()));
                return null;
            }
        }
        if (leftType != null && rightType != null) {
            if (leftType.equals(rightType)) {
                return leftType;
            } else {
                errors.addError(new TypeException(mult.line, rightType, leftType));
            }
        }
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
        if (rightType != null && fleche.right instanceof Idf idf) { // si a droite c'est un idf
            Symbole s = tds.findSymbole(idf.name);
            if (s instanceof SymboleVar sv && !sv.isInitalized()) {
                this.errors.addError(new VarNotInitializedException(fleche.line, sv.getName(), sv.getDefinitionLine()));
                return null;
            }
        }
        if (leftType != null && fleche.left instanceof Idf idf) { // si a gauche c'est un idf
            Symbole s = tds.findSymbole(idf.name);
            if (s instanceof SymboleVar sv && !sv.isInitalized()) {
                this.errors.addError(new VarNotInitializedException(fleche.line, sv.getName(), sv.getDefinitionLine()));
                return null;
            }
        }
        if (rightType != null) {
            return rightType;
        }
        return null;
    }

    @Override
    public String visit(MoinsUnaire unaire, Tds tds) {
        String type = unaire.noeud.accept(this, tds);
        if (type != null && unaire.noeud instanceof Idf idf) { // si noeud est un idf
            Symbole s = tds.findSymbole(idf.name);
            if (s instanceof SymboleVar sv && !sv.isInitalized()) {
                this.errors.addError(new VarNotInitializedException(unaire.line, sv.getName(), sv.getDefinitionLine()));
                return null;
            }
        }
        if (!type.equals("int")) {
            this.errors.addError(new UnauthorizedOperationException(unaire.line));
            return null;
        }
        return type;
    }

    @Override
    public String visit(Negation unaire, Tds tds) {
        String type = unaire.noeud.accept(this, tds);
        if (type != null && unaire.noeud instanceof Idf idf) { // si noeud est un idf
            Symbole s = tds.findSymbole(idf.name);
            if (s instanceof SymboleVar sv && !sv.isInitalized()) {
                this.errors.addError(new VarNotInitializedException(unaire.line, sv.getName(), sv.getDefinitionLine()));
                return null;
            }
        }
        if (type == null) {
            this.errors.addError(new UnauthorizedOperationException(unaire.line));
            return null;
        }
        return "int";
    }

    @Override
    public String visit(Semicolon semicolon, Tds tds) {
        return null;
    }

}
