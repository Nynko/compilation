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
import compilateur.tds.SymboleInt;
import compilateur.tds.SymboleStructContent;
import compilateur.tds.SymboleVar;
import compilateur.tds.Tds;
import compilateur.utils.Os;

public class TrueARM64Generator implements AstVisitor<String> {

    /**
     * Informations/Conventions:
     * Full Descending: SP pointe vers une case "pleine" et SP diminue quand on push sur la stack
     * adresses.
     * X0 : Adresse de retour / argument
     * 
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

    private Os.systeme type;
    public static final int WORD_SIZE = 16; // Taille d'un mot en octet

    private int whileCompt = 0;
    private int ifCompt = 0;
    private int nbCmp = 0;
    private Boolean sl = true;
    private StringAggregator data;

    private Tds lastUsedTds = null; // nécessaire pour avoir la tds des identifiants quand on utilise les comparaisons dans les while et if dont les idfs sont pas forcéement associé à une tds !!


    public TrueARM64Generator(Os.systeme type) {
        this.type = type;
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
    // |@r | <- FP (bp)
    // |param |
    @Override
    public String visit(Idf idf) {
        StringAggregator str = new StringAggregator();
        int decalage = 0;
        Tds tds = idf.getTds();
        if(tds==null){
            tds = lastUsedTds;
        }
        String name = idf.name;

        // Avec chainage statique

        // Détermination du nombre de remontée de chainage statique
        int imbrication = tds.getImbrication();
        int nb_remontage = imbrication - tds.findImbrication(name);

        // On récupère le décalage de la variable
        decalage = ((SymboleVar) tds.findSymbole(name)).getDeplacement(WORD_SIZE); 
        // TODO: pas le truc le plus propre car incertain potentiellement (on a une valeur d'imbrication qu'on utilise pas pour retrouver le symbole et le déplacement)
        // ++ TODO: Z'êtes sûr que findSymbole trouvera pas une struct à la place ? 


        // On remonte les chainages statiques
        str.appendFormattedLine("MOV X17, FP // On récupère le chainage statique", WORD_SIZE);
        for (int i = 0; i < nb_remontage; i++) {
            str.appendFormattedLine("LDR X17, [X17, #-%d] // On remonte de %d fois le chainage", WORD_SIZE, nb_remontage - i);
        }

        // On récupère la nouvelle valeur depuis l'emplacement'adéquate (passage au full descending avec le moins)
        str.appendFormattedLine("LDR X0, [X17, #%d] // On load la nouvelle valeur depuis l'emplacement adéquate avec chainage statique", -decalage); 

        // Symbole s = idf.getTds().findSymbole(idf.name);
        // if (s instanceof SymboleVar sv) {
        //     decalage = sv.getDeplacement(WORD_SIZE*(-1));
        // }
        // str.appendFormattedLine("LDR X0, [FP, #%d] // On récupère dans la pile la valeur de la variable", decalage);

        return str.getString();
    }

    @Override
    public String visit(Fichier fichier) {
        StringAggregator str = new StringAggregator();

        // Ajout de la macro de sauvegarde des registres
        str.appendLine(".macro saveReg Xn, Xnbis");
        str.appendLine("""
            stp       \\Xn, \\Xnbis, [SP, #-16]!
                """);
        str.appendLine(".endm");

        // Ajout de la macro de restauration des registres
        str.appendLine(".macro restoreReg Xn, Xnbis");
        str.appendLine("""
            ldp     \\Xn, \\Xnbis, [SP],#16
                """);
        str.appendLine(".endm");

       
        if(type== Os.systeme.MACOS){
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

        // Écriture fonction _malloc
        fonctionMalloc(str);

        if(type == Os.systeme.MACOS){
            // end 
            str.appendLine("RET");
            str.appendLine("""
            _div_by_zero:
                        // Division par zéro
                        MOV X0, #1
                        RET
            """);
            // End en cas d'erreur
            str.appendLine("__end__:");
            str.appendLine("""
                // Setup the parameters to exit the program
                // and then call Linux to do it.
                mov     X16, #1 // Service code 1 terminates
                svc     #0x80 // Call iOS to terminate
                    """);

            // data
            this.data.appendLine("""
                l_.str:                                 //@.str
                    .asciz	\"%d\\n\"      
                erreur_malloc_str:
                    .asciz \"Erreur Malloc\\n\"          
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
                // mov     X0, #0      // Return code should be in X0
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
        StringAggregator str = new StringAggregator();
        Idf argument = (Idf) sizeof.name; 
        // On récupère le contenu de la structure
        SymboleStructContent symbole = sizeof.getTds().findSymboleStruct("struct_"+ argument.name);
        int size = symbole.getSizeOfStruct();
        str.appendFormattedLine("MOV X0, #%d",size);
        return str.getString();
    }

    @Override
    public String visit(IdfParenthesis idfParenthesis) {
        StringAggregator str = new StringAggregator();
        String name = ((Idf) idfParenthesis.idf).name;
        str.appendLine("//debut appel fonction");
        

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
        int imbricationAppelee = idfParenthesis.getTds().findImbrication(name);
        if(imbricationPere == imbricationAppelee){
            // Si les imbrications sont identiques, on enregistre l'adresse pointée par le chainage statique de l'appelant dans X0
            str.appendFormattedLine("LDR X0, [FP, #-%d]  // Chainage statique même imbrication", 1*WORD_SIZE);
        }
        else{
            // Sinon, on enregistre l'adresse de base de l'appelant dans X0
            str.appendFormattedLine("MOV X0, FP // Chainage statique adresse base appellante ");
        }
        // Appel de la fonction
        str.appendFormattedLine("BL _%s", name);

        // str.appendFormattedLine("_breakpoint_%s:", name);
        // Restauration des registres
        // str.appendLine("BL __restore_reg__");
        str.appendLine("//fin appel de fonction");

        return str.getString();
    }

    @Override
    public String visit(IdfParenthesisEmpty idfParenthesisEmpty) {
        StringAggregator str = new StringAggregator();
        
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


        return str.getString();
    }

    @Override
    public String visit(IntNode intNode) {
        return String.format("LDR X0, =%d\n", intNode.parseInt);
    }

    @Override
    public String visit(Affectation affectation) {
        StringAggregator str = new StringAggregator();
        str.appendLine("// Début affection");
        // on recupere le code assembleur de la partie droite de l'affectation, le code
        // retourne le resultat de l'expression dans X0
        str.appendLine(affectation.right.accept(this));

        // on recupere l'adresse de la base et le décalage de ce qu'il y à gauche
        int decalage = 0;
        int imbrication;
        Tds tds = affectation.getTds();
        String name;

        if (affectation.left instanceof Idf identifiant) { // Si on a un idf à gauche
            // on recupere l'adresse de l'idf
            name = identifiant.name;

            // Détermination du nombre de remontée de chainage statique
            imbrication = tds.getImbrication();
            int nb_remontage = imbrication - tds.findImbrication(name);

            // On récupère le décalage de la variable
            decalage = ((SymboleVar) tds.findSymbole(name)).getDeplacement(WORD_SIZE); 
            // TODO: pas le truc le plus propre car incertain potentiellement (on a une valeur d'imbrication qu'on utilise pas pour retrouver le symbole et le déplacement)

              // On remonte les chainages statiques
            str.appendFormattedLine("MOV X17, FP // On récupère le chainage statique", WORD_SIZE);
            for (int i = 0; i < nb_remontage; i++) {
                str.appendFormattedLine("LDR X17, [X17, #-%d] // On remonte de %d fois le chainage", WORD_SIZE, nb_remontage - i);
            }

            // On store la nouvelle valeur à la place adéquate (passage au full descending avec le moins)
            str.appendFormattedLine("STR X0, [X17, #%d] // On store la nouvelle valeur à la place adéquate avec chainage statique", -decalage); 

        }
        // TODO: Fleche
        else if (affectation.left instanceof Fleche fleche) {
            // (a) -> b (avec a potentiellement aussi une fleche)
            // Il faut donc accept a jusqu'à que a soit un idf. On a besoin d'accepter l'idf pour récupèrer l'adresse !
            // a->b où type(b) = int
            // @a->b = base de declaration de a + decalage de b

            // On recupere le decalage de l'int à droite:
            Idf idf = (Idf) fleche.right;
            Symbole s = idf.getTds().findSymbole(idf.name);
            decalage = ((SymboleVar) s).getDeplacement() - 2; // - 2 car on s'en fout de la base et chainage statique ici
            decalage = decalage * WORD_SIZE;
            // Pas besoin d'accepter b (dans (a) -> b) c'est là où l'on veut affecter.

            // On récupère l'addresse de a
            str.appendLine("MOV X7, X0 // on SAVE X0 dans X7");
            str.appendLine(fleche.left.accept(this)); 
            // Soit fleche.left = idf --> dans ce cas on load la valeur de a dans X0
            // Soit fleche.left est une fleche --> dans ce cas, on récupère l'adresse de c (a -> c -> b) en cherchant a.

            // On a donc X7 = @ 
            str.appendFormattedLine("ADD X0, X0, #%d // On ajoute le décalage de b dans (a) -> b", decalage);

            // On store la valeur de X0 à l'adresse correspondante sachant que l'on est dans la heap
            str.appendFormattedLine("STR X7, [X0] // On store la nouvelle valeur sachant que l'on est dans la heap (décalage positif)");

        }
        str.appendLine();
        str.appendLine("// Fin affection");
        return str.getString();
    }

    public String visit(IfThen ifThen) {
        StringAggregator str = new StringAggregator();
        int ifNum = getIfIncr();
        lastUsedTds = ifThen.getTds();
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
        lastUsedTds = ifThenElse.getTds();
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
        lastUsedTds = while1.getTds();
        str.appendFormattedLine("_while%d:", whileNum);
        str.appendLine(while1.condition.accept(this));
        str.appendLine("CMP X0, #0");
        str.appendFormattedLine("BEQ _finWhile%d", whileNum);
        str.appendLine(while1.doBlock.accept(this));
        str.appendFormattedLine("B _while%d", whileNum);
        str.appendFormattedLine("_finWhile%d:", whileNum);
        str.appendLine();
        return str.getString();
    }

    @Override
    public String visit(Return return1) {
        StringAggregator str = new StringAggregator();
        str.appendLine("// return");
        str.appendLine(return1.expr.accept(this));

        // chainage statique : si on est dans une imbrication > 1 on remonte le chainage statique
        if(return1.getTds().getImbrication() > 1) {
            int imbrication = return1.getTds().getImbrication();
            str.appendFormattedLine("// On remonte au total de %d fois le chainage statique des blocs", imbrication - 1);
            for (int i = 0; i < imbrication - 1; i++) {
                remonteeChainageStatique(str);
            }
        }

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
        String name = tds.getName();

        Boolean notFunctionBlock = name.equals("anonblock") || name.equals("thenblock") || name.equals("elseblock") || name.equals("whileblock");
        // Si on est pas dans un bloc de fonction !!
        if (notFunctionBlock) {
            str.appendFormattedLine("//debut bloc %s", name);

        // On réalise un chainage statique:

            // On a pas besoin de chainage dynamique et pour éviter des problèmes avec les variables locales et le déplacement !!!!
            // on met une case vide à la place du chainage dynamique
            str.appendFormattedLine("SUB SP, SP, #%d", WORD_SIZE);

            // Chainage statique
            // On sait qu'il y a une différence d'imbrication 
            // Donc on enregistre l'adresse de base de l'appelant sur la pile (FP)
            str.appendFormattedLine("STR FP, [SP, #-%d]", WORD_SIZE); // On décrémente pas encore 

            // On met le nouveau pointeur de base dans FP
            str.appendLine("MOV FP, SP");

            // On décrémente SP qui avait bougé du coup 
            str.appendFormattedLine("SUB SP, SP, #%d", WORD_SIZE);

            // Espace libre pour les variables locales
            str.appendLine("MOV     X3, #0 // Inutile si pas de variables locales");
            int deplacement = bloc.getTds().getDeplacement();
            for(int i=0; i < deplacement -2 ; i++ ){ // -2 car on a démarré le déplacement à 2 pour le chainage statique
                str.appendFormattedLine("STR    X3, [SP,#-%d]! // Espace libre pour variable locales", WORD_SIZE);
            }

        }

        str.appendLine("//debut instructions");
        // Content
        for (Ast instruction : bloc.instList) {
            str.appendString(instruction.accept(this));
        }
        str.appendLine("//fin instructions");

        if (notFunctionBlock) {
            // Remise du pointeur de pile à sa position du chainage statique + WORD_SIZE (on rappel que le fp est au dessus du chainage statique)
            str.appendLine("MOV SP, FP");
            remonteeChainageStatique(str);
            str.appendFormattedLine("//fin bloc %s", name);
        }
        return str.getString();
    }

    private void remonteeChainageStatique(StringAggregator str) {
        // On remonte le chainage statique
        // str.appendFormattedLine("ADD   SP, FP, #%d // On récupère l'ancien SP situé juste sous le FP actuel", WORD_SIZE); // On en aura besoin pour les appels de fonctions ! Qui prennent SP en compte pour mettre les nouveaux paramètres !! 
        str.appendFormattedLine("LDR   FP, [FP,#-%d] //Remontée du chainage statique", WORD_SIZE); // (On rappel que le FP est au dessus du chainage statique)
    }


    @Override
    public String visit(CharNode charNode) {
        String str = "' '";
        if(sl){
            if(charNode.string.equals('n')){
                str = "'\\n'";
            }
            else if(charNode.string.equals('s')){
                // do nothing
            }
        }
        else{
            str = charNode.string;
        }

        return String.format("LDR X0, =%s //Char \n ",str);
    }

    @Override
    public String visit(Fleche fleche) {
        StringAggregator str = new StringAggregator();
        str.appendLine("// fleche");
        // On récupère l'adresse de a (a -> b) dans X0
        str.appendLine(fleche.left.accept(this));

        // On récupère l'adresse de b: pour cela
        // On récupère le décalage de b par rapport à a
        Idf idf = (Idf) fleche.right;
        Tds tds = idf.getTds();
        int deplacement = ((SymboleVar) tds.findSymbole(idf.name)).getDeplacement() - 2; // -2 car on s'en fout de BP et chainage statique
        str.appendFormattedLine("LDR X0, [X0, #%d] // On récupère l'addresse de b (dans a -> b -> c)", deplacement*WORD_SIZE);
        str.appendLine("// Rappel on a malloc donc deplacement positif");
        return str.getString();
    }

    @Override
    public String visit(MoinsUnaire unaire) {
        StringAggregator str = new StringAggregator();
        str.appendLine(unaire.noeud.accept(this));
        str.appendLine("NEG X0, X0");
        return str.getString();
    }

    @Override
    public String visit(Negation unaire) {
        StringAggregator str = new StringAggregator();
        str.appendLine(unaire.noeud.accept(this));
        str.appendLine("CMP X0, #0");
        str.appendLine("MOV X0, #1 // On suppose que la condition est vraie");
        str.appendFormattedLine("BEQ _Egal%d // Si X0 == 0, on fait rien",nbCmp); 
        str.appendLine("MOV X0, #0 // Sinon on met 0 dans X0");
        str.appendFormattedLine("_Egal%d: // on ne met rien et X0 = 1",nbCmp);
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
        // str.appendLine("BL      __save_reg__");
        str.appendLine("MOV X1,X0");

        str.appendLine(expr_ou.right.accept(this));
        str.appendLine("MOV X2,X0");
        str.appendLine("MOV X0, #1");
        str.appendLine("CMP X2, #1");

        // Comparaison
        str.appendFormattedLine("BEQ _Egal%d // Si X1 == 1",nbCmp); // Si x1 vaut 1 alors l'expression ou est valide
        str.appendLine("CMP X1, #1");

        // Comparaison
        str.appendFormattedLine("BEQ _Egal%d // Si X2 == 1",nbCmp); // Si x2 vaut 1 et x1 vaut 0 alors elle est valide aussi
        str.appendLine("MOV X0, #0 // On met 0 dans X0"); // x1 et x2 vale tous les deux 0 à ce moment donc l'expr n'est pas valide

        str.appendFormattedLine("_Egal%d: // Sinon on met 1 dans X0",nbCmp);
        nbCmp++;
        str.appendLine("; Expr_ou");
        return str.getString();
    }

    @Override
    public String visit(Expr_et expr_et) {
        StringAggregator str = new StringAggregator();
        str.appendLine("; Expr_et");
        str.appendLine(expr_et.left.accept(this));
        // str.appendLine("BL      __save_reg__");
        str.appendLine("MOV X1,X0");

        str.appendLine(expr_et.right.accept(this));
        str.appendLine("MOV X2,X0");
        str.appendLine("MOV X0, #0");
        str.appendLine("CMP X1, #1");
        str.appendFormattedLine("BNE _NonEgal%d // Si X1 == 0",nbCmp); // Si x1 est faux alors toutes l'expression est fausse

        // Comparaison Expr_Et 
        str.appendLine("CMP X1, X2"); // Si x1 ne vaut pas x2 sachant que x1 est vrai alors l'expression est fausse
        str.appendFormattedLine("BNE _NonEgal%d // Si X1 == X2",nbCmp); 
        str.appendLine("MOV X0, #1 // On met 1 dans X0"); // Sinon tout vaut 1 donc l'expression est juste

        str.appendFormattedLine("_NonEgal%d:",nbCmp);
        nbCmp++;
        str.appendLine("; Expr_et");
        return str.getString();
    }

    public String startCmp(Comparaison cmp, StringAggregator str) {
        str.appendLine("// début comparaison");
        str.appendLine(cmp.left.accept(this));
        // récup le registre depuis X0 dans le premier registre libre
        str.appendLine("MOV X1,X0");
        str.appendLine(cmp.right.accept(this));
        // same
        str.appendLine("MOV X2, X0");
        str.appendLine("CMP X1, X2"); // SUBS X0, X1, X2 || CMP X1, X2
        str.appendLine("MOV X0, #1 // On suppose que la condition est juste");
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
        str.appendLine("MOV X0, #0 // On met 0 dans X0");
        str.appendFormattedLine("_Egal%d: // Sinon on ne met rien et X0 = 0",nbCmp);
        nbCmp++; //On incrémente un compteur pour nommer de manière unique
        return str.getString();
    }

    @Override
    public String visit(Different dif) {
        StringAggregator str = new StringAggregator();
        startCmp(dif, str);
        str.appendFormattedLine("BNE _NotEgal%d // Si X1 != X2",nbCmp); 
        str.appendLine("MOV X0, #0 // On met 0 dans X0");
        str.appendFormattedLine("_NotEgal%d: // Sinon on ne met rien et X0 = 0",nbCmp);
        nbCmp++; //On incrémente un compteur pour nommer de manière unique
        return str.getString();
    }

    @Override
    public String visit(Inferieur inf) {
        StringAggregator str = new StringAggregator();
        startCmp(inf, str);
        str.appendFormattedLine("BLT _Inf%d // Si X1 < X2",nbCmp); 
        str.appendLine("MOV X0, #0 // On met 0 dans X0");
        str.appendFormattedLine("_Inf%d: // Sinon on ne met rien et X0 = 0",nbCmp);
        nbCmp++; //On incrémente un compteur pour nommer de manière unique
        return str.getString();
    }

    @Override
    public String visit(InferieurEgal infEgal) {
        StringAggregator str = new StringAggregator();
        startCmp(infEgal, str);
        str.appendFormattedLine("BLE _InfEgal%d // Si X1 <= X2",nbCmp); 
        str.appendLine("MOV X0, #0 // On met 1 dans X0");
        str.appendFormattedLine("_InfEgal%d: // Sinon on ne met rien et X0 = 0",nbCmp);
        nbCmp++; //On incrémente un compteur pour nommer de manière unique
        return str.getString();
    }

    @Override
    public String visit(Superieur sup) {
        StringAggregator str = new StringAggregator();
        startCmp(sup, str);
        str.appendFormattedLine("BGT _Sup%d // Si X1 > X2",nbCmp); 
str.appendLine("MOV X0, #0 // On met 0 dans X0");
        str.appendFormattedLine("_Sup%d: // Sinon on ne met rien et X0 = 0",nbCmp);
        nbCmp++; //On incrémente un compteur pour nommer de manière unique
        return str.getString();
    }

    @Override
    public String visit(SuperieurEgal supEgal) {
        StringAggregator str = new StringAggregator();
        startCmp(supEgal, str);
        str.appendFormattedLine("BGE _SupEgal%d // Si X1 >= X2",nbCmp); 
str.appendLine("MOV X0, #0 // On met 0 dans X0");
        str.appendFormattedLine("_SupEgal%d: // Sinon on ne met rien et X0 = 0",nbCmp);
        nbCmp++; //On incrémente un compteur pour nommer de manière unique
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
        str.appendLine("SUB     X0, X1, X0");
        // str.appendLine("BL      __restore_reg__");
        return str.getString();
    }

    @Override
    public String visit(Division div) {
        StringAggregator str = new StringAggregator();
        str.appendLine(div.left.accept(this));
        // str.appendLine("BL      __save_reg__");
        str.appendLine("MOV    X3, X0");
        str.appendLine(div.right.accept(this));
        str.appendLine("MOV    X4, X0");
        if(type==Os.systeme.MACOS){
            str.appendLine("""
                    CMP     X4, #0
                    BEQ     _div_by_zero
                    """);
        }
        else{
        str.appendLine("""
                
                BEQ      __end__ //division par 0, exit
                """);
        }
        str.appendLine("SDIV    X0, X3, X4");
        str.appendLine();
        // str.appendLine("BL      __restore_reg__");
        return str.getString();
    }

    @Override
    public String visit(Multiplication mult) {
        StringAggregator str = new StringAggregator();
        str.appendLine(mult.left.accept(this));
        // str.appendLine("BL      __save_reg__");
        str.appendLine("MOV    X3, X0");
        str.appendLine(mult.right.accept(this));
        str.appendLine("MOV    X4, X0");
        str.appendLine("MUL X0,X3,X4");
        // str.appendLine("BL      __restore_reg__");
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

        // Save reg 
        str.appendLine("saveReg X14, X15");
  
        if(type==Os.systeme.MACOS){    
            str.appendLine("""
                mov	    X8, X0  // On copie l'argument dans X8
                adrp	x0, l_.str@PAGE
                add	    x0, x0, l_.str@PAGEOFF
                str	    x8, [sp,#-16]!  
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

        // Restore reg 
        str.appendLine("restoreReg X14, X15");
  
  
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


    // private void stackParams(StringAggregator str, int numParams){
    //     // if (numParams != 0) {
    //     //     str.appendLine("// On empile les param");
    //     //     str.appendFormattedLine("SUB SP, SP, #%d", numParams * WORD_SIZE);
    //     // }
    // }

    /** Fonction pour la déclaration de fonction (afin d'éviter redondances entre struct et int)
     * @param str : le StringAggregator qui contient le code à écrire
     * @param name : nom de la fonction
     * @param deplacement : déplacement de la fonction dans la mémoire
     * @param bloc : le bloc de l'AST declFunction {Struct || Int}
     */
    private void declFct(StringAggregator str, String name, Bloc bloc){
        
        // int numParams = bloc.getTds().getParams().size();
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
        if(name.equals("main")){
            str.appendLine("MOV X0, FP"); // Le chainage statique dans ce cas est le même que FP
            // Ajout de l'adresse de la HEAP dans X14 et X15
            str.appendLine("SUB X14, SP, #0x1000 // Adresse de la base HEAP");
            str.appendLine("MOV X15, X14 // Adresse du haut de la HEAP");
        }
        staticPush(str, "X0");

        // Espace libre pour les variables locales
        str.appendLine("MOV     X3, #0 // Inutile si pas de variables locales");
        for(int i=0; i < deplacement -2 ; i++ ){ // -2 car on a démarré le déplacement à 2 pour le chainage statique
            str.appendFormattedLine("STR    X3, [SP,#-%d]! // Espace libre pour variable locales", WORD_SIZE);
        }
        
        str.appendLine( bloc.accept(this));
        
        str.appendLine("// RETURN de sécurité si abscence de return");
        // Remise du pointeur de pile à sa position avant l'appel de fonction
        str.appendLine("MOV SP, FP");

        // Récupération de l'addresse de retour et retour à l'appelant
        popLRFP(str);

        str.appendLine("RET");
    }


        
    private void fonctionMalloc(StringAggregator str){
        str.appendLine("_malloc:");

        // Sauvegarde de l'adresse de retour et Sauvegarde de l'ancien pointeur de base (chaînage dynamique)
        pushLRFP(str);
            
        // On met le nouveau pointeur de base dans FP
        str.appendLine("MOV FP, SP");

        // Sauvegarde du pointeur du bloc englobant (chaînage statique)
        staticPush(str, "X0");

        // Load dans X0 de l'argument (la taille de l'espace à allouer)
        str.appendFormattedLine("LDR X0, [FP, #%d]  // On récupère l'argument pour malloc --> taille espace à allouer", WORD_SIZE);
    
        str.appendFormattedLine("""
            // Test si HEAP + X0 < STACK
            ADD X1, X0, X15
            MOV X2, SP
            CMP X1, X2
            BGE erreur_malloc // Si Heap + X0 >= stack, on renvoie NULL
            //sinon on alloue X0 octets dans la zone mémoire
            // On le fait comme un calloc ici afin d'allouer la mémoire
            MOV X1, X0  // On copie pour allouer 
            MOV X0, X15 // On renvoit l'adresse de la zone mémoire allouée
            MOV X2, #0  // On mettra des 0 dans la zone à allouer
            whileMalloc:
            CMP X1, #0
            BLE finWhileMalloc
            STR X2, [X15], #16
            SUB X1, X1, #8  // Oui car on utilise pas les 16 octets mais seulement 8
            B whileMalloc
            finWhileMalloc:
            """);

        // Remise du pointeur de pile à sa position avant l'appel de fonction
        str.appendLine("MOV SP, FP");

        // Récupération de l'addresse de retour et retour à l'appelant
        popLRFP(str);

        str.appendLine("RET");

        // Écriture erreur_malloc
        if(type==Os.systeme.MACOS){
            str.appendLine("""
                erreur_malloc:
                adrp	x0, erreur_malloc_str@PAGE
                add	x0, x0, erreur_malloc_str@PAGEOFF 
                bl    _printf
                        """);
        }
        else{
            str.appendLine("""
                erreur_malloc:
                ldr 	x0, =erreur_malloc_str  
                bl    printf
                    """);
        }
        str.appendLine("""
            MOV X0, #0 // On retourne 0
            MOV SP, FP
            // B   __end__ // On termine le programme
            """);
        popLRFP(str); // Récupération de l'addresse de retour et retour à l'appelant
        str.appendLine("RET");
    }

}

