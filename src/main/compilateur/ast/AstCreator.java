package compilateur.ast;

import java.util.ArrayList;

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
		Ast prgm = ctx.getChild(0).accept(this);
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
		for (int i = 1; i < ctx.getChildCount(); i += 2) {
			idf.add(new Idf(ctx.getChild(i).toString()));
		}
		return new DeclVarInt(idf);
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
		for (int i = 3; i < ctx.getChildCount(); i += 3) {
			idf.add(new Idf(ctx.getChild(1).toString()));
		}
		return new DeclVarStruct(idf);
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

		ArrayList<Ast> decl = new ArrayList<>();
		int i = 3;
		while(!ctx.getChild(i).equals('}')) {
			decl.add(ctx.getChild(i).accept(this));
			i++;
		}
		return new Decl_typ(idf,decl);
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
		return new DeclFctInt(idf, param, bloc);
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
		return new DeclFctStruct(idf0, idf1, param, bloc);
	 }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public Ast visitParamListMulti(circParser.ParamListMultiContext ctx) {
		ArrayList<Ast> paramList = new ArrayList<Ast>();
		for (int i = 0; i < ctx.getChildCount(); i++) {
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
	@Override public Ast visitParamListNone(circParser.ParamListNoneContext ctx) { return ctx.accept(this);}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public Ast visitParamInt(circParser.ParamIntContext ctx) { 
		Ast idf = new Idf(ctx.getChild(1).toString());
		return new ParamInt(idf);
	 }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public Ast visitParamStruct(circParser.ParamStructContext ctx) {
		Ast idf0 = new Idf(ctx.getChild(1).toString());
		Ast idf1 = new Idf(ctx.getChild(3).toString());
		return new ParamStruct(idf0, idf1);
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
	@Override public Ast visitIdfParenthesisEmpty(circParser.IdfParenthesisEmptyContext ctx) { return new IdfParenthesisEmpty(new Idf(ctx.getChild(0).toString())); }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public Ast visitIdfParenthesis(circParser.IdfParenthesisContext ctx) {
		Ast idf = new Idf(ctx.getChild(0).toString());
		ArrayList<Ast> exprList = new ArrayList<Ast>();
		for (int i = 2; i < ctx.getChildCount(); i= i+2) {
			exprList.add(ctx.getChild(i).accept(this));
		}
		return new IdfParenthesis(idf, exprList);
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public Ast visitSizeof(circParser.SizeofContext ctx) { return new Sizeof(new Idf(ctx.getChild(3).toString())); }
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
	@Override public Ast visitSemicolon(circParser.SemicolonContext ctx) { return ctx.getChild(0).accept(this);}
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
		return new IfThen(condition, thenBlock);

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
		return new IfThenElse(condition, thenBlock, elseBlock);
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
		return new While(condition, doBlock);
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
		return new Return(expr);
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public Ast visitBloc(circParser.BlocContext ctx) {
		ArrayList<Ast> instList = new ArrayList<Ast>();
		for (int i = 1; i < ctx.getChildCount()-1; i++) {
			instList.add(ctx.getChild(i).accept(this));
		}
		return new Bloc(instList);
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
	@Override public Ast visitAffectation(circParser.AffectationContext ctx) { return visitChildren(ctx); }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public Ast visitExpr_ou(circParser.Expr_ouContext ctx) { return visitChildren(ctx); }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public Ast visitExpr_et(circParser.Expr_etContext ctx) { return visitChildren(ctx); }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public Ast visitComparaison(circParser.ComparaisonContext ctx) { return visitChildren(ctx); }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public Ast visitOrdre(circParser.OrdreContext ctx) { return visitChildren(ctx); }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public Ast visitAddition(circParser.AdditionContext ctx) { return visitChildren(ctx); }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public Ast visitMultiplication(circParser.MultiplicationContext ctx) { return visitChildren(ctx); }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public Ast visitUnaire(circParser.UnaireContext ctx) { return visitChildren(ctx); }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public Ast visitFleche(circParser.FlecheContext ctx) { return visitChildren(ctx); }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public Ast visitInteger(circParser.IntegerContext ctx) { return new IntNode(Integer.parseInt(ctx.getChild(0).toString())); }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public Ast visitChar(circParser.CharContext ctx) { return new CharNode(ctx.getChild(0).toString()); }
	
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