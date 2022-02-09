package compilateur.ARMGenerator;

import compilateur.ast.Affectation;
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
import compilateur.tds.Tds;
import compilateur.tds.TdsVisitor;

public class ARMGenerator implements TdsVisitor<String> {

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
    public String visit(Operateur operateur, Tds tds) {
        // TODO Auto-generated method stub
        return null;
    }


    
}
