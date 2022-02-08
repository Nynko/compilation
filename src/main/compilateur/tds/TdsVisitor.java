package compilateur.tds;

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


public interface TdsVisitor<T>{

    public T visit(Fichier fichier, Tds tds);
    public T visit(Idf idf, Tds tds);
    public T visit(DeclVarInt declVarInt, Tds tds);
    public T visit(DeclVarStruct declVarStruct, Tds tds);
    public T visit(Decl_typ decl_typ, Tds tds);
    public T visit(DeclFctInt declFctInt, Tds tds);
    public T visit(DeclFctStruct declFctStruct, Tds tds);
    public T visit(ParamListMulti paramListMulti, Tds tds);
    public T visit(ParamInt paramInt, Tds tds);
    public T visit(ParamStruct paramStruct, Tds tds);
    public T visit(Sizeof sizeof, Tds tds);
    public T visit(IdfParenthesis idfParenthesis, Tds tds);
    public T visit(IdfParenthesisEmpty idfParenthesisEmpty, Tds tds);
    public T visit(IfThen ifThen, Tds tds);
    public T visit(IfThenElse ifThenElse, Tds tds);
    public T visit(While while1, Tds tds);
    public T visit(Return return1, Tds tds);
    public T visit(Bloc bloc, Tds tds);
    public T visit(CharNode charNode, Tds tds);
    public T visit(IntNode intNode, Tds tds);
    public T visit(Affectation affectation, Tds tds);
    public T visit(Fleche fleche, Tds tds);
    public T visit(MoinsUnaire unaire, Tds tds);
    public T visit(Negation unaire, Tds tds);
    public T visit(Semicolon semicolon, Tds tds);
    public T visit(Operateur operateur, Tds tds);

    public static final String TYPESTRUCT = "struct_";
    
}
