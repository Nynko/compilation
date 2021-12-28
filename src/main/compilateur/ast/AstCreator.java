package compilateur.ast;

import java.util.ArrayList;

import org.antlr.v4.runtime.misc.Interval;

import compilateur.grammar.circBaseVisitor;
import compilateur.grammar.circParser;

public class AstCreator extends circBaseVisitor<Ast>{


	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public Ast visitFichier(circParser.FichierContext ctx) {
		ArrayList<Ast> prgm = new ArrayList<>();
		for(int i= 0 ; i <ctx.getChildCount()-1 ; i++){
			prgm.add(ctx.getChild(i).accept(this));
		}
		return new Fichier(prgm);
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public Ast visitDecl(circParser.DeclContext ctx) { return ctx.getChild(0).accept(this); }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public Ast visitDeclVarInt(circParser.DeclVarIntContext ctx) { 
		ArrayList<Ast> idf = new ArrayList<>();
		int line = ctx.getStart().getLine();
		for (int i = 1; i < ctx.getChildCount() - 1; i += 2) {
			idf.add(new Idf(ctx.getChild(i).toString()));
		}
		return new DeclVarInt(idf,line);
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public Ast visitDeclVarStruct(circParser.DeclVarStructContext ctx) { 
		ArrayList<Ast> idf = new ArrayList<>();
		idf.add(new Idf(ctx.getChild(1).toString()));
		int line = ctx.getStart().getLine();
		for (int i = 3; i < ctx.getChildCount() - 1; i += 3) {
			idf.add(new Idf(ctx.getChild(i).toString()));
		}
		return new DeclVarStruct(idf,line);
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public Ast visitDecl_typ(circParser.Decl_typContext ctx) { 
		String StrIdf = ctx.getChild(1).toString();
		Ast idf = new Idf(StrIdf);
		int line = ctx.getStart().getLine();

		ArrayList<Ast> decl = new ArrayList<>();
		// int i = 3;
		for (int j = 3; j < ctx.getChildCount()-2; j++) {
			decl.add(ctx.getChild(j).accept(this));
		}
		// while(!ctx.getChild(i).equals('}')) {
		// 	decl.add(ctx.getChild(i).accept(this));
		// 	i++;
		// }
		return new Decl_typ(idf,decl,line);
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public Ast visitDeclFctInt(circParser.DeclFctIntContext ctx) {
		Ast idf = new Idf(ctx.getChild(1).toString());
		Ast param = ctx.getChild(3).accept(this);
		Ast bloc = ctx.getChild(5).accept(this);
		int line = ctx.getStart().getLine();
		return new DeclFctInt(idf, param, bloc,line);
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public Ast visitDeclFctStruct(circParser.DeclFctStructContext ctx) {
		Ast idf0 = new Idf(ctx.getChild(1).toString());
		Ast idf1 = new Idf(ctx.getChild(3).toString());
		Ast param = ctx.getChild(5).accept(this);
		Ast bloc = ctx.getChild(7).accept(this);
		int line = ctx.getStart().getLine();
		return new DeclFctStruct(idf0, idf1, param, bloc,line);
	 }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public Ast visitParamListMulti(circParser.ParamListMultiContext ctx) {
		ArrayList<Ast> paramList = new ArrayList<Ast>();
		for (int i = 0; i < ctx.getChildCount(); i= i+2) {
			paramList.add(ctx.getChild(i).accept(this));
		}
		return new ParamListMulti(paramList);
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public Ast visitParamListNone(circParser.ParamListNoneContext ctx) { 
		return null;
	 }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public Ast visitParamInt(circParser.ParamIntContext ctx) { 
		int line = ctx.getStart().getLine();
		Ast idf = new Idf(ctx.getChild(1).toString());
		return new ParamInt(idf,line);
	 }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public Ast visitParamStruct(circParser.ParamStructContext ctx) {
		int line = ctx.getStart().getLine();
		Ast idf0 = new Idf(ctx.getChild(1).toString());
		Ast idf1 = new Idf(ctx.getChild(3).toString());
		return new ParamStruct(idf0, idf1, line);
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public Ast visitValue(circParser.ValueContext ctx) { return ctx.getChild(0).accept(this); }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public Ast visitIdf(circParser.IdfContext ctx) { return new Idf(ctx.getChild(0).toString());}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public Ast visitIdfParenthesisEmpty(circParser.IdfParenthesisEmptyContext ctx) { return new IdfParenthesisEmpty(new Idf(ctx.getChild(0).toString()),ctx.getStart().getLine()); }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public Ast visitIdfParenthesis(circParser.IdfParenthesisContext ctx) {
		Ast idf = new Idf(ctx.getChild(0).toString());
		int line = ctx.getStart().getLine();
		ArrayList<Ast> exprList = new ArrayList<Ast>();
		for (int i = 2; i < ctx.getChildCount(); i= i+2) {
			exprList.add(ctx.getChild(i).accept(this));
		}
		return new IdfParenthesis(idf, exprList, line);
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public Ast visitSizeof(circParser.SizeofContext ctx) { return new Sizeof(new Idf(ctx.getChild(3).toString()),ctx.getStart().getLine()); }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public Ast visitParenthesis(circParser.ParenthesisContext ctx) { return ctx.getChild(1).accept(this); }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public Ast visitSemicolon(circParser.SemicolonContext ctx) { return new Semicolon(ctx.getStart().getLine());}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public Ast visitInstExpr(circParser.InstExprContext ctx) { return ctx.getChild(0).accept(this); }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public Ast visitIfThen(circParser.IfThenContext ctx) {
		Ast condition = ctx.getChild(2).accept(this);
		Ast thenBlock = ctx.getChild(4).accept(this);
		int line = ctx.getStart().getLine();
		return new IfThen(condition, thenBlock, line);

	 }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public Ast visitIfThenElse(circParser.IfThenElseContext ctx) {
		Ast condition = ctx.getChild(2).accept(this);
		Ast thenBlock = ctx.getChild(4).accept(this);
		Ast elseBlock = ctx.getChild(6).accept(this);
		int line = ctx.getStart().getLine();
		int lineElse = ctx.getStop().getLine();
		return new IfThenElse(condition, thenBlock, elseBlock, line, lineElse);
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public Ast visitWhile(circParser.WhileContext ctx) {
		Ast condition = ctx.getChild(2).accept(this);
		Ast doBlock = ctx.getChild(4).accept(this);
		int line = ctx.getStart().getLine();
		return new While(condition, doBlock, line);
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public Ast visitInstBloc(circParser.InstBlocContext ctx) { return ctx.getChild(0).accept(this); }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public Ast visitReturn(circParser.ReturnContext ctx) {
		Ast expr = ctx.getChild(1).accept(this);
		int line = ctx.getStart().getLine();
		return new Return(expr,line);
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public Ast visitBloc(circParser.BlocContext ctx) {
		ArrayList<Ast> instList = new ArrayList<Ast>();
		int line = ctx.getStart().getLine();
		for (int i = 1; i < ctx.getChildCount()-1; i++) {
			instList.add(ctx.getChild(i).accept(this));
		}
		return new Bloc(instList, line);
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public Ast visitExpr(circParser.ExprContext ctx) { return ctx.getChild(0).accept(this); }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public Ast visitAffectation(circParser.AffectationContext ctx) { 
		Ast noeudTemporaire = ctx.getChild(ctx.getChildCount()-1).accept(this);
		for (int i=ctx.getChildCount()-3; i >= 0; i=i-2) {
			Ast right = ctx.getChild(i).accept(this);
			noeudTemporaire = new Affectation(right, noeudTemporaire);
		}
		return noeudTemporaire;
		
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public Ast visitExpr_ou(circParser.Expr_ouContext ctx) { 
		Ast noeudTemporaire = ctx.getChild(0).accept(this);
		for (int i = 0; i * 2 < ctx.getChildCount() - 1; i++) {
			Ast right = ctx.getChild(2*(i+1)).accept(this);
			noeudTemporaire = new Expr_ou(noeudTemporaire, right);
		}
		return noeudTemporaire;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public Ast visitExpr_et(circParser.Expr_etContext ctx) { 
		Ast noeudTemporaire = ctx.getChild(0).accept(this);
		for (int i = 0; i * 2 < ctx.getChildCount() - 1; i++) {
			Ast right = ctx.getChild(2*(i+1)).accept(this);
			noeudTemporaire = new Expr_et(noeudTemporaire, right);
		}
		return noeudTemporaire;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public Ast visitComparaison(circParser.ComparaisonContext ctx) { 
		Ast noeudTemporaire = ctx.getChild(0).accept(this);
		for (int i = 0; i * 2 < ctx.getChildCount() - 1; i++) {
			String signe = ctx.getChild(2 * i + 1).toString();
			Ast right = ctx.getChild(2*(i+1)).accept(this);
			switch (signe) {
				case "==" :
					noeudTemporaire = new Egal(noeudTemporaire, right);
					break;
				case "!=" :
					noeudTemporaire = new Different(noeudTemporaire, right);
					break;
				default :
					break;
			}
		}
		return noeudTemporaire;}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public Ast visitOrdre(circParser.OrdreContext ctx) { 
		Ast noeudTemporaire = ctx.getChild(0).accept(this);
		for (int i = 0; i * 2 < ctx.getChildCount() - 1; i++) {

			String signe = ctx.getChild(2 * i + 1).toString();
			Ast right = ctx.getChild(2*(i+1)).accept(this);
			switch (signe) {
				case "<" :
					noeudTemporaire = new Inferieur(noeudTemporaire, right);
					break;
				case "<=" :
					noeudTemporaire = new InferieurEgal(noeudTemporaire, right);
					break;
				case ">" :
					noeudTemporaire = new Superieur(noeudTemporaire, right);
					break;
				case ">=" :
					noeudTemporaire = new SuperieurEgal(noeudTemporaire, right);
				default :
					break;
			}
		}
		return noeudTemporaire; }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public Ast visitAddition(circParser.AdditionContext ctx) { 
		Ast noeudTemporaire = ctx.getChild(0).accept(this);
		for (int i = 0; i * 2 < ctx.getChildCount() - 1; i++) {

			String signe = ctx.getChild(2 * i + 1).toString();
			Ast right = ctx.getChild(2*(i+1)).accept(this);
			switch (signe) {
				case "+" :
					noeudTemporaire = new Plus(noeudTemporaire, right);
					break;
				case "-":
					noeudTemporaire = new Minus(noeudTemporaire, right);
					break;
				default :
					break;
			}
		}
		return noeudTemporaire; 
}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public Ast visitMultiplication(circParser.MultiplicationContext ctx) { 
		Ast noeudTemporaire = ctx.getChild(0).accept(this);
		for (int i = 0; i * 2 < ctx.getChildCount() - 1; i++) {
			String signe = ctx.getChild(2 * i + 1).toString();
			Ast right = ctx.getChild(2*(i+1)).accept(this);
			switch (signe) {
				case "*" :
					noeudTemporaire = new Multiplication(noeudTemporaire, right);
					break;
				case "/":
					noeudTemporaire = new Division(noeudTemporaire, right);
					break;
				default :
					break;
			}
		}
		return noeudTemporaire; 
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public Ast visitUnaire(circParser.UnaireContext ctx) { 
		Ast noeud; 
		if (ctx.getChildCount() == 1) {
			noeud = ctx.getChild(0).accept(this);
			return noeud;
		}
		else {
			String signe = ctx.getChild(0).toString();
			ctx.children.remove(0);
			switch (signe) {
				case "!" :
					noeud = new Negation(ctx.accept(this));
					break;
				case "-":
					noeud = new MoinsUnaire(ctx.accept(this));
					break;
				default :
					throw new Error();
			}
			return noeud;
		}
		
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public Ast visitFleche(circParser.FlecheContext ctx) { 
		
		Ast noeudTemporaire = ctx.getChild(0).accept(this);
		for (int i = 1; i * 2 < ctx.getChildCount(); i++) {
			Ast right = new Idf(ctx.getChild(2*i).toString());
			noeudTemporaire = new Fleche(noeudTemporaire, right);
		}
		return noeudTemporaire;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public Ast visitInteger(circParser.IntegerContext ctx) { return new IntNode(Integer.parseInt(ctx.getChild(0).toString()),ctx.getStart().getLine()); }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public Ast visitChar(circParser.CharContext ctx) { return new CharNode(ctx.getChild(0).toString(), ctx.getStart().getLine()); }
	
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	//@Override public Ast enterFichier(circParser.FichierContext ctx) { }
	
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	//@Override public Ast exitFichier(circParser.FichierContext ctx) { }
	
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	// @Override public Ast enterDecl(circParser.DeclContext ctx) { return ctx.getChild(0).accept(this); }
	
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	//@Override public Ast exitDecl(circParser.DeclContext ctx) { }
	
	
	
	
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	//@Override public Ast exitDecl_fct(circParser.Decl_fctContext ctx) { }
	
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	//@Override public Ast enterParam_liste(circParser.Param_listeContext ctx) { }
	
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	//@Override public Ast exitParam_liste(circParser.Param_listeContext ctx) { }
	
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	//@Override public Ast enterParam(circParser.ParamContext ctx) { }
	
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	//@Override public Ast exitParam(circParser.ParamContext ctx) { }
	
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	//@Override public Ast enterExpr_primaire(circParser.Expr_primaireContext ctx) { }
	
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	//@Override public Ast exitExpr_primaire(circParser.Expr_primaireContext ctx) { }
	
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	//@Override public Ast enterInstruction(circParser.InstructionContext ctx) { }
	
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	//@Override public Ast exitInstruction(circParser.InstructionContext ctx) { }
	
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	//@Override public Ast enterBloc(circParser.BlocContext ctx) { }
	
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	//@Override public Ast exitBloc(circParser.BlocContext ctx) { }
	
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	//@Override public Ast enterExpr(circParser.ExprContext ctx) { }
	
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	//@Override public Ast exitExpr(circParser.ExprContext ctx) { }
	
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	//@Override public Ast enterAffectation(circParser.AffectationContext ctx) { }
	
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	//@Override public Ast exitAffectation(circParser.AffectationContext ctx) { }
	
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	//@Override public Ast enterExpr_ou(circParser.Expr_ouContext ctx) { }
	
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	//@Override public Ast exitExpr_ou(circParser.Expr_ouContext ctx) { }
	
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	//@Override public Ast enterExpr_et(circParser.Expr_etContext ctx) { }
	
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	//@Override public Ast exitExpr_et(circParser.Expr_etContext ctx) { }
	
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	//@Override public Ast enterComparaison(circParser.ComparaisonContext ctx) { }
	
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	//@Override public Ast exitComparaison(circParser.ComparaisonContext ctx) { }
	
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	//@Override public Ast enterOrdre(circParser.OrdreContext ctx) { }
	
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	//@Override public Ast exitOrdre(circParser.OrdreContext ctx) { }
	
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	//@Override public Ast enterAddition(circParser.AdditionContext ctx) { }
	
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	//@Override public Ast exitAddition(circParser.AdditionContext ctx) { }
	
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	//@Override public Ast enterMultiplication(circParser.MultiplicationContext ctx) { }
	
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	//@Override public Ast exitMultiplication(circParser.MultiplicationContext ctx) { }
	
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	//@Override public Ast enterUnaire(circParser.UnaireContext ctx) { }
	
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	//@Override public Ast exitUnaire(circParser.UnaireContext ctx) { }
	
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	//@Override public Ast enterFleche(circParser.FlecheContext ctx) { }
	
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	//@Override public Ast exitFleche(circParser.FlecheContext ctx) { }
	
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	//@Override public Ast enterValeur(circParser.ValeurContext ctx) { }
	
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	//@Override public Ast exitValeur(circParser.ValeurContext ctx) { }

	
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	//@Override public Ast enterEveryRule(ParserRuleContext ctx) { }
	
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	//@Override public Ast exitEveryRule(ParserRuleContext ctx) { }
	
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	//@Override public Ast visitTerminal(TerminalNode node) { }
	
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	//@Override public Ast visitErrorNode(ErrorNode node) { }
}