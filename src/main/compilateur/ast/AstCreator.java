package compilateur.ast;

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
	//@Override public Ast enterDecl(circParser.DeclContext ctx) { }
	
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
	//@Override public Ast enterDecl_vars(circParser.Decl_varsContext ctx) { }
	
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	//@Override public Ast exitDecl_vars(circParser.Decl_varsContext ctx) { }
	
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	//@Override public Ast enterDecl_typ(circParser.Decl_typContext ctx) { }
	
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	//@Override public Ast exitDecl_typ(circParser.Decl_typContext ctx) { }
	
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	//@Override public Ast enterDecl_fct(circParser.Decl_fctContext ctx) { }
	
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