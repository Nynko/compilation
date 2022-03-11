package compilateur.ARMGenerator;

import java.util.ArrayList;

import compilateur.ast.Affectation;
import compilateur.ast.Ast;
import compilateur.ast.AstVisitor;
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
import compilateur.tds.Symbole;
import compilateur.tds.SymboleVar;
import compilateur.tds.Tds;

public class ARMGenerator implements AstVisitor<String> {

    /**
     * Informations/Conventions:
     * Full Ascending: SP pointe vers une case "pleine" et SP augmente avec les
     * adresses.
     * R0 : Adresse de retour
     */

    public static final int WORD_SIZE = 4; // Taille d'un mot en octet

    private int whileCompt = 0;
    private int ifCompt = 0;
    private boolean mul = false;
    private boolean division = false;

    private int AdresseInitStack = 0xFF000000;

    public ARMGenerator() {

    }

    private int getWhileIncr() {
        int whileInt = whileCompt;
        whileCompt++;
        return whileInt;
    }

    private int getIfIncr() {
        int ifInt = ifCompt;
        ifCompt++;
        return ifInt;
    }

    // |<- R13
    // |var loc|
    // |@stat |
    // |@dyn |
    // |@r | <- R11
    // |param |
    @Override
    public String visit(Idf idf) {
        StringAggregator str = new StringAggregator();
        String bp = "R11";
        int decalage = 0;
        int imbrication = idf.getTds().findImbrication(idf.name);
        // TODO diplay[imbrication] ou remonter tds.getImbrication() - imbrication
        // chainage statique pour mettre a jour bp
        // chainage statique
        if (idf.getTds().getImbrication() - imbrication != 0) {
            str.appendLine("MOV [R11] , R0");
            str.appendLine("ADD R0, R0, #8");
            for (int i = 0; i < idf.getTds().getImbrication() - imbrication - 1; i++) {
                str.appendLine("MOV [R0], R0");
            }
            bp = "R0";
        }
        Symbole s = idf.getTds().findSymbole(idf.name);
        if (s instanceof SymboleVar sv) {
            decalage = sv.getDeplacement();
        }
        str.appendFormattedLine("LDR R0, [%s, #%d]", bp, decalage);

        return str.getString();
    }

    @Override
    public String visit(Fichier fichier) {
        StringAggregator str = new StringAggregator();

        // Initialisation
        str.appendLine("BL _main");
        str.appendLine("B __end__");

        for (Ast ast : fichier.instructions) {
            String code = ast.accept(this);
            str.appendLine(code);
        }

        // Ajout de la macro de sauvegarde des registres
        str.appendLine("__save_reg__");
        str.appendLine("\t\tSTMFA	R13!, {R1-R12}");
        str.appendLine("\t\tMOV		PC, LR");
        str.appendLine();

        // Ajout de la macro de restauration des registres
        str.appendLine("__restore_reg__");
        str.appendLine("\t\tLDMFA	R13!, {R1-R12}");
        str.appendLine("\t\tMOV		PC, LR");
        str.appendLine();

        // On place un label end à la fin de programme pour le quitter
        // (l'instruction END n'est pas reconnue par le vrai ARM)
        str.appendLine("__end__");

        return str.getString();
    }

    @Override
    public String visit(DeclVarInt declVarInt) {
        // TODO Auto-generated method stub
        return "";
    }

    @Override
    public String visit(DeclVarStruct declVarStruct) {
        // TODO Auto-generated method stub
        return "";
    }

    @Override
    public String visit(Decl_typ decl_typ) {
        // TODO Auto-generated method stub
        return "";
    }

    @Override
    public String visit(DeclFctInt declFctInt) {
        StringAggregator str = new StringAggregator();
        // On ajoute le nom de la fonction pour pouvoir faire le jump
        str.appendFormattedLine("_%s", ((Idf) declFctInt.Idf).name);
        // Sauvegarde de l'adresse de retour
        str.appendLine("STR		LR, [R13, #4]!");
        // On sauvegarde temporairement l'ancien pointeur de base dans R1
        str.appendLine("MOV		R1, R11");
        // On met le nouveau pointeur de base dans R11
        str.appendLine("MOV		R11, R13");
        // Sauvegarde de l'ancien pointeur de base (chaînage dynamique)
        str.appendLine("STR		R1, [R13, #4]!");

        String blocContent = declFctInt.bloc.accept(this);

        str.appendLine(blocContent);

        // Remise du pointeur de pile à sa position avant l'appel de fonction
        str.appendLine("MOV		R13, R11");
        str.appendLine("SUB		R13, R13, #4");
        int numParams = declFctInt.getTds().getParams().size();
        str.appendFormattedLine("SUB		R13, R13, #%d", numParams * 4);
        // Récupération de l'addresse de retour et retour à l'appelant
        str.appendLine("LDR		PC, [R11]");
        return str.getString();
    }

