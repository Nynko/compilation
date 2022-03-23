package compilateur.ARMGenerator;


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
     * Full Ascending: SP pointe vers une case "pleine" et SP augmente avec les
     * adresses.
     * X0 : Adresse de retour
     * 
     * 
     * Différence avec 32 bits: 
     *  - Non accès PC: https://developer.arm.com/documentation/dui0801/a/Overview-of-AArch64-state/Program-Counter-in-AArch64-state
     *      vs 32 bits: https://developer.arm.com/documentation/dui0801/a/Overview-of-AArch32-state/Program-Counter-in-AArch32-state?lang=en
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

    private int AdresseInitStack = 0xFF000000;

    public TrueARM64Generator(String type) {
        if(type.equals("macos")){
        this.type = systeme.MACOS;
        }

        else{
           this.type= systeme.LINUX;
        }
        this.data = new StringAggregator();
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
    // |@r | <- X11 (bp)
    // |param |
    @Override
    public String visit(Idf idf) {
        StringAggregator str = new StringAggregator();
        String bp = "X11";
        int decalage = 0;
        int imbrication = idf.getTds().findImbrication(idf.name);
        // TODO diplay[imbrication] ou remonter tds.getImbrication() - imbrication
        // chainage statique pour mettre a jour bp
        // chainage statique
        if (idf.getTds().getImbrication() - imbrication != 0) {
            str.appendLine("LDR X0 , [X11, #8]");
            for (int i = 0; i < idf.getTds().getImbrication() - imbrication - 1; i++) {
                str.appendLine("LDR X0, [X0, #8]");
            }
            bp = "X0";
        }
        Symbole s = idf.getTds().findSymbole(idf.name);
        if (s instanceof SymboleVar sv) {
            decalage = sv.getDeplacement();
        }
        str.appendFormattedLine("LDR X0, [%s, #%d]", bp, decalage);

        return str.getString();
    }

    @Override
    public String visit(Fichier fichier) {
        StringAggregator str = new StringAggregator();

        // Initialisation
        str.appendLine("""
            .global _main             // Provide program starting address to linker
            .align 4
            """);
        str.appendLine("BL _main");
        str.appendLine("B __end__");

        for (Ast ast : fichier.instructions) {
            String code = ast.accept(this);
            str.appendLine(code);
        }

        // Ajout de la macro de sauvegarde des registres
        str.appendLine("__save_reg__:");
        str.appendLine("\t\tSTMEA	SP!, {X1-X12}");
        str.appendLine("\t\tRET");
        str.appendLine();

        // Ajout de la macro de restauration des registres
        str.appendLine("__restore_reg__:");
        str.appendLine("\t\tLDMEA	SP!, {X1-X12}");
        str.appendLine("\t\tRET");
        str.appendLine();

        // On place un label end à la fin de programme pour le quitter
        // (l'instruction END n'est pas reconnue par le vrai ARM)
        str.appendLine("__end__:");
        if(type == systeme.MACOS){
            str.appendLine("""
                mov     X0, #0      // Use 0 return code
                mov     X16, #1     // Service command code 1 terminates this program
                svc     0           // Call MacOS to terminate the program 
            """);

            str.appendLine("""

                _print:
                mov   X0, #1   // 1 = StdOut
                adr   X1, helloworld   // string to print
                mov   X2, #13  // length of our string
                mov   X16, #4  // macos write system call
                svc   #0x80  // Call macos to output the string


                helloworld: .ascii  \"Hello World!\\n\"
                    """);

            str.appendLine(data.getString());
        }
        else{ // linux
            str.appendLine("""
                // Setup the parameters to exit the program
                // and then call Linux to do it.
                mov     X0, #0
                mov     X8, #93
                svc     0
                // Use 0 return code
                // Service code 93 terminates
                // Call Linux to terminate
                    """);
//TODO à changer le helloworld
            str.appendLine("""
                    
                _print:
                mov     X0, #1     // 1 = StdOut
                ldr   X1, =helloworld // string to print
                mov   X2, #13
                mov   X8, #64
                svc   0
                // length of our string
                // Linux write system call
                // Call Linux to output the string
                    """);

            str.appendLine("""
            .data
            helloworld: .ascii  \"Hello World!\\n\"
            """);
            str.appendLine(this.data.getString());
        }

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
        str.appendFormattedLine("_%s:", ((Idf) declFctInt.Idf).name);
        // Sauvegarde de l'adresse de retour
        str.appendLine("STR		LR, [SP]");
        // On sauvegarde temporairement l'ancien pointeur de base dans X1
        str.appendLine("MOV X1, X11");
        // On met le nouveau pointeur de base dans X11
        str.appendLine("MOV X11, SP");
        // Sauvegarde de l'ancien pointeur de base (chaînage dynamique)
        str.appendFormattedLine("STR X1, [SP, #%d]", WORD_SIZE);
        // Chainage statique

        // On s'assure que SP pointe sur le maximum de son déplacement
        str.appendLine(";ajout place pour var local");
        str.appendFormattedLine("ADD        X1, X11, #%d", ((Bloc) declFctInt.bloc).getTds().getDeplacement());
        str.appendLine("MOV        SP, X1");

        String blocContent = declFctInt.bloc.accept(this);

        str.appendLine(blocContent);

        // Remise du pointeur de pile à sa position avant l'appel de fonction
        str.appendLine("MOV SP, X11");
        int numParams = ((Bloc)declFctInt.bloc).getTds().getParams().size();

        if (numParams != 0) {
            str.appendLine("; on depile les param");
            str.appendFormattedLine("SUB SP, SP, #%d", numParams * 4);
        }
        
        // Récupération de l'addresse de retour et retour à l'appelant
        str.appendLine("LDR   LR, [X11]    // POP LR");
        str.appendLine("RET");
        return str.getString();
    }

    @Override
    public String visit(DeclFctStruct declFctStruct) {
        StringAggregator str = new StringAggregator();
        // On ajoute le nom de la fonction pour pouvoir faire le jump
        str.appendFormattedLine("_%s:", ((Idf) declFctStruct.Idf1).name);

        // Sauvegarde de l'adresse de retour
        str.appendLine("STR		LR, [SP]");
        // On sauvegarde temporairement l'ancien pointeur de base dans X1
        str.appendLine("MOV		X1, X11");
        // On met le nouveau pointeur de base dans X11
        str.appendLine("MOV		X11, SP");
        // Sauvegarde de l'ancien pointeur de base (chaînage dynamique)
        str.appendLine("STR		X1, [SP, #%d]");
        // Chainage statique

        // On s'assure que SP pointe sur le maximum de son déplacement
        str.appendFormattedLine("ADD        X1, X11, #%d", ((Bloc) declFctStruct.bloc).getTds().getDeplacement() + 4);
        str.appendLine("MOV        SP, X1");

        String blocContent = declFctStruct.bloc.accept(this);

        str.appendLine(blocContent);

        // Remise du pointeur de pile à sa position avant l'appel de fonction
        str.appendLine("MOV		SP, X11");
        int numParams = ((Bloc) declFctStruct.bloc).getTds().getParams().size();
        str.appendFormattedLine("SUB		SP, SP, #%d", numParams * 4);
        // Récupération de l'addresse de retour et retour à l'appelant
        str.appendLine("LDR   LR, [X11]   // POP LR");
        str.appendLine("RET");
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
        System.out.println(idfParenthesis.getTds().getName());
        // Sauvegarde des registres
        str.appendLine(";debut appel fonction");
        str.appendLine("BL __save_reg__");

        // Ajout des parametres à la pile
        for (Ast param : idfParenthesis.exprList) {
            str.appendLine(param.accept(this));
            // Putting X0 in the stack
            str.appendFormattedLine("STR X0, [SP], #%d", WORD_SIZE);
        }

        // Appel de la fonction

        str.appendFormattedLine("BL _%s", ((Idf) idfParenthesis.idf).name);

        // Restauration des registres
        str.appendLine("BL __restore_reg__");
        str.appendLine(";fin appel de fonction");

        return str.getString();
    }

    @Override
    public String visit(IdfParenthesisEmpty idfParenthesisEmpty) {
        StringAggregator str = new StringAggregator();

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
        String bp = "X11";
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
                sb.appendLine("LDR X7 , [X11, #8]");

                // itère (num_imbri_courant - num_imbri_decla - 1 (car on a déjà la base de
                // l'appelant)) jusqu'à obtenir la base ou se trouve la déclaration de l'idf
                for (int i = 0; i < affectation.getTds().getImbrication() - imbrication - 1; i++) {
                    sb.appendLine("LDR X7, [X7, #8]");
                }
                bp = "X7";
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
        sb.appendFormattedLine("STR X0, [%s, #%d]", bp, decalage);
        if (bp.equals("X7")) {
            // charge l'ancienne valeur de X7
            sb.appendLine("LDR X7, [SP, #-4]!");
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
            str.appendLine(";debut bloc anonyme");

            // Sauvegarde des registres
            str.appendLine("BL __save_reg__");

            // Ignore l'adresse de retour
            // On sauvegarde temporairement l'ancien pointeur de base dans X1
            str.appendLine("MOV X1, X11");
            // On met le nouveau pointeur de base dans X11
            str.appendLine("MOV X11, SP");
            // Sauvegarde de l'ancien pointeur de base (chaînage dynamique)
            str.appendFormattedLine("STR X1, [SP, #%d]", WORD_SIZE);
            // Chainage statique
            str.appendLine("STR X1, [SP, #8]");
            
            // On s'assure que SP pointe sur le maximum de son déplacement (ajoute la place
            // pour var local)
            str.appendLine(";ajout place pour var local");
            str.appendFormattedLine("ADD X1, X11, #%d", tds.getDeplacement());
            str.appendLine("MOV SP , X1");
        }

        str.appendLine(";debut instructions");
        // Content
        for (Ast instruction : bloc.instList) {
            str.appendString(instruction.accept(this));
        }
        str.appendLine(";fin instructions");

        if (tds.getName().equals("anonblock")) {
            // Remise du pointeur de pile à sa position avant l'appel de fonction
            str.appendLine("MOV SP, X11");

            // Restauration des registres
            str.appendLine("BL __restore_reg__");

            str.appendLine(";fin bloc anonyme");
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
        str.appendLine("MOVEQ X0, #1");
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
        str.appendLine("MOVLT X0, #1");
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
                BEQ      __end__ ; division par 0, exit
                """);
        str.appendLine("BL      div");
        str.appendLine();
        if (!division) {
            str.appendLine("""
                    div         STMFA   SP!, {X2-X5}
                                MOV     X0,#0
                                MOV     X3,#0
                                CMP     X1,#0
                                RSBLT   X1,X1,#0
                                EORLT   X3,X3,#1
                                CMP     X2,#0
                                RSBLT   X2,X2,#0
                                EORLT   X3,X3,#1
                                MOV     X4,X2
                                MOV     X5,#1
                    _div_max    LSL     X4,X4,#1
                                LSL     X5,X5,#1
                                CMP     X4,X1
                                BLE     _div_max
                    _div_loop   LSR     X4,X4,#1
                                LSR     X5,X5,#1
                                CMP     X4,X1
                                BGT     _div_loop
                                ADD     X0,X0,X5
                                SUB     X1,X1,X4
                                CMP     X1,X2
                                BGE     _div_loop
                                CMP     X3,#1
                                BNE     _div_exit
                                CMP     X1,#0
                                ADDNE   X0,X0,#1
                                RSB     X0,X0,#0
                                RSB     X1,X1,#0
                                ADDNE   X1,X1,X2
                    _div_exit   LDMFA   SP!, {X2-X5}
                                LDR     PC, [SP, #-4]!
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
        str.appendLine("MOV    X1, X0");
        str.appendLine(mult.right.accept(this));
        str.appendLine("MOV    X2, X0");
        str.appendLine("BL      mul");
        if (!mul) {
            str.appendLine("""
                    mul         STMFA   SP!, {X1,X2}
                                MOV     X0,#0
                    _mul_loop   LSRS    X2,X2,#1
                                ADDCS   X0,X0,X1
                                LSL     X1,X1,#1
                                TST     X2,X2
                                BNE     _mul_loop
                                LDMFA   SP!, {X1,X2}
                                LDR     PC, [SP,#-4]!
                    """);
            mul = true;
        }
        str.appendLine("BL      __restore_reg__");
        return str.getString();
    }

}
