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

public class TrueARM64Generator implements AstVisitor<String> {

    /**
     * Informations/Conventions:
     * Full Descending: SP pointe vers une case "pleine" et SP diminue quand on push sur la stack
     * adresses.
     * X0 : Adresse de retour
     * X1 : Utilisable pour les retours dans les "fonctions intermédiaire" quand on charge des valeurs...
     * 
     * Différence avec 32 bits: 
     *  - Non accès PC: https://developer.arm.com/documentation/dui0801/a/Overview-of-AArch64-state/Program-Counter-in-AArch64-state
     *      vs 32 bits: https://developer.arm.com/documentation/dui0801/a/Overview-of-AArch32-state/Program-Counter-in-AArch32-state?lang=en
     * Sources ARM 64bits:
     *  - https://github.com/ARM-software/abi-aa/releases
     * 
     * Autres sources pour MACOS ARM: 
     *  - https://github.com/below/HelloSilicon
     *  - https://developer.apple.com/documentation/xcode/writing-arm64-code-for-apple-platforms
     *  
     */

    private enum systeme{
        MACOS,
        LINUX
    };

    private systeme type;

    public static final int WORD_SIZE = 16; // Taille d'un mot en octet

    private int whileCompt = 0;
    private int ifCompt = 0;
    private int nbCmp = 0;

    private StringAggregator data;
    private ArrayList<String> dataList;