    @Override
    public String visit(DeclFctStruct declFctStruct) {
        StringAggregator str = new StringAggregator();
        // On ajoute le nom de la fonction pour pouvoir faire le jump
        str.appendFormattedLine("_%s", ((Idf) declFctStruct.Idf1).name);

        // Sauvegarde de l'adresse de retour
        str.appendLine("STR		LR, [R13, #4]!");
        // On sauvegarde temporairement l'ancien pointeur de base dans R1
        str.appendLine("MOV		R1, R11");
        // On met le nouveau pointeur de base dans R11
        str.appendLine("MOV		R11, R13");
        // Sauvegarde de l'ancien pointeur de base (chaînage dynamique)
        str.appendLine("STR		R1, [R13, #4]!");

        String blocContent = declFctStruct.bloc.accept(this);

        str.appendLine(blocContent);

        // Remise du pointeur de pile à sa position avant l'appel de fonction
        str.appendLine("MOV		R13, R11");
        str.appendLine("SUB		R13, R13, #4");
        int numParams = declFctStruct.getTds().getParams().size();
        str.appendFormattedLine("SUB		R13, R13, #%d", numParams * 4);
        // Récupération de l'addresse de retour et retour à l'appelant
        str.appendLine("LDR		PC, [R11]");
        return str.getString();
    }

    @Override
    public String visit(ParamListMulti paramListMulti) {
        // TODO Auto-generated method stub
        return "";
    }

    @Override
    public String visit(ParamInt paramInt) {
        // TODO Auto-generated method stub
        return "";
    }

    @Override
    public String visit(ParamStruct paramStruct) {
        // TODO Auto-generated method stub
        return "";
    }

    @Override
    public String visit(Sizeof sizeof) {
        // TODO Auto-generated method stub
        return "";
    }

    @Override
    public String visit(IdfParenthesis idfParenthesis) {
        StringAggregator str = new StringAggregator();

        // On s'assure que R13 pointe sur le maximum de son déplacement
        str.appendFormattedLine("ADD        R1, R11, #%d", idfParenthesis.getTds().getDeplacement());
        str.appendLine("MOV        R13, R1");

        // Sauvegarde des registres
        str.appendLine("BL		__save_reg__");

        // Ajout des parametres à la pile
        for (Ast param : idfParenthesis.exprList) {
            str.appendLine(param.accept(this));
            // Putting R0 in the stack
            str.appendLine("STR		R0, [R13, #4]!");
        }

        // Appel de la fonction
        str.appendFormattedLine("BL 		_%s", ((Idf) idfParenthesis.idf).name);

        // Restauration des registres
        str.appendLine("BL		__restore_reg__");

        return str.getString();
    }

    @Override
    public String visit(IdfParenthesisEmpty idfParenthesisEmpty) {
        StringAggregator str = new StringAggregator();

        // On s'assure que R13 pointe sur le maximum de son déplacement
        str.appendFormattedLine("ADD        R1, R11, #%d", idfParenthesisEmpty.getTds().getDeplacement());
        str.appendLine("MOV        R13, R1");

        // Sauvegarde des registres
        str.appendLine("BL		__save_reg__");

        // Appel de la fonction
        str.appendFormattedLine("BL 		_%s", ((Idf) idfParenthesisEmpty.idf).name);

        // Restauration des registres
        str.appendLine("BL		__restore_reg__");

        return str.getString();
    }

    @Override
    public String visit(IntNode intNode) {
        return String.format("LDR R0, =%d\n", intNode.parseInt);
    }

