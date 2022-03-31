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
    private boolean mul = false;
    private boolean division = false;

    private StringAggregator data;
    private ArrayList<String> dataList;

    private int AdresseInitStack = 0xFF000000;

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
            decalage = sv.getDeplacement(WORD_SIZE);
        }
        // str.appendFormattedLine("LDR X0, [%s, #-%d]", bp, decalage);

        return str.getString();
    }

    @Override
    public String visit(Fichier fichier) {
        StringAggregator str = new StringAggregator();

        // Initialisation
        str.appendLine("""
            .global _main             // Provide program starting address to linker
            .align 4        // Nécessaire d'être aligné par 4 pour Darwin
            """);

        //Si on est en linux: on doit ajouter .data à this.data
        if(type==systeme.LINUX){
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
        str.appendLine("RET");
        str.appendLine();

        // On place un label end à la fin de programme pour le quitter
        // (l'instruction END n'est pas reconnue par le vrai ARM)
        // str.appendLine("__end__:");
        if(type == systeme.MACOS){
          
            // str.appendLine("""
            //     mov     X0, #0      // Use 0 return code
            //     mov     X16, #1     // Service command code 1 terminates this program
            //     svc     0           // Call MacOS to terminate the program 
            // """);
            this.data.appendLine("""
                .section	__TEXT,__cstring,cstring_literals
                l_.str:                                 ; @.str
                    .asciz	\"%d\\n\"                
                    """);
            str.appendLine(data.getString());
        }

        else{ // linux
            // str.appendLine("""
            //     // Setup the parameters to exit the program
            //     // and then call Linux to do it.
            //     mov     X0, #0
            //     mov     X8, #93
            //     svc     0
            //     // Use 0 return code
            //     // Service code 93 terminates
            //     // Call Linux to terminate
            //         """);
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
        for (Ast param : idfParenthesis.exprList) {
            str.appendLine(param.accept(this));
            // Putting X0 in the stack
            str.appendFormattedLine("STR X0, [SP, #-%d]!", WORD_SIZE);
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

                sb.appendLine(";Chainage statique");
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
        str.appendFormattedLine(";if%d", ifNum);// Commentaire pour debug
        str.appendLine(ifThen.condition.accept(this));
        str.appendLine("CMP X0, #0");
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
        str.appendFormattedLine(";ifThenElse%d", ifNum);// Commentaire pour debug
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
        return return1.expr.accept(this);
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
            str.appendLine(";ajout place pour var local");
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
        str.appendLine("MOV X1,X0");
        str.appendLine(cmp.right.accept(this));
        // same
        str.appendLine("MOV X2, X0");
        str.appendLine("CMP X1, X2");
        str.appendLine("MOV X0, #0");
        return str.getString();
    }

    @Override
    public String visit(Egal egal) {
        StringAggregator str = new StringAggregator();
        startCmp(egal, str);
        str.appendLine("BEQ _egal");
        str.appendLine("MOV X0, #1");
        str.appendLine("_egal:");
        return str.getString();
    }

    @Override
    public String visit(Different dif) {
        StringAggregator str = new StringAggregator();
        startCmp(dif, str);
        str.appendLine("MOVNE X0, #1");
        return str.getString();
    }

    @Override
    public String visit(Inferieur inf) {
        StringAggregator str = new StringAggregator();
        startCmp(inf, str);
        str.appendLine("BLO _inf");
        str.appendLine("MOV X0, #1");
        str.appendLine("_inf:");
        return str.getString();
    }

    @Override
    public String visit(InferieurEgal infEgal) {
        StringAggregator str = new StringAggregator();
        startCmp(infEgal, str);
        str.appendLine("MOVLE X0, #1");
        return str.getString();
    }

    @Override
    public String visit(Superieur sup) {
        StringAggregator str = new StringAggregator();
        startCmp(sup, str);
        str.appendLine("MOVGT X0, #1");
        return str.getString();
    }

    @Override
    public String visit(SuperieurEgal supEgal) {
        StringAggregator str = new StringAggregator();
        startCmp(supEgal, str);
        str.appendLine("MOVGE X0, #1");
        return str.getString();
    }

    @Override
    public String visit(Plus plus) {
        StringAggregator str = new StringAggregator();
        str.appendLine(plus.left.accept(this));
        // str.appendLine("BL      __save_reg__");
        str.appendLine("MOV   X1, X0");
        str.appendLine(plus.right.accept(this));
        str.appendLine("ADD     X0, X0, X1");
        // str.appendLine("BL      __restore_reg__");
        return str.getString();
    }

    @Override
    public String visit(Minus minus) {
        StringAggregator str = new StringAggregator();
        str.appendLine(minus.left.accept(this));
        // str.appendLine("BL      __save_reg__");
        str.appendLine("MOV    X1, X0");
        str.appendLine(minus.right.accept(this));
        str.appendLine("SUB     X0, X0, X1");
        // str.appendLine("BL      __restore_reg__");
        return str.getString();
    }

    @Override
    public String visit(Division div) {
        StringAggregator str = new StringAggregator();
        str.appendLine(div.left.accept(this));
        // str.appendLine("BL      __save_reg__");
        str.appendLine("MOV    X1, X0");
        str.appendLine(div.right.accept(this));
        str.appendLine("MOV    X2, X0");
        str.appendLine("""
                CMP     X2, #0
                BEQ      __end__ ; division par 0, exit
                """);
        str.appendLine("SDIV    X0, X1, X2");
        str.appendLine();
        // str.appendLine("BL      __restore_reg__");
        return str.getString();
    }

    @Override
    public String visit(Multiplication mult) {
        StringAggregator str = new StringAggregator();
        str.appendLine(mult.left.accept(this));
        // str.appendLine("BL      __save_reg__");
        str.appendLine("MOV    X1, X0");
        str.appendLine(mult.right.accept(this));
        str.appendLine("MOV    X2, X0");
        str.appendLine("MUL X0,X1,X2");
        // str.appendLine("BL      __restore_reg__");
        return str.getString();
    }


    private void fonctionPrint(StringAggregator str){
        str.appendLine("_print:");

        // Sauvegarde de l'adresse de retour et Sauvegarde de l'ancien pointeur de base (chaînage dynamique)
        pushLRFP(str);
          
        // On met le nouveau pointeur de base dans FP
        str.appendLine("MOV FP, SP");
  
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
                mov	    X8, X0  // Argument dans X0, on passe à printf par X8
                ldr 	x0, =l_.str
                str	    x8, [sp,#-16]!
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
     * @param str
     * @param registre
     */
    private void push(StringAggregator str, String registre ){
        str.appendFormattedLine("STR    %s, [SP,#-%d]! //PUSH %s sur SP avec pré-décrémentation", registre, WORD_SIZE,registre);
    }
    

    private void pushLRFP(StringAggregator str){
        // Sauvegarde de l'adresse de retour et FP
        str.appendFormattedLine("STP   LR, FP, [SP, #-%d]! // PUSH LR, FP", WORD_SIZE);
    }

    /**
     * On pop LR et FP
     * @param str
     */
    private void popLRFP(StringAggregator str){
        // Récupération de l'addresse de retour et retour à l'appelant
        str.appendFormattedLine("LDP   LR, FP, [SP], #%d // Restore LR, FP avec post incrémentation",WORD_SIZE);
        
    }


    private void empileParams(StringAggregator str, int numParams){
        // if (numParams != 0) {
        //     str.appendLine("// On empile les param");
        //     str.appendFormattedLine("SUB SP, SP, #%d", numParams * WORD_SIZE);
        // }
    }

    private void staticPush(StringAggregator str){
         // chainage statique
    }


    /**
     * 
     * @param str
     * @param name  nom de la fonction
     * @param deplacement    
     * @param bloc
     */
    private void declFct(StringAggregator str, String name, Bloc bloc){
        
        int numParams = bloc.getTds().getParams().size();
        int deplacement = bloc.getTds().getDeplacement(WORD_SIZE)-2*WORD_SIZE;
        // On ajoute le nom de la fonction pour pouvoir faire le jump
        str.appendFormattedLine("_%s:",name);

        // Sauvegarde de l'adresse de retour et Sauvegarde de l'ancien pointeur de base (chaînage dynamique)
        pushLRFP(str);

        // Buffer pour les variables locales
        // TODO : déterminer 

        
        // On met le nouveau pointeur de base dans FP
        str.appendLine("MOV FP, SP");

        // Récupération des variables locales
        // recupParams(str);
        //tmp juste X0 

        // Chainage statique
        // staticPush(str);

        // On s'assure que SP pointe sur le maximum de son déplacement
        //TODO : to check
        // str.appendLine(";ajout place pour var local");
        // str.appendFormattedLine("SUB  X1, SP, #%d", deplacement);
        // str.appendLine("MOV SP, X1");

        str.appendLine( bloc.accept(this));


        // Remise du pointeur de pile à sa position avant l'appel de fonction
        str.appendLine("MOV SP, FP");

        // Récupération de l'addresse de retour et retour à l'appelant
        popLRFP(str);

        str.appendLine("RET");
    }
    


}

