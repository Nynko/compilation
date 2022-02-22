package compilateur.ARMGenerator;

import compilateur.ast.Affectation;
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
import compilateur.tds.Symbole;
import compilateur.tds.SymboleVar;
import compilateur.tds.Tds;

public class ARMGenerator implements ARMVisitor<String> {

    private StringAggregator stringAggregator;

    public ARMGenerator() {
        stringAggregator = new StringAggregator();
    }

    @Override
    public String visit(Fichier fichier, Tds tds) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visit(Idf idf, Tds tds) {
        String bp = "R12";
        int imbrication = tds.findImbrication(idf.name);
        // TODO diplay[imbrication] ou remonter tds.getImbrication() - imbrication
        // chainage statique pour mettre a jour bp
        int deplacement = 0;
        Symbole s = tds.findSymbole(idf.name);
        if (s instanceof SymboleVar sv) {
            deplacement = sv.getDeplacement();
        }

        return String.format("LDR R0, [%s #-%d]\n", bp, deplacement);
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
        return String.format("LDR R0, =%d\n", intNode.parseInt);
    }

    @Override
    public String visit(Affectation affectation, Tds tds) {
        StringBuilder sb = new StringBuilder();
        // on recupere le code assembleur de la partie droite de l'affectation, le code
        // retourne le resultat de l'expression dans R0
        sb.append(affectation.right.accept(this, tds));

        // on recupere l'adresse de la base et le décalage de ce qu'il y à gauche
        int decalage = 0;
        String bp = "R11";
        int imbrication;

        if (affectation.left instanceof Idf idf) {
            imbrication = tds.findImbrication(idf.name);
            // TODO diplay[imbrication] ou remonter tds.getImbrication() - imbrication
            // chainage statique pour mettre a jour bp
            // chainage statique
            if (tds.getImbrication() - imbrication != 0){
                sb.append("MOV [R11, #4] , R7");
                for (int i = 0; i < tds.getImbrication() - imbrication -1; i++) {
                    sb.append("MOVE [R7], R7");
                }
                bp = "R7";
            }
            Symbole s = tds.findSymbole(idf.name);
            if (s instanceof SymboleVar sv) {
                decalage = sv.getDeplacement();
            }
        } else if (affectation.left instanceof Fleche fleche) {
            // a->b ou type(b) = int
            // decalage = decalage de a + decalage de b
            // @a->b = base de declaration de a + decalage 
            Idf idf = (Idf) fleche.right;
            Symbole s = tds.findSymbole(idf.name);

            if (s instanceof SymboleVar sv) {
                decalage = sv.getDeplacement();
            }
        }
        sb.append(String.format("STR R0, [%s #%d]", bp, decalage));
        return sb.toString();
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
