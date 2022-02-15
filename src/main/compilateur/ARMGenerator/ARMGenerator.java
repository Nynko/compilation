package compilateur.ARMGenerator;

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
import compilateur.tds.Tds;

public class ARMGenerator implements ARMVisitor<String> {

    private StringAggregator stringAggregator;

    private int whileCompt = 0;
    private int ifCompt = 0;

    public ARMGenerator(){
        stringAggregator = new StringAggregator();
    }

    private int getWhileIncr(){
        int whileInt = whileCompt;
        whileCompt ++;
        return whileInt;
    }

    private int getIfIncr(){
        int ifInt = ifCompt;
        ifCompt ++;
        return ifInt;
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
        StringAggregator str = new StringAggregator();
        String ifNum = Integer.toString(getIfIncr());
        str.appendLine(";if" + ifNum); // Commentaire pour debug
        str.appendLine(ifThen.condition.accept(this, tds));
        str.appendLine("BEQ _finIf" + ifNum);
        str.appendLine(ifThen.thenBlock.accept(this, tds));
        str.appendLine("_finIf" + ifNum);
        str.appendLine();
        return str.getString();
    }

    @Override
    public String visit(IfThenElse ifThenElse, Tds tds) {
        StringAggregator str = new StringAggregator();
        String ifNum = Integer.toString(getIfIncr());
        str.appendLine(";ifThenElse" + ifNum); // Commentaire pour debug
        str.appendLine(ifThenElse.condition.accept(this, tds));
        str.appendLine("BEQ _else" + ifNum);
        str.appendLine(ifThenElse.thenBlock.accept(this, tds));
        str.appendLine("B " + "_finIf" + ifNum);
        str.appendLine("_else" + ifNum);
        str.appendLine(ifThenElse.elseBlock.accept(this, tds));
        str.appendLine("_finIf" + ifNum);
        str.appendLine();
        return str.getString();
    }

    @Override
    public String visit(While while1, Tds tds) {
        StringAggregator str = new StringAggregator();
        String whileNum = Integer.toString(getWhileIncr());
        str.appendLine(";while" + whileNum); // Commentaire pour debug
        str.appendLine(while1.condition.accept(this, tds));
        str.appendLine("BEQ _finWhile" + whileNum);
        str.appendLine(while1.doBlock.accept(this, tds));
        str.appendLine("B " + "_while" +  whileNum);
        str.appendLine("_finWhile" + whileNum);
        str.appendLine();
        return str.getString();
    }

    @Override
    public String visit(Return return1, Tds tds) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(Bloc bloc, Tds tds) {
        // TODO 
        StringAggregator str = new StringAggregator();
        for(Ast instruction : bloc.instList){
            str.appendString(instruction.accept(this, tds));
        }
        
        return str.getString();
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

    @Override
    public String visit(Egal egal, Tds tds) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(Different dif, Tds tds) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(Inferieur inf, Tds tds) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(InferieurEgal infEgal, Tds tds) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(Superieur sup, Tds tds) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(SuperieurEgal supEgal, Tds tds) {
        // TODO Auto-generated method stub
        return null;
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
