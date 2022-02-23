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
        StringAggregator str = new StringAggregator();
        // On ajoute le nom de la fonction pour pouvoir faire le jump
        str.appendLine(declFctInt.Idf.toString());
        // Sauvegarde du pointeur de base
        str.appendLine("MOV		R11, R13");
        // Sauvegarde de l'adresse de retour
        str.appendLine("STR		LR, [R13, #4]!");
        
        String blocContent = declFctInt.bloc.accept(this);

        str.appendLine(blocContent);
        
        
        // Remise du pointeur de pile à sa position avant l'appel de fonction
        str.appendLine("MOV		R13, R11");
        int numParams = declFctInt.getTds().getParams().size();
        str.appendFormattedLine("SUB		R13, R13, #%d", numParams*4);
        // Récupération de l'addresse de retour et retour à l'appelant
        str.appendLine("LDR		PC, [R11, #4]");
        return str.getString();
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

    public String startCmp(Comparaison cmp, StringAggregator str) {
        cmp.left.accept(this);
        //récup le registre depuis r0 dans le premier registre libre
        cmp.right.accept(this);
        //same 
        str.appendLine("CMP"); //TODO ajouter les registres
        str.appendLine("MOV R0, #0");
        return str.getString();
    }

    @Override
    public String visit(Egal egal) {
        StringAggregator str = new StringAggregator();
        startCmp(egal, str);
        str.appendLine("MOVEQ R0, #1");
        return str.getString();
    }

    @Override
    public String visit(Different dif) {
        StringAggregator str = new StringAggregator();
        startCmp(dif, str);
        str.appendLine("MOVNE R0, #1");
        return str.getString();
    }
    

    @Override
    public String visit(Inferieur inf) {
        StringAggregator str = new StringAggregator();
        startCmp(inf, str);
        str.appendLine("MOVLT R0, #1");
        return str.getString();
    }

    @Override
    public String visit(InferieurEgal infEgal) {
        StringAggregator str = new StringAggregator();
        startCmp(infEgal, str);
        str.appendLine("MOVLE R0, #1");
        return str.getString();
    }

    @Override
    public String visit(Superieur sup) {
        StringAggregator str = new StringAggregator();
        startCmp(sup, str);
        str.appendLine("MOVGT R0, #1");
        return str.getString();
    }

    @Override
    public String visit(SuperieurEgal supEgal) {
        StringAggregator str = new StringAggregator();
        startCmp(supEgal, str);
        str.appendLine("MOVGE R0, #1");
        return str.getString();
    }

    @Override
    public String visit(Plus plus) {
        StringAggregator str = new StringAggregator();
        plus.left.accept(this);
        // TODO save R1
        str.appendLine("MOVE R1, R0");
        plus.right.accept(this);
        str.appendLine("ADD R0, R0, R1");
        // TODO recup R1
        return str.getString();
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
