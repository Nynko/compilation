package compilateur.ARMGenerator;

import compilateur.ast.Affectation;
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

public class ARMGenerator implements ARMVisitor<String> {

    private StringAggregator stringAggregator;

    public ARMGenerator(){
        stringAggregator = new StringAggregator();
    }

    @Override
    public String visit(Fichier fichier) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(Idf idf) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(DeclVarInt declVarInt) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(DeclVarStruct declVarStruct) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(Decl_typ decl_typ) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(DeclFctInt declFctInt) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(DeclFctStruct declFctStruct) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(ParamListMulti paramListMulti) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(ParamInt paramInt) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(ParamStruct paramStruct) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(Sizeof sizeof) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(IdfParenthesis idfParenthesis) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(IdfParenthesisEmpty idfParenthesisEmpty) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(IfThen ifThen) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(IfThenElse ifThenElse) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(While while1) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(Return return1) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(Bloc bloc) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(CharNode charNode) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(IntNode intNode) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(Affectation affectation) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(Fleche fleche) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(MoinsUnaire unaire) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(Negation unaire) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(Semicolon semicolon) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(Expr_ou expr_ou) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(Expr_et expr_et) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(Egal egal) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(Different dif) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(Inferieur inf) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(InferieurEgal infEgal) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(Superieur sup) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(SuperieurEgal supEgal) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(Plus plus) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(Minus minus) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(Division div) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(Multiplication mult) {
        // TODO Auto-generated method stub
        return null;
    }


    
}
