package compilateur.ARMGenerator;

import compilateur.ast.Affectation;
import compilateur.ast.Bloc;
import compilateur.ast.CharNode;
import compilateur.ast.Comparaison;
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
import compilateur.tds.Tds;

public class ARMGenerator implements ARMVisitor<String> {

    private StringAggregator stringAggregator;

    public ARMGenerator(){
        stringAggregator = new StringAggregator();
    }

    @Override
    public String visit(Fichier fichier, Tds tds) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(Idf idf, Tds tds) {
        // TODO Auto-generated method stub
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
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(ParamStruct paramStruct, Tds tds) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(Sizeof sizeof, Tds tds) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(IdfParenthesis idfParenthesis, Tds tds) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(IdfParenthesisEmpty idfParenthesisEmpty, Tds tds) {
        // TODO Auto-generated method stub
        return null;
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
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(Bloc bloc, Tds tds) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(CharNode charNode, Tds tds) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(IntNode intNode, Tds tds) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(Affectation affectation, Tds tds) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(Fleche fleche, Tds tds) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(MoinsUnaire unaire, Tds tds) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(Negation unaire, Tds tds) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(Semicolon semicolon, Tds tds) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(Expr_ou expr_ou, Tds tds) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(Expr_et expr_et, Tds tds) {
        // TODO Auto-generated method stub
        return null;
    }

    public String startCmp(Comparaison cmp, StringAggregator str, Tds tds) {
        cmp.left.accept(this, tds);
        //récup le registre depuis r0 dans le premier registre libre
        cmp.right.accept(this, tds);
        //same 
        str.appendLine("CMP"); //TODO ajouter les registres
        str.appendLine("MOV R0, #0");
        return str.getString();
    }

    @Override
    public String visit(Egal egal, Tds tds) {
        StringAggregator str = new StringAggregator();
        startCmp(egal, str, tds);
        str.appendLine("MOVEQ R0, #1");
        return str.getString();
    }

    @Override
    public String visit(Different dif, Tds tds) {
        StringAggregator str = new StringAggregator();
        startCmp(dif, str, tds);
        str.appendLine("MOVNE R0, #1");
        return str.getString();
    }
    

    @Override
    public String visit(Inferieur inf, Tds tds) {
        StringAggregator str = new StringAggregator();
        startCmp(inf, str, tds);
        str.appendLine("MOVLT R0, #1");
        return str.getString();
    }

    @Override
    public String visit(InferieurEgal infEgal, Tds tds) {
        StringAggregator str = new StringAggregator();
        startCmp(infEgal, str, tds);
        str.appendLine("MOVLE R0, #1");
        return str.getString();
    }

    @Override
    public String visit(Superieur sup, Tds tds) {
        StringAggregator str = new StringAggregator();
        startCmp(sup, str, tds);
        str.appendLine("MOVGT R0, #1");
        return str.getString();
    }

    @Override
    public String visit(SuperieurEgal supEgal, Tds tds) {
        StringAggregator str = new StringAggregator();
        startCmp(supEgal, str, tds);
        str.appendLine("MOVGE R0, #1");
        return str.getString();
    }

    @Override
    public String visit(Plus plus, Tds tds) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(Minus minus, Tds tds) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(Division div, Tds tds) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(Multiplication mult, Tds tds) {
        // TODO Auto-generated method stub
        return null;
    }


    
}