    @Override
    public String visit(Affectation affectation) {
        StringAggregator sb = new StringAggregator();
        // on recupere le code assembleur de la partie droite de l'affectation, le code
        // retourne le resultat de l'expression dans R0
        sb.appendLine(affectation.right.accept(this));

        // on recupere l'adresse de la base et le décalage de ce qu'il y à gauche
        int decalage = 0;
        String bp = "R11";
        int imbrication;

        if (affectation.left instanceof Idf idf) {
            imbrication = affectation.getTds().findImbrication(idf.name);
            // chainage statique
            if (affectation.getTds().getImbrication() - imbrication != 0) {
                // sauvegarde du registre R7 sur la pile
                sb.appendLine("STR R7, [R13], #4");
                // recupère l'adresse de base du bloc dans R7
                sb.appendLine("MOV [R11] , R7");
                // ajoute 8 pour pointer vers le chainage statique
                sb.appendLine("ADD R7, R7, #8");

                // itère (num_imbri_courant - num_imbri_decla - 1 (car on a déjà la base de
                // l'appelant)) jusqu'à obtenir la base ou se trouve la déclaration de l'idf
                for (int i = 0; i < affectation.getTds().getImbrication() - imbrication - 1; i++) {
                    sb.appendLine("MOV [R7], R7");
                }
                bp = "R7";
            }
            Symbole s = affectation.getTds().findSymbole(idf.name);
            if (s instanceof SymboleVar sv) {
                decalage = sv.getDeplacement();
            }
        } else if (affectation.left instanceof Fleche fleche) {
            // a->b ou type(b) = int
            // decalage = decalage de a + decalage de b
            // @a->b = base de declaration de a + decalage
            Idf idf = (Idf) fleche.right;
            Symbole s = affectation.getTds().findSymbole(idf.name);

            if (s instanceof SymboleVar sv) {
                decalage = sv.getDeplacement();
            }
        }
        sb.appendFormattedLine("STR R0, [%s, #%d]", bp, decalage);
        if (bp.equals("R7")) {
            // charge l'ancienne valeur de R7
            sb.appendLine("LDR R7, [R13], #-4");
        }
        return sb.getString();
    }

    public String visit(IfThen ifThen) {
        StringAggregator str = new StringAggregator();
        int ifNum = getIfIncr();
        str.appendFormattedLine(";if%d", ifNum);// Commentaire pour debug
        str.appendLine(ifThen.condition.accept(this));
        str.appendLine("CMP R0, #0");
        str.appendFormattedLine("BEQ _finIf%d", ifNum);
        str.appendLine(ifThen.thenBlock.accept(this));
        str.appendFormattedLine("_finIf%d", ifNum);
        str.appendLine();
        return str.getString();
    }

    @Override
    public String visit(IfThenElse ifThenElse) {
        StringAggregator str = new StringAggregator();
        int ifNum = getIfIncr();
        str.appendFormattedLine(";ifThenElse%d", ifNum);// Commentaire pour debug
        str.appendLine(ifThenElse.condition.accept(this));
        str.appendLine("CMP R0, #0");
        str.appendFormattedLine("BEQ _else%d", ifNum);
        str.appendLine(ifThenElse.thenBlock.accept(this));
        str.appendFormattedLine("B  _finIf%d", ifNum);
        str.appendFormattedLine("_else%d", ifNum);
        str.appendLine(ifThenElse.elseBlock.accept(this));
        str.appendFormattedLine("_finIf%d", ifNum);
        str.appendLine();
        return str.getString();
    }

    @Override
    public String visit(While while1) {
        StringAggregator str = new StringAggregator();
        int whileNum = getWhileIncr();
        str.appendFormattedLine("_while %d", whileNum);
        str.appendLine(while1.condition.accept(this));
        str.appendLine("CMP R0, #0");
        str.appendFormattedLine("BEQ _finWhile %d", whileNum);
        str.appendLine(while1.doBlock.accept(this));
        str.appendFormattedLine("B _while %d", whileNum);
        str.appendFormattedLine("_finWhile %d", whileNum);
        str.appendLine();
        return str.getString();
    }

    @Override
    public String visit(Return return1) {
        return return1.expr.accept(this);
    }

    @Override
    public String visit(Bloc bloc) {
        StringAggregator str = new StringAggregator();
        Tds tds = bloc.getTds();

        // Content
        for (Ast instruction : bloc.instList) {
            str.appendString(instruction.accept(this));
        }

        return str.getString();
    }

    @Override
    public String visit(CharNode charNode) {
        return String.format("LDR R0, =%s\n", charNode.string);
    }

    @Override
    public String visit(Fleche fleche) {
        // TODO Auto-generated method stub
        return "";
    }

    @Override
    public String visit(MoinsUnaire unaire) {
        // TODO Auto-generated method stub
        return "";
    }

