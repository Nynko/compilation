package compilateur.TDS;


import java.util.ArrayList;
import java.util.NoSuchElementException;


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
import compilateur.ast.Multiplication;
import compilateur.ast.ParamInt;
import compilateur.ast.ParamListMulti;
import compilateur.ast.ParamStruct;
import compilateur.ast.Plus;
import compilateur.ast.Return;
import compilateur.ast.Semicolon;
import compilateur.ast.Sizeof;
import compilateur.ast.Superieur;
import compilateur.ast.SuperieurEgal;
import compilateur.ast.MoinsUnaire;
import compilateur.ast.Negation;
import compilateur.ast.While;

public class TdsCreator implements TdsVisitor<Symbole>{

    private static final int OFFSETVALUE = 4;

    public void addSymboleIntStructToTds(Symbole symbole, int deplacement, Tds tds, String nomFonctionAppellante){
        if(symbole instanceof SymboleInt){
            //UPDATE du déplacement
            SymboleInt symboleInt = (SymboleInt) symbole;
            symboleInt.setDeplacement(deplacement);

            try {
                tds.addSymbole(symboleInt.getName(), symbole);
            } catch (SymbolAlreadyExistsException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        else if(symbole instanceof SymboleStruct){
            //UPDATE du déplacement
            SymboleStruct symboleStruct = (SymboleStruct) symbole;
            symboleStruct.setDeplacement(deplacement);

            
            try {
                tds.addSymbole(symboleStruct.getName(), symbole);
            } catch (SymbolAlreadyExistsException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } 
        }

        else{
            throw new Error("Erreur de remontée des symboles dans" + nomFonctionAppellante + ", symbole doit être SymboleInt ou SymboleStruct)");
        }
    }


    @Override public Symbole visit(Fichier fichier, Tds tds){

        tds.addnumRegion(0);
        tds.addNameSpaceStruct(new NameSpaceStruct());

        if (fichier.instructions != null) {

            for (Ast ast:fichier.instructions){
                Symbole instruction= ast.accept(this,tds);
                if(instruction!= null){

                    if(instruction instanceof SymboleFonction){
                        try {
                            tds.addSymbole(((SymboleFonction)instruction).getName(), instruction);
                        } catch (SymbolAlreadyExistsException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }

                    else if (instruction instanceof SymboleStructContent){ 
                        try {
                            tds.getNameSpaceStruct().addSymboleDeclStruct(((SymboleStructContent)instruction));
                        } catch (SymbolAlreadyExistsException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }

                    else{
                        throw new Error("Erreur de remontée des symboles visit(Fichier...), instruction doit être SymboleStructContent ou SymboleFonction");
                    }
                }
            }
        }

        return null;
    }

    @Override public Symbole visit(Idf idf, Tds tds){
        return new SymboleName(idf.name);
    }

    @Override public Symbole visit(DeclVarInt declVarInt, Tds tds){
        ListeSymbole listeSymbole = new ListeSymbole();
  
        for(Ast identifiants : declVarInt.idf){
            SymboleName nameStr = (SymboleName) identifiants.accept(this,tds);
            Symbole symbole = new SymboleInt(nameStr.getString());
            symbole.addDefinitionLine(declVarInt.line);
            listeSymbole.addSymbole(symbole);
        }
    
        return listeSymbole;
    }
//CHANGEMENT ok 


    @Override public Symbole visit(DeclVarStruct declVarStruct, Tds tds){

        ArrayList<Ast> liste =  declVarStruct.idf;

        Symbole nameSymbole = liste.get(0).accept(this, tds);
        String nameStruct = ((SymboleName) nameSymbole).getString();

        // Récupération de la structure
        SymboleStructContent struct ; 
        try {
            struct = tds.getNameSpaceStruct().getStruct(nameStruct);
        } catch (NoSuchElementException e) {
            // throw new UndefinedStructureException(nameStruct,nameSymbole.getDefinitionLine());
            struct = new SymboleStructContent(nameStruct);
        }
       

        liste.remove(0);      
        ListeSymbole listeSymbole = new ListeSymbole();
        for(Ast identifiants :liste){
            SymboleName nameStr = (SymboleName) identifiants.accept(this,tds);
            Symbole symbole = new SymboleStruct(struct,nameStr.getString());
            symbole.addDefinitionLine(declVarStruct.line);
            listeSymbole.addSymbole(symbole);
        }
    
        return listeSymbole;
    }
// changement ok

    @Override public Symbole visit(Decl_typ decl_typ, Tds tds){
        // Création d'une struct que l'on va ajouter dans le NameSpace et pouvoir la référencer.
        SymboleName identifiant = (SymboleName) decl_typ.idf.accept(this,tds);
        String idf = identifiant.getString();

        ArrayList<Symbole> listeVars = new ArrayList<Symbole>();

        for (Ast ast:decl_typ.decl){
            Symbole symbole = ast.accept(this, tds);
            symbole.addDefinitionLine(decl_typ.line);
            if(symbole instanceof ListeSymbole){
                ListeSymbole liste = (ListeSymbole) symbole;
                for(Symbole elem : liste.getList()){
                    listeVars.add(elem);
                } 
            }

            else{
                throw new Error("Erreur de remontée des symboles dans visit(Decl_typ...), symbole doit être une liste de Symbole (contenant des SymboleInt ou SymboleStruct)");
            }

        }

        SymboleStructContent symboleDeclSrtuct = new SymboleStructContent(idf, listeVars);
        symboleDeclSrtuct.addDefinitionLine(decl_typ.line);

        return symboleDeclSrtuct;
    }



    @Override public Symbole visit(DeclFctInt declFctInt, Tds tds){
        Symbole nameSymbole = declFctInt.Idf.accept(this,tds);
        String nameFunction = ((SymboleName) nameSymbole).getString();

        Tds tdsFunction = new Tds(tds); // Création d'une nouvelle Tds
        SymboleFonction symboleFonction = new SymboleFonction(nameFunction, tdsFunction);
        symboleFonction.setReturnType("int");
        symboleFonction.addDefinitionLine(declFctInt.line);

        if (declFctInt.param != null) {
            declFctInt.param.accept(this,tdsFunction);  
         }

        if (declFctInt.bloc != null){
            declFctInt.bloc.accept(this,tdsFunction);
        }

        return symboleFonction;
    }


    @Override public Symbole visit(DeclFctStruct declFctStruct, Tds tds){
        Symbole nameStructSymbole = declFctStruct.Idf0.accept(this, tds);
        String nameStruct = ((SymboleName) nameStructSymbole).getString();

        Symbole nameSymbole = declFctStruct.Idf1.accept(this,tds);
        String nameFunction = ((SymboleName) nameSymbole).getString();

        Tds tdsFunction = new Tds(tds); // Création d'une nouvelle Tds
        SymboleFonction symboleFonction = new SymboleFonction(nameFunction, tdsFunction);
        symboleFonction.addDefinitionLine(declFctStruct.line);

        if(tds.isNameSpaceStructContains(nameStruct)){
            symboleFonction.setReturnType(nameStruct);
        }

        else{
            // throw new UndefinedStructureException(nameStruct, nameStructSymbole.getDefinitionLine());
        }

        if (declFctStruct.param != null) {
           declFctStruct.param.accept(this,tdsFunction);  
        }

        if (declFctStruct.bloc != null){
            declFctStruct.bloc.accept(this,tdsFunction);
        }

        return symboleFonction;
    }


    @Override public Symbole visit(ParamListMulti paramListMulti, Tds tds){

        int deplacementParam = -OFFSETVALUE; // On considère que l'on sera en "Empty ascending ou descending dans la stack" 
        for (Ast ast:paramListMulti.paramList){
            Symbole symbole = ast.accept(this,tds);

            if(symbole instanceof SymboleInt){
                //UPDATE du déplacement
                ((SymboleInt)symbole).setDeplacement(deplacementParam);
                addSymboleIntStructToTds(symbole,deplacementParam,tds,"visit(ParamListMulti...)");
                deplacementParam -= OFFSETVALUE;

                

            }

            else if(symbole instanceof SymboleStruct){
                //UPDATE du déplacement
                ((SymboleStruct)symbole).setDeplacement(deplacementParam);
                addSymboleIntStructToTds(symbole,deplacementParam,tds,"visit(ParamListMulti...)");
                deplacementParam -= OFFSETVALUE;
            }

            else{
                throw new Error("Erreur de remontée des symboles dans visit(DeclFctInt...), symbole doit être SymboleInt ou SymboleStruct)");
            }
        }
        
        return null; 
    }

    @Override public Symbole visit(ParamInt paramInt, Tds tds){
        
        SymboleName symboleName = (SymboleName) paramInt.name.accept(this, tds);
        String name = symboleName.getString();
        SymboleInt symboleInt = new SymboleInt(name);   
        symboleInt.addDefinitionLine(paramInt.line);

        return symboleInt; 
    }

    @Override public Symbole visit(ParamStruct paramStruct, Tds tds){

        Symbole nameStructSymbole = paramStruct.idf0.accept(this, tds);
        String nameStruct = ((SymboleName) nameStructSymbole).getString();

        SymboleStructContent struct;

        if(tds.isNameSpaceStructContains(nameStruct)){
            struct = tds.getNameSpaceStruct(nameStruct);
        }

        else{
            // throw new UndefinedStructureException(nameStruct, nameStructSymbole.getDefinitionLine());
            struct = new SymboleStructContent(nameStruct);
        }

        Symbole symbole = paramStruct.idf1.accept(this, tds);
        String nameVar = ((SymboleName) symbole).getString();
        
        SymboleStruct symboleStruct = new SymboleStruct(struct, nameVar);
        symboleStruct.addDefinitionLine(paramStruct.line);

        return symboleStruct;
    }


    @Override public Symbole visit(Sizeof sizeof, Tds tds){
        // Symbole nodeIdentifier = this.nextState();
        // this.addNode(nodeIdentifier, "Sizeof");
        // this.addTransition(nodeIdentifier, sizeof.name.accept(this));
        return new SymboleName(""); 
    }


    @Override public Symbole visit(IdfParenthesis idfParenthesis, Tds tds){
        // Symbole nodeIdentifier = this.nextState();
        // this.addTransition(nodeIdentifier, idfParenthesis.idf.accept(this));
        // for (Ast ast:idfParenthesis.exprList, Tds tds){
        //     Symbole astState = ast.accept(this);
        //     this.addTransition(nodeIdentifier, astState);
        // }
        return new SymboleName(""); 
    }


    @Override public Symbole visit(IdfParenthesisEmpty idfParenthesisEmpty, Tds tds){
        // Symbole nodeIdentifier = this.nextState();
        // this.addTransition(nodeIdentifier, idfParenthesisEmpty.idf.accept(this));
        return new SymboleName(""); 
    }


    @Override public Symbole visit(IfThen ifThen, Tds tds){
       
        Tds newTds = new Tds(tds);
        SymboleBlocAnonyme bloc = new SymboleBlocAnonyme(newTds);
        bloc.addDefinitionLine(ifThen.line);
        try {
            tds.addSymbole("ifThen", bloc); // il n'y aura qu'au plus un symbole nommé ifThen dans la tds
        } catch (SymbolAlreadyExistsException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } 
        ifThen.thenBlock.accept(this, newTds);

        return null; 
    }


    @Override public Symbole visit(IfThenElse ifThenElse, Tds tds){

        Tds newTds = new Tds(tds);
        Tds newTdsElse = new Tds(tds);
        SymboleBlocAnonyme blocThen = new SymboleBlocAnonyme(newTds);
        SymboleBlocAnonyme blocElse = new SymboleBlocAnonyme(newTdsElse);
        blocThen.addDefinitionLine(ifThenElse.lineIf);
        blocElse.addDefinitionLine(ifThenElse.lineElse);
        try {
            tds.addSymbole("ifThen", blocThen); // il n'y aura qu'au plus un symbole nommé ifThenElse dans la tds
            tds.addSymbole("ifElse", blocElse); // il n'y aura qu'au plus un symbole nommé ifElse dans la tds
        } catch (SymbolAlreadyExistsException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } 
        
        ifThenElse.thenBlock.accept(this, newTds);
        ifThenElse.elseBlock.accept(this, newTdsElse);

        return null; 
    }
    @Override public Symbole visit(While while1, Tds tds){

        Tds newTds = new Tds(tds);
        SymboleBlocAnonyme bloc = new SymboleBlocAnonyme(newTds);
        bloc.addDefinitionLine(while1.line);
        try {
            tds.addSymbole("While", bloc); // il n'y aura qu'au plus un symbole nommé While dans la tds
        } catch (SymbolAlreadyExistsException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } 
        
        while1.doBlock.accept(this, newTds);

        return null; 
    }


    @Override public Symbole visit(Return return1, Tds tds){
        // Symbole nodeIdentifier = this.nextState();
        // this.addNode(nodeIdentifier, "Return");
        // this.addTransition(nodeIdentifier, return1.expr.accept(this));
        return new SymboleName(""); 
    }

    @Override public Symbole visit(Bloc bloc, Tds tds){

        int i = 0;
        ArrayList<Ast> listeAst =  bloc.instList ; 
        int longueurListe = listeAst.size();
        Ast ast = listeAst.get(i);  
        int deplacement = 0; 
        while( i < longueurListe && ((ast instanceof DeclVarInt) || (ast instanceof DeclVarStruct))){ // Tant qu'objet de types decl_vars
            ListeSymbole listeSymbole = (ListeSymbole) ast.accept(this, tds);
            for(Symbole symbole : listeSymbole.getList()){
                addSymboleIntStructToTds(symbole,deplacement,tds,"visit(Bloc...)");
                deplacement += OFFSETVALUE; 
            }
            i ++;
            if(i < longueurListe) ast = listeAst.get(i);
        }

        // types instructions

        while(i < longueurListe){
            Ast astInstruction = listeAst.get(i);
            i++;
            // On ne va modifier la TDS que pour les instructions générants un nouveau bloc
            if(astInstruction instanceof IfThen){
                astInstruction.accept(this, tds);
            }

            else if (astInstruction instanceof IfThenElse){
                astInstruction.accept(this, tds);
            }

            else if (astInstruction instanceof While){
                astInstruction.accept(this, tds);
            }

            else if (astInstruction instanceof Bloc){
                Tds newTds = new Tds(tds);
                Symbole symbole = astInstruction.accept(this, newTds);
                symbole.addDefinitionLine(bloc.line);
                try {
                    tds.addSymbole("Bloc", symbole); // il n'y aura qu'au plus un symbole nommé bloc dans la tds
                } catch (SymbolAlreadyExistsException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

        }

        return new SymboleBlocAnonyme(tds); 
    }
    @Override public Symbole visit(CharNode charNode, Tds tds){
        // Symbole nodeIdentifier = this.nextState();
        // this.addNode(nodeIdentifier, charNode.Symbole);
        
        return new SymboleName(""); 
    }
    @Override public Symbole visit(IntNode intNode, Tds tds){
        // Symbole nodeIdentifier = this.nextState();

        // this.addNode(nodeIdentifier, Symbole.valueOf(intNode.parseInt));

        return new SymboleName(""); 

    }

    @Override public Symbole visit(Affectation affectation, Tds tds){
        // Symbole nodeIdentifier = this.nextState();

        // Symbole idfState = affectation.left.accept(this);
        // Symbole expressionState = affectation.right.accept(this);

        // this.addNode(nodeIdentifier, "Affectation");
        // this.addTransition(nodeIdentifier, idfState);
        // this.addTransition(nodeIdentifier, expressionState);

        return new SymboleName(""); 
    }

    @Override public Symbole visit(Expr_ou expr_ou, Tds tds){
        // Symbole nodeIdentifier = this.nextState();
        // this.addNode(nodeIdentifier, "||");
        // this.addTransition(nodeIdentifier, expr_ou.left.accept(this));
        // this.addTransition(nodeIdentifier, expr_ou.right.accept(this));
        return new SymboleName(""); 
    }

    @Override public Symbole visit(Expr_et expr_et, Tds tds){
        // Symbole nodeIdentifier = this.nextState();
        // this.addNode(nodeIdentifier, "&&");
        // this.addTransition(nodeIdentifier, expr_et.left.accept(this));
        // this.addTransition(nodeIdentifier, expr_et.right.accept(this));
        return new SymboleName(""); 
    }

    @Override public Symbole visit(Egal egal, Tds tds){
        // Symbole nodeIdentifier = this.nextState();
        // this.addNode(nodeIdentifier, "==");
        // this.addTransition(nodeIdentifier, egal.left.accept(this));
        // this.addTransition(nodeIdentifier, egal.right.accept(this));
        return new SymboleName(""); 
    }

    @Override public Symbole visit(Different dif, Tds tds){
        // Symbole nodeIdentifier = this.nextState();
        // this.addNode(nodeIdentifier, "!=");
        // this.addTransition(nodeIdentifier, dif.left.accept(this));
        // this.addTransition(nodeIdentifier, dif.right.accept(this));
        return new SymboleName(""); 
    }

    @Override public Symbole visit(Inferieur inf, Tds tds){
        // Symbole nodeIdentifier = this.nextState();
        // this.addNode(nodeIdentifier, "<");
        // this.addTransition(nodeIdentifier, inf.left.accept(this));
        // this.addTransition(nodeIdentifier, inf.right.accept(this));
        return new SymboleName(""); 
    }

    @Override public Symbole visit (InferieurEgal infEgal, Tds tds){
        // Symbole nodeIdentifier = this.nextState();
        // this.addNode(nodeIdentifier, "<=");
        // this.addTransition(nodeIdentifier, infEgal.left.accept(this));
        // this.addTransition(nodeIdentifier, infEgal.right.accept(this));
        return new SymboleName(""); 
    }

    @Override public Symbole visit (Superieur sup, Tds tds){
        // Symbole nodeIdentifier = this.nextState();
        // this.addNode(nodeIdentifier, ">");
        // this.addTransition(nodeIdentifier, sup.left.accept(this));
        // this.addTransition(nodeIdentifier, sup.right.accept(this));
        return new SymboleName(""); 
    }

    @Override public Symbole visit (SuperieurEgal supEgal, Tds tds){
        // Symbole nodeIdentifier = this.nextState();
        // this.addNode(nodeIdentifier, ">=");
        // this.addTransition(nodeIdentifier, supEgal.left.accept(this));
        // this.addTransition(nodeIdentifier, supEgal.right.accept(this));
        return new SymboleName(""); 
    }

    @Override public Symbole visit (Plus plus, Tds tds){
        // Symbole nodeIdentifier = this.nextState();

        // Symbole leftState = plus.left.accept(this);
        // Symbole rightState = plus.right.accept(this);

        // this.addNode(nodeIdentifier, "+");
        
        // this.addTransition(nodeIdentifier, leftState);
        // this.addTransition(nodeIdentifier, rightState);

        return new SymboleName(""); 
    }

    @Override public Symbole visit (Minus minus, Tds tds){
        // Symbole nodeIdentifier = this.nextState();

        // Symbole leftState = minus.left.accept(this);
        // Symbole rightState = minus.right.accept(this);

        // this.addNode(nodeIdentifier, "-");
        
        // this.addTransition(nodeIdentifier, leftState);
        // this.addTransition(nodeIdentifier, rightState);

        return new SymboleName(""); 
    }

    @Override public Symbole visit (Division div, Tds tds){
        // Symbole nodeIdentifier = this.nextState();

        // Symbole leftState = div.left.accept(this);
        // Symbole rightState = div.right.accept(this);

        // this.addNode(nodeIdentifier, "/");
        
        // this.addTransition(nodeIdentifier, leftState);
        // this.addTransition(nodeIdentifier, rightState);

        return new SymboleName(""); 
    }

    @Override public Symbole visit (Multiplication mult, Tds tds){
        // Symbole nodeIdentifier = this.nextState();

        // Symbole leftState = mult.left.accept(this);
        // Symbole rightState = mult.right.accept(this);

        // this.addNode(nodeIdentifier, "*");
        
        // this.addTransition(nodeIdentifier, leftState);
        // this.addTransition(nodeIdentifier, rightState);

        return new SymboleName(""); 
    }

    @Override public Symbole visit (Fleche fleche, Tds tds){
        // Symbole nodeIdentifier = this.nextState();
        // this.addNode(nodeIdentifier, "->");
        // this.addTransition(nodeIdentifier, fleche.left.accept(this));
        // this.addTransition(nodeIdentifier, fleche.right.accept(this));
        return new SymboleName(""); 
    }

    @Override public Symbole visit (MoinsUnaire unaire, Tds tds){
        // Symbole nodeIdentifier = this.nextState();
        // this.addNode(nodeIdentifier, "-");
        // this.addTransition(nodeIdentifier, unaire.noeud.accept(this));
        return new SymboleName(""); 
    }
    
    @Override public Symbole visit (Negation unaire, Tds tds){
        // Symbole nodeIdentifier = this.nextState();
        // this.addNode(nodeIdentifier, "!");
        // this.addTransition(nodeIdentifier, unaire.noeud.accept(this));
        return new SymboleName(""); 
    }

    @Override
    public Symbole visit(Semicolon semicolon, Tds tds) {
        // TODO Auto-generated method stub
        return new SymboleName("");
    }
    
}