    public TrueARM64Generator(String type) {
        if(type.equals("macos")){
        this.type = systeme.MACOS;
        }

        else{
           this.type= systeme.LINUX;
        }
        this.data = new StringAggregator();
        this.dataList = new ArrayList<String>();
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

    // |<- SP
    // |var loc|
    // |@stat @bp |
    // |dyn @bp |
    // |@r | <- FP (bp)
    // |param |
    @Override
    public String visit(Idf idf) {
        StringAggregator str = new StringAggregator();
        String bp = "FP";
        int decalage = 0;
        int imbrication = idf.getTds().findImbrication(idf.name);
        // TODO diplay[imbrication] ou remonter tds.getImbrication() - imbrication
        // chainage statique pour mettre a jour bp
        // chainage statique
        if (idf.getTds().getImbrication() - imbrication != 0) {
            // str.appendFormattedLine("LDR X0 , [FP, #-%d]",WORD_SIZE);
            for (int i = 0; i < idf.getTds().getImbrication() - imbrication - 1; i++) {
                // str.appendFormattedLine("LDR X0, [X0, #-%d]", WORD_SIZE);
            }
            bp = "X0";
        }
        Symbole s = idf.getTds().findSymbole(idf.name);
        if (s instanceof SymboleVar sv) {
            decalage = sv.getDeplacement(WORD_SIZE*(-1));
        }
        str.appendFormattedLine("LDR X0, [%s, #%d] // On récupère dans la pile la valeur de la variable", bp, decalage);

        return str.getString();
    }

    @Override
    public String visit(Fichier fichier) {
        StringAggregator str = new StringAggregator();

       
        if(type==systeme.MACOS){
            // Initialisation
            str.appendLine("""
            .global _main             // Provide program starting address to linker
            .align 4        // Nécessaire d'être aligné par 4 pour Darwin
            """);
        }
        else{
            // Initialisation
            str.appendLine("""
            .global _start             // Provide program starting address to linker
            .align 4        

            _start:
            BL _main
            BL __end__
            """);
            //Si on est en linux: on doit ajouter .data à this.data
            this.data.appendLine(".data");

        }

        for (Ast ast : fichier.instructions) {
            String code = ast.accept(this);
            str.appendLine(code);
        }

        // Écriture de la fonction _print
        fonctionPrint(str);

        // Ajout de la macro de sauvegarde des registres
        str.appendLine("__save_reg__:");
        str.appendLine("""
            stp       X0, X1, [SP, #-16]!
            stp       X2, X3, [SP, #-16]!
            stp       X4, X5, [SP, #-16]!
            stp       X6, X7, [SP, #-16]!
            stp       X8, X9, [SP, #-16]!
            stp       X10, X11, [SP, #-16]!
            stp       X12, X13, [SP, #-16]!
            stp       X14, X15, [SP, #-16]!
            stp       X16, X17, [SP, #-16]!
                """);
        str.appendLine("RET");
        str.appendLine();

        // Ajout de la macro de restauration des registres
        str.appendLine("__restore_reg__:");
        str.appendLine("""
            ldp     X16, X17, [SP],#16
            ldp     X14, X15, [SP],#16
            ldp     X12, X13, [SP],#16
            ldp     X10, X11, [SP],#16
            ldp     X8, X9, [SP],#16
            ldp     X6, X7, [SP],#16
            ldp     X4, X5, [SP],#16
            ldp     X2, X3, [SP],#16
                """);
       


        if(type == systeme.MACOS){
            // end 
            str.appendLine("RET");
            // data
            this.data.appendLine("""
                .section	__TEXT,__cstring,cstring_literals
                l_.str:                                 //@.str
                    .asciz	\"%d\\n\"                
                    """);
            str.appendLine(data.getString());
        }

        else{ // linux
            // On place un label end à la fin de programme pour le quitter
            // (l'instruction END n'est pas reconnue par le vrai ARM)
            str.appendLine("__end__:");
            str.appendLine("""
                // Setup the parameters to exit the program
                // and then call Linux to do it.
                mov     X0, #0      // Use 0 return code
                mov     X8, #93     // Service code 93 terminates
                svc     0           // Call Linux to terminate
                    """);
        
            this.data.appendLine("""
                l_.str:                                 
                    .asciz	\"%d\\n\"                
                    """);
            str.appendLine(this.data.getString());
        }

        return str.getString();
    }

    @Override
    public String visit(DeclVarInt declVarInt) {
        // TODO Auto-generated method stub
        StringAggregator str = new StringAggregator();
        
        return str.getString();
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
        String name =  ((Idf) declFctInt.Idf).name;
        Bloc bloc = (Bloc) declFctInt.bloc;
        declFct(str,name,bloc);
        return str.getString();
    }

    @Override
    public String visit(DeclFctStruct declFctStruct) {
        StringAggregator str = new StringAggregator();
        String name = ((Idf) declFctStruct.Idf1).name;
        Bloc bloc = (Bloc) declFctStruct.bloc;
        declFct(str,name,bloc);
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
        // System.out.println(idfParenthesis.getTds().getName()); // le nom de la tds englobante !!
        // Sauvegarde des registres
        str.appendLine("//debut appel fonction");
        // str.appendLine("BL __save_reg__");
        
        String name = ((Idf) idfParenthesis.idf).name;

        // Ajout des parametres à la pile
        int nb_param = idfParenthesis.exprList.size();
        for (int i = 0; i < nb_param; i++) {
            Ast param = idfParenthesis.exprList.get(i);
            str.appendLine(param.accept(this)); // Met le paramètre dans X0
            // Putting X0 in the stack
            str.appendFormattedLine("STR X0, [SP, #-%d]", WORD_SIZE*(nb_param-i));
        }
        str.appendFormattedLine("SUB  SP, SP, #%d ", WORD_SIZE*nb_param);

        // On détermine si la fonction appelée a le même chainage dynamique que la fonction appelante
        int imbricationPere = idfParenthesis.getTds().getImbrication();
        int imbricationAppelee = idfParenthesis.getTds().findImbrication(((Idf) idfParenthesis.idf).name);
        if(imbricationPere == imbricationAppelee){
            // Si les imbrications sont identiques, on enregistre l'adresse pointée par le chainage statique de l'appelant dans X0
            str.appendFormattedLine("LDR X0, [FP, #-%d]  // Chainage statique même imbrication", 1*WORD_SIZE);
        }
        else{
            // Sinon, on enregistre l'adresse de base de l'appelant dans X0
            str.appendFormattedLine("MOV X0, FP // Chainage statique adresse base appellante ");
        }
        // Appel de la fonction
        
        str.appendFormattedLine("BL _%s", ((Idf) idfParenthesis.idf).name);

        // str.appendFormattedLine("_breakpoint_%s:", ((Idf) idfParenthesis.idf).name);
        // Restauration des registres
        // str.appendLine("BL __restore_reg__");
        str.appendLine("//fin appel de fonction");

        return str.getString();
    }

    @Override
    public String visit(IdfParenthesisEmpty idfParenthesisEmpty) {
        StringAggregator str = new StringAggregator();

        // Sauvegarde des registres
        // str.appendLine("BL		__save_reg__");

        // On détermine si la fonction appelée a le même chainage dynamique que la fonction appelante
        int imbricationPere = idfParenthesisEmpty.getTds().getImbrication();
        int imbricationAppelee = idfParenthesisEmpty.getTds().findImbrication(((Idf) idfParenthesisEmpty.idf).name);
        if(imbricationPere == imbricationAppelee){
            // Si les imbrications sont identiques, on enregistre l'adresse pointée par le chainage statique de l'appelant dans X0
            str.appendFormattedLine("LDR X0, [FP, #-%d]", 1*WORD_SIZE);
        }
        else{
            // Sinon, on enregistre l'adresse de base de l'appelant dans X0
            str.appendFormattedLine("MOV X0, FP");
        }

        // Appel de la fonction
        str.appendFormattedLine("BL 		_%s", ((Idf) idfParenthesisEmpty.idf).name);

        // Restauration des registres
        // str.appendLine("BL		__restore_reg__");

        return str.getString();
    }

    @Override
    public String visit(IntNode intNode) {
        return String.format("LDR X0, =%d\n", intNode.parseInt);
    }

    @Override
    public String visit(Affectation affectation) {
        StringAggregator sb = new StringAggregator();
        // on recupere le code assembleur de la partie droite de l'affectation, le code
        // retourne le resultat de l'expression dans X0
        sb.appendLine(affectation.right.accept(this));

        // on recupere l'adresse de la base et le décalage de ce qu'il y à gauche
        int decalage = 0;
        String bp = "FP";
        int imbrication;

        if (affectation.left instanceof Idf idf) {
            imbrication = affectation.getTds().findImbrication(idf.name);
            // chainage statique
            if (affectation.getTds().getImbrication() - imbrication != 0) {
                sb.appendLine();
                // sauvegarde du registre X7 sur la pile
                sb.appendFormattedLine("STR X7, [SP], #%d", WORD_SIZE);

                sb.appendLine("//Chainage statique");
                // recupère l'adresse du chainage statique du bloc dans X7
                // sb.appendFormattedLine("LDR X7 , [FP, #-%d]", WORD_SIZE);

                // itère (num_imbri_courant - num_imbri_decla - 1 (car on a déjà la base de
                // l'appelant)) jusqu'à obtenir la base ou se trouve la déclaration de l'idf
                for (int i = 0; i < affectation.getTds().getImbrication() - imbrication - 1; i++) {
                    // sb.appendFormattedLine("LDR X7, [X7, #-%d]", WORD_SIZE);
                }
                bp = "X7";
            }
            Symbole s = affectation.getTds().findSymbole(idf.name);
            if (s instanceof SymboleVar sv) {
                decalage = sv.getDeplacement(WORD_SIZE);
            }
        } else if (affectation.left instanceof Fleche fleche) {
            // a->b ou type(b) = int
            // decalage = decalage de a + decalage de b
            // @a->b = base de declaration de a + decalage
            Idf idf = (Idf) fleche.right;
            Symbole s = affectation.getTds().findSymbole(idf.name);

            if (s instanceof SymboleVar sv) {
                decalage = sv.getDeplacement(WORD_SIZE);
            }
        }
        // sb.appendFormattedLine("STR X0, [%s, #-%d]", bp, decalage);
        if (bp.equals("X7")) {
            // charge l'ancienne valeur de X7
            // sb.appendFormattedLine("LDR X7, [SP, #-%d]!", WORD_SIZE);
        }
        sb.appendLine();
        return sb.getString();
    }

    public String visit(IfThen ifThen) {
        StringAggregator str = new StringAggregator();
        int ifNum = getIfIncr();
        str.appendFormattedLine("//if%d", ifNum);// Commentaire pour debug
        str.appendLine(ifThen.condition.accept(this));
        str.appendLine("CMP X0, #0 // X0 = bool condition --> Si X0 = 1 : vraie");
        str.appendFormattedLine("BEQ _finIf%d", ifNum);
        str.appendLine(ifThen.thenBlock.accept(this));
        str.appendFormattedLine("_finIf%d:", ifNum);
        str.appendLine();
        return str.getString();
    }

    @Override
    public String visit(IfThenElse ifThenElse) {
        StringAggregator str = new StringAggregator();
        int ifNum = getIfIncr();
        str.appendFormattedLine("//ifThenElse%d", ifNum);// Commentaire pour debug
        str.appendLine(ifThenElse.condition.accept(this));
        str.appendLine("CMP X0, #0");
        str.appendFormattedLine("BEQ _else%d", ifNum);
        str.appendLine(ifThenElse.thenBlock.accept(this));
        str.appendFormattedLine("B  _finIf%d", ifNum);
        str.appendFormattedLine("_else%d:", ifNum);
        str.appendLine(ifThenElse.elseBlock.accept(this));
        str.appendFormattedLine("_finIf%d:", ifNum);
        str.appendLine();
        return str.getString();
    }

    @Override
    public String visit(While while1) {
        StringAggregator str = new StringAggregator();
        int whileNum = getWhileIncr();
        str.appendFormattedLine("_while %d:", whileNum);
        str.appendLine(while1.condition.accept(this));
        str.appendLine("CMP X0, #0");
        str.appendFormattedLine("BEQ _finWhile %d", whileNum);
        str.appendLine(while1.doBlock.accept(this));
        str.appendFormattedLine("B _while %d", whileNum);
        str.appendFormattedLine("_finWhile %d:", whileNum);
        str.appendLine();
        return str.getString();
    }

    @Override
    public String visit(Return return1) {
        StringAggregator str = new StringAggregator();
        str.appendLine("// return");
        str.appendLine(return1.expr.accept(this));


        // Remise du pointeur de pile à sa position avant l'appel de fonction
        str.appendLine("MOV SP, FP");

        // Récupération de l'addresse de retour et retour à l'appelant
        popLRFP(str);

        str.appendLine("RET");
        return str.getString();
    }

    @Override
    public String visit(Bloc bloc) {
        StringAggregator str = new StringAggregator();
        Tds tds = bloc.getTds();
        if (tds.getName().equals("anonblock")) {
            str.appendLine("//debut bloc anonyme");

            // Sauvegarde des registres
            // str.appendLine("BL __save_reg__");

            // Ignore l'adresse de retour
            // On sauvegarde temporairement l'ancien pointeur de base dans X1
            str.appendLine("MOV X1, FP");
            // On met le nouveau pointeur de base dans FP
            str.appendLine("MOV FP, SP");
            // Sauvegarde de l'ancien pointeur de base (chaînage dynamique)
            str.appendFormattedLine("STR X1, [SP, #-%d]", WORD_SIZE);
            // Chainage statique
            str.appendFormattedLine("STR X1, [SP, #-%d]", WORD_SIZE);
            
            // On s'assure que SP pointe sur le maximum de son déplacement (ajoute la place
            // pour var local)
            str.appendLine("//ajout place pour var local");
            str.appendFormattedLine("ADD X1, FP, #-%d", tds.getDeplacement(WORD_SIZE) - WORD_SIZE);
            str.appendLine("MOV SP , X1");
        }

        str.appendLine("//debut instructions");
        // Content
        for (Ast instruction : bloc.instList) {
            str.appendString(instruction.accept(this));
        }
        str.appendLine("//fin instructions");

        if (tds.getName().equals("anonblock")) {
            // Remise du pointeur de pile à sa position avant l'appel de fonction
            str.appendLine("MOV SP, FP");

            // Restauration des registres
            // str.appendLine("BL __restore_reg__");

            str.appendLine("//fin bloc anonyme");
        }
        return str.getString();
    }

    @Override
    public String visit(CharNode charNode) {
        return String.format("LDR X0, =%s\n", charNode.string);
    }

    @Override
    public String visit(Fleche fleche) {
        // TODO Auto-generated method stub
        return "";
    }

    @Override
    public String visit(MoinsUnaire unaire) {
        StringAggregator str = new StringAggregator();
        str.appendLine(unaire.noeud.accept(this));
        str.appendLine("SUB R0, #0, R0");
        return str.getString();
    }

    @Override
    public String visit(Negation unaire) {
        StringAggregator str = new StringAggregator();
        str.appendLine(unaire.noeud.accept(this));
        str.appendLine("CMP R0, #0");
        str.appendLine("MOV R0, #0");
        str.appendFormattedLine("BNE _NonEgal%d // Si X1 > X2",nbCmp); 
        str.appendLine("MOV X0, #1 // On met 1 dans X0");
        str.appendFormattedLine("_NonEgal%d: // Sinon on ne met rien et X0 = 0",nbCmp);
        nbCmp++;
        return str.getString();
    }

    @Override
    public String visit(Semicolon semicolon) {
        // TODO suppr jamais visité
        return "";
    }

    @Override
    public String visit(Expr_ou expr_ou) {
        StringAggregator str = new StringAggregator();
        str.appendLine("; Expr_ou");
        str.appendLine(expr_ou.left.accept(this));
        str.appendLine("BL      __save_reg__");
        str.appendLine("MOV R1,R0");

        str.appendLine(expr_ou.right.accept(this));
        str.appendLine("MOV R2,R0");
        str.appendLine("MOV R0, #0");
        str.appendLine("CMP R2, #1");

        // Comparaison
        str.appendFormattedLine("BEQ _Egal%d // Si X1 > X2",nbCmp); 
        str.appendLine("MOV X0, #1 // On met 1 dans X0");
        str.appendFormattedLine("_Egal%d: // Sinon on ne met rien et X0 = 0",nbCmp);
        nbCmp++;

        str.appendLine("CMP R1, #1");

        // Comparaison
        str.appendFormattedLine("BEQ _Egal%d // Si X1 > X2",nbCmp); 
        str.appendLine("MOV X0, #1 // On met 1 dans X0");
        str.appendFormattedLine("_Egal%d: // Sinon on ne met rien et X0 = 0",nbCmp);
        nbCmp++;
        
        str.appendLine("; Expr_ou");
        return str.getString();
    }

    @Override
    public String visit(Expr_et expr_et) {
        StringAggregator str = new StringAggregator();
        str.appendLine("; Expr_et");
        str.appendLine(expr_et.left.accept(this));
        str.appendLine("BL      __save_reg__");
        str.appendLine("MOV R1,R0");

        str.appendLine(expr_et.right.accept(this));
        str.appendLine("MOV R2,R0");
        str.appendLine("MOV R0, #0");
        str.appendLine("CMP R1, #1");

        // Comparaison Expr_Et 
        str.appendLine("CMP R1, R2");
        str.appendFormattedLine("BEQ _Egal%d // Si X1 > X2",nbCmp); 
        str.appendLine("MOV X0, #1 // On met 1 dans X0");
        str.appendFormattedLine("_Egal%d: // Sinon on ne met rien et X0 = 0",nbCmp);
        nbCmp++;
        
        str.appendLine("; Expr_et");
        return str.getString();
    }

    public String startCmp(Comparaison cmp, StringAggregator str) {
        str.appendLine("// début comparaison");
        str.appendLine(cmp.left.accept(this));
        // récup le registre depuis r0 dans le premier registre libre
        str.appendLine("MOV X1,X0");
        str.appendLine(cmp.right.accept(this));
        // same
        str.appendLine("MOV X2, X0");
        str.appendLine("CMP X1, X2"); // SUBS X0, X1, X2 || CMP X1, X2
        str.appendLine("MOV X0, #0 // Remise a 0 cf plus bas dans comparaison");
        return str.getString();
    }

    public String cmp(Comparaison cmp, StringAggregator str) {
        str.appendLine("MOV X0, #1");
        return str.getString();
    }

    @Override
    public String visit(Egal egal) {
        StringAggregator str = new StringAggregator();
        startCmp(egal, str);
        str.appendFormattedLine("BEQ _Egal%d // Si Z==1 -->  X1 == X2",nbCmp); 
        str.appendLine("MOV X0, #1 // On met 1 dans X0");
        str.appendFormattedLine("_Egal%d: // Sinon on ne met rien et X0 = 0",nbCmp);
        nbCmp++; //On incrémente un compteur pour nommer de manière unique
        return str.getString();
    }

    @Override
    public String visit(Different dif) {
        StringAggregator str = new StringAggregator();
        startCmp(dif, str);
        str.appendFormattedLine("BNE _NotEgal%d // Si X1 != X2",nbCmp); 
        str.appendLine("MOV X0, #1 // On met 1 dans X0");
        str.appendFormattedLine("_NotEgal%d: // Sinon on ne met rien et X0 = 0",nbCmp);
        nbCmp++; //On incrémente un compteur pour nommer de manière unique
        return str.getString();
    }

    @Override
    public String visit(Inferieur inf) {
        StringAggregator str = new StringAggregator();
        startCmp(inf, str);
        str.appendFormattedLine("BLT _Inf%d // Si X1 < X2",nbCmp); 
        str.appendLine("MOV X0, #1 // On met 1 dans X0");
        str.appendFormattedLine("_Inf%d: // Sinon on ne met rien et X0 = 0",nbCmp);
        nbCmp++; //On incrémente un compteur pour nommer de manière unique
        return str.getString();
    }

    @Override
    public String visit(InferieurEgal infEgal) {
        StringAggregator str = new StringAggregator();
        startCmp(infEgal, str);
        str.appendFormattedLine("BLE _InfEgal%d // Si X1 <= X2",nbCmp); 
        str.appendLine("MOV X0, #1 // On met 1 dans X0");
        str.appendFormattedLine("_InfEgal%d: // Sinon on ne met rien et X0 = 0",nbCmp);
        nbCmp++; //On incrémente un compteur pour nommer de manière unique
        return str.getString();
    }

    @Override
    public String visit(Superieur sup) {
        StringAggregator str = new StringAggregator();
        startCmp(sup, str);
        str.appendFormattedLine("BGT _Sup%d // Si X1 > X2",nbCmp); 
        str.appendLine("MOV X0, #1 // On met 1 dans X0");
        str.appendFormattedLine("_Sup%d: // Sinon on ne met rien et X0 = 0",nbCmp);
        nbCmp++; //On incrémente un compteur pour nommer de manière unique
        return str.getString();
    }

    @Override
    public String visit(SuperieurEgal supEgal) {
        StringAggregator str = new StringAggregator();
        startCmp(supEgal, str);
        str.appendFormattedLine("BGE _SupEgal%d // Si X1 >= X2",nbCmp); 
        str.appendLine("MOV X0, #1 // On met 1 dans X0");
        str.appendFormattedLine("_SupEgal%d: // Sinon on ne met rien et X0 = 0",nbCmp);
        nbCmp++; //On incrémente un compteur pour nommer de manière unique
        return str.getString();
    }

    @Override
    public String visit(Plus plus) {
        StringAggregator str = new StringAggregator();
        str.appendLine(plus.left.accept(this));
        str.appendLine("BL      __save_reg__");
        str.appendLine("MOV   X1, X0");
        str.appendLine(plus.right.accept(this));
        str.appendLine("ADD     X0, X0, X1");
        str.appendLine("BL      __restore_reg__");
        return str.getString();
    }

    @Override
    public String visit(Minus minus) {
        StringAggregator str = new StringAggregator();
        str.appendLine(minus.left.accept(this));
        str.appendLine("BL      __save_reg__");
        str.appendLine("MOV    X1, X0");
        str.appendLine(minus.right.accept(this));
        str.appendLine("SUB     X0, X0, X1");
        str.appendLine("BL      __restore_reg__");
        return str.getString();
    }

    @Override
    public String visit(Division div) {
        StringAggregator str = new StringAggregator();
        str.appendLine(div.left.accept(this));
        str.appendLine("BL      __save_reg__");
        str.appendLine("MOV    X1, X0");
        str.appendLine(div.right.accept(this));
        str.appendLine("MOV    X2, X0");
        str.appendLine("""
                CMP     X2, #0
                BEQ      __end__ //division par 0, exit
                """);
        str.appendLine("SDIV    X0, X1, X2");
        str.appendLine();
        str.appendLine("BL      __restore_reg__");
        return str.getString();
    }

    @Override
    public String visit(Multiplication mult) {
        StringAggregator str = new StringAggregator();
        str.appendLine(mult.left.accept(this));
        str.appendLine("BL      __save_reg__");
        str.appendLine("MOV    X1, X0");
        str.appendLine(mult.right.accept(this));
        str.appendLine("MOV    X2, X0");
        str.appendLine("MUL X0,X1,X2");
        str.appendLine("BL      __restore_reg__");
        return str.getString();
    }


    private void fonctionPrint(StringAggregator str){
        str.appendLine("_print:");

        // Sauvegarde de l'adresse de retour et Sauvegarde de l'ancien pointeur de base (chaînage dynamique)
        pushLRFP(str);
          
        // On met le nouveau pointeur de base dans FP
        str.appendLine("MOV FP, SP");

        // Load dans X0 de l'argument
        str.appendFormattedLine("LDR X0, [FP, #%d]  // On récupère l'argument pour print", WORD_SIZE);
  
        if(type==systeme.MACOS){    
            str.appendLine("""
                mov	    X8, X0  // Argument dans X0, on passe à printf par X8
                adrp	x0, l_.str@PAGE
                add	x0, x0, l_.str@PAGEOFF
                str	x8, [sp,#-16]!
                bl	_printf
                add SP, SP, #16 // Clean up stack
                    """);
        }
        else{ // linux
            str.appendLine("""
                mov	    X1, X0  // Argument dans X0, on passe à printf par X8
                ldr 	x0, =l_.str
                str	    x1, [sp,#-16]!
                bl	printf
                add SP, SP, #16 // Clean up stack
                    """);
        }
  
  
        // Remise du pointeur de pile à sa position avant l'appel de fonction
        str.appendLine("MOV SP, FP");

        // Récupération de l'addresse de retour et retour à l'appelant
        popLRFP(str);

        str.appendLine("RET");
    
    }

    /**
     * Push avec pré-decrémentation de SP sur la stack
     * @param str : le StringAggregator qui contient le code à écrire 
     * @param registre : le registre à mettre sur la stack
     */
    private void staticPush(StringAggregator str, String registre ){
        str.appendFormattedLine("STR    %s, [SP,#-%d]! //Chainage statique: PUSH %s sur SP avec pré-décrémentation", registre, WORD_SIZE,registre);
    }
    
    /**
     * Sauvegarde de l'adresse de retour et Sauvegarde de l'ancien pointeur de base (chaînage dynamique)
     * @param str : le StringAggregator qui contient le code à écrire
     */
    private void pushLRFP(StringAggregator str){
        // Sauvegarde de l'adresse de retour et FP
        str.appendFormattedLine("STP   LR, FP, [SP, #-%d]! // PUSH LR, FP", WORD_SIZE);
    }

    /**
     * On pop LR et FP
     * @param str : le StringAggregator qui contient le code à écrire
     */
    private void popLRFP(StringAggregator str){
        // Récupération de l'addresse de retour et retour à l'appelant
        str.appendFormattedLine("LDP   LR, FP, [SP], #%d // Restore LR, FP avec post incrémentation",WORD_SIZE);
        
    }


    private void stackParams(StringAggregator str, int numParams){
        // if (numParams != 0) {
        //     str.appendLine("// On empile les param");
        //     str.appendFormattedLine("SUB SP, SP, #%d", numParams * WORD_SIZE);
        // }
    }

    /**
     * @param str
     * @param name : nom de la fonction
     * @param deplacement    
     * @param bloc
     */
    private void declFct(StringAggregator str, String name, Bloc bloc){
        
        int numParams = bloc.getTds().getParams().size();
        int deplacement = bloc.getTds().getDeplacement();
        System.out.println("deplacement : " + deplacement);
        System.out.println(bloc.getTds().getDeplacement(16));
        // On ajoute le nom de la fonction pour pouvoir faire le jump
        str.appendFormattedLine("_%s:",name);

        // Sauvegarde de l'adresse de retour et Sauvegarde de l'ancien pointeur de base (chaînage dynamique)
        pushLRFP(str);

        // On met le nouveau pointeur de base dans FP
        str.appendLine("MOV FP, SP");

        // Sauvegarde du pointeur du bloc englobant (chaînage statique)
        staticPush(str, "X0");

        // Espace libre pour les variables locales
        // On enlève TEMPORAIREMENT un WORD_SIZE car le chaînage dynamique et l'adresse de retour sont dans le même espace
        // TODO adapter la TDS
        // str.appendFormattedLine("SUB   SP, FP, %d", bloc.getTds().getDeplacement(WORD_SIZE));
        str.appendLine("MOV     X3, #0");
        for(int i=0; i < deplacement -1 ; i++ ){ // -1 car on a démarré le déplacement à 1 pour le chainage statique
            str.appendFormattedLine("STR    X3, [SP,#-%d]! // Espace libre pour variable locales", WORD_SIZE);
        }
   
        //tmp juste X0 

        // Chainage statique
        // staticPush(str);

        // On s'assure que SP pointe sur le maximum de son déplacement
        //TODO : to check
        // str.appendLine("//ajout place pour var local");
        // str.appendFormattedLine("SUB  X1, SP, #%d", deplacement);
        // str.appendLine("MOV SP, X1");

        str.appendLine( bloc.accept(this));

        
        str.appendLine("// RETURN de sécurité si abscence de return");
        // Remise du pointeur de pile à sa position avant l'appel de fonction
        str.appendLine("MOV SP, FP");

        // Récupération de l'addresse de retour et retour à l'appelant
        popLRFP(str);

        str.appendLine("RET");
    }
}