    @Override
    public String visit(Negation unaire) {
        // TODO Auto-generated method stub
        return "";
    }

    @Override
    public String visit(Semicolon semicolon) {
        // TODO Auto-generated method stub
        return "";
    }

    @Override
    public String visit(Expr_ou expr_ou) {
        // TODO Auto-generated method stub
        return "";
    }

    @Override
    public String visit(Expr_et expr_et) {
        // TODO Auto-generated method stub
        return "";
    }

    public String startCmp(Comparaison cmp, StringAggregator str) {
        str.appendLine("; début comparaison");
        str.appendLine(cmp.left.accept(this));
        // récup le registre depuis r0 dans le premier registre libre
        str.appendLine("MOV R1,R0");
        str.appendLine(cmp.right.accept(this));
        // same
        str.appendLine("MOV R2, R0");
        str.appendLine("CMP R1, R2");
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
        str.appendLine(plus.left.accept(this));
        str.appendLine("BL      __save_reg__");
        str.appendLine("MOV   R1, R0");
        str.appendLine(plus.right.accept(this));
        str.appendLine("ADD     R0, R0, R1");
        str.appendLine("BL      __restore_reg__");
        return str.getString();
    }

    @Override
    public String visit(Minus minus) {
        StringAggregator str = new StringAggregator();
        str.appendLine(minus.left.accept(this));
        str.appendLine("BL      __save_reg__");
        str.appendLine("MOV    R1, R0");
        str.appendLine(minus.right.accept(this));
        str.appendLine("SUB     R0, R0, R1");
        str.appendLine("BL      __restore_reg__");
        return str.getString();
    }

    @Override
    public String visit(Division div) {
        StringAggregator str = new StringAggregator();
        str.appendLine(div.left.accept(this));
        str.appendLine("BL      __save_reg__");
        str.appendLine("MOV    R1, R0");
        str.appendLine(div.right.accept(this));
        str.appendLine("MOV    R2, R0");
        str.appendLine("""
                CMP     R2, #0
                BEQ      __end__ ; division par 0, exit
                """);
        str.appendLine("BL      div");
        str.appendLine();
        if (!division) {
            str.appendLine("""
                    div         STMFA   SP!, {R2-R5}
                                MOV     R0,#0
                                MOV     R3,#0
                                CMP     R1,#0
                                RSBLT   R1,R1,#0
                                EORLT   R3,R3,#1
                                CMP     R2,#0
                                RSBLT   R2,R2,#0
                                EORLT   R3,R3,#1
                                MOV     R4,R2
                                MOV     R5,#1
                    _div_max    LSL     R4,R4,#1
                                LSL     R5,R5,#1
                                CMP     R4,R1
                                BLE     _div_max
                    _div_loop   LSR     R4,R4,#1
                                LSR     R5,R5,#1
                                CMP     R4,R1
                                BGT     _div_loop
                                ADD     R0,R0,R5
                                SUB     R1,R1,R4
                                CMP     R1,R2
                                BGE     _div_loop
                                CMP     R3,#1
                                BNE     _div_exit
                                CMP     R1,#0
                                ADDNE   R0,R0,#1
                                RSB     R0,R0,#0
                                RSB     R1,R1,#0
                                ADDNE   R1,R1,R2
                    _div_exit   LDMFA   SP!, {R2-R5}
                                LDR     PC, [R13, #-4]!
                    """);
            division = true;
        }
        str.appendLine("BL      __restore_reg__");
        return str.getString();
    }

    @Override
    public String visit(Multiplication mult) {
        StringAggregator str = new StringAggregator();
        str.appendLine(mult.left.accept(this));
        str.appendLine("BL      __save_reg__");
        str.appendLine("MOV    R1, R0");
        str.appendLine(mult.right.accept(this));
        str.appendLine("MOV    R2, R0");
        str.appendLine("BL      mul");
        if (!mul) {
            str.appendLine("""
                    mul         STMFA   SP!, {R1,R2}
                                MOV     R0,#0
                    _mul_loop   LSRS    R2,R2,#1
                                ADDCS   R0,R0,R1
                                LSL     R1,R1,#1
                                TST     R2,R2
                                BNE     _mul_loop
                                LDMFA   SP!, {R1,R2}
                                LDR     PC, [R13,#-4]!
                    """);
            mul = true;
        }
        str.appendLine("BL      __restore_reg__");
        return str.getString();
    }

}
