// Generated from grammar/circ.g4 by ANTLR 4.7.2

    package main.grammar;
 
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link circParser}.
 */
public interface circListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link circParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterPrimaryExpression(circParser.PrimaryExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link circParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitPrimaryExpression(circParser.PrimaryExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link circParser#genericSelection}.
	 * @param ctx the parse tree
	 */
	void enterGenericSelection(circParser.GenericSelectionContext ctx);
	/**
	 * Exit a parse tree produced by {@link circParser#genericSelection}.
	 * @param ctx the parse tree
	 */
	void exitGenericSelection(circParser.GenericSelectionContext ctx);
	/**
	 * Enter a parse tree produced by {@link circParser#genericAssocList}.
	 * @param ctx the parse tree
	 */
	void enterGenericAssocList(circParser.GenericAssocListContext ctx);
	/**
	 * Exit a parse tree produced by {@link circParser#genericAssocList}.
	 * @param ctx the parse tree
	 */
	void exitGenericAssocList(circParser.GenericAssocListContext ctx);
	/**
	 * Enter a parse tree produced by {@link circParser#genericAssociation}.
	 * @param ctx the parse tree
	 */
	void enterGenericAssociation(circParser.GenericAssociationContext ctx);
	/**
	 * Exit a parse tree produced by {@link circParser#genericAssociation}.
	 * @param ctx the parse tree
	 */
	void exitGenericAssociation(circParser.GenericAssociationContext ctx);
	/**
	 * Enter a parse tree produced by {@link circParser#postfixExpression}.
	 * @param ctx the parse tree
	 */
	void enterPostfixExpression(circParser.PostfixExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link circParser#postfixExpression}.
	 * @param ctx the parse tree
	 */
	void exitPostfixExpression(circParser.PostfixExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link circParser#argumentExpressionList}.
	 * @param ctx the parse tree
	 */
	void enterArgumentExpressionList(circParser.ArgumentExpressionListContext ctx);
	/**
	 * Exit a parse tree produced by {@link circParser#argumentExpressionList}.
	 * @param ctx the parse tree
	 */
	void exitArgumentExpressionList(circParser.ArgumentExpressionListContext ctx);
	/**
	 * Enter a parse tree produced by {@link circParser#unaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterUnaryExpression(circParser.UnaryExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link circParser#unaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitUnaryExpression(circParser.UnaryExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link circParser#unaryOperator}.
	 * @param ctx the parse tree
	 */
	void enterUnaryOperator(circParser.UnaryOperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link circParser#unaryOperator}.
	 * @param ctx the parse tree
	 */
	void exitUnaryOperator(circParser.UnaryOperatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link circParser#castExpression}.
	 * @param ctx the parse tree
	 */
	void enterCastExpression(circParser.CastExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link circParser#castExpression}.
	 * @param ctx the parse tree
	 */
	void exitCastExpression(circParser.CastExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link circParser#multiplicativeExpression}.
	 * @param ctx the parse tree
	 */
	void enterMultiplicativeExpression(circParser.MultiplicativeExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link circParser#multiplicativeExpression}.
	 * @param ctx the parse tree
	 */
	void exitMultiplicativeExpression(circParser.MultiplicativeExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link circParser#additiveExpression}.
	 * @param ctx the parse tree
	 */
	void enterAdditiveExpression(circParser.AdditiveExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link circParser#additiveExpression}.
	 * @param ctx the parse tree
	 */
	void exitAdditiveExpression(circParser.AdditiveExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link circParser#shiftExpression}.
	 * @param ctx the parse tree
	 */
	void enterShiftExpression(circParser.ShiftExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link circParser#shiftExpression}.
	 * @param ctx the parse tree
	 */
	void exitShiftExpression(circParser.ShiftExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link circParser#relationalExpression}.
	 * @param ctx the parse tree
	 */
	void enterRelationalExpression(circParser.RelationalExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link circParser#relationalExpression}.
	 * @param ctx the parse tree
	 */
	void exitRelationalExpression(circParser.RelationalExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link circParser#equalityExpression}.
	 * @param ctx the parse tree
	 */
	void enterEqualityExpression(circParser.EqualityExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link circParser#equalityExpression}.
	 * @param ctx the parse tree
	 */
	void exitEqualityExpression(circParser.EqualityExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link circParser#andExpression}.
	 * @param ctx the parse tree
	 */
	void enterAndExpression(circParser.AndExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link circParser#andExpression}.
	 * @param ctx the parse tree
	 */
	void exitAndExpression(circParser.AndExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link circParser#exclusiveOrExpression}.
	 * @param ctx the parse tree
	 */
	void enterExclusiveOrExpression(circParser.ExclusiveOrExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link circParser#exclusiveOrExpression}.
	 * @param ctx the parse tree
	 */
	void exitExclusiveOrExpression(circParser.ExclusiveOrExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link circParser#inclusiveOrExpression}.
	 * @param ctx the parse tree
	 */
	void enterInclusiveOrExpression(circParser.InclusiveOrExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link circParser#inclusiveOrExpression}.
	 * @param ctx the parse tree
	 */
	void exitInclusiveOrExpression(circParser.InclusiveOrExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link circParser#logicalAndExpression}.
	 * @param ctx the parse tree
	 */
	void enterLogicalAndExpression(circParser.LogicalAndExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link circParser#logicalAndExpression}.
	 * @param ctx the parse tree
	 */
	void exitLogicalAndExpression(circParser.LogicalAndExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link circParser#logicalOrExpression}.
	 * @param ctx the parse tree
	 */
	void enterLogicalOrExpression(circParser.LogicalOrExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link circParser#logicalOrExpression}.
	 * @param ctx the parse tree
	 */
	void exitLogicalOrExpression(circParser.LogicalOrExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link circParser#conditionalExpression}.
	 * @param ctx the parse tree
	 */
	void enterConditionalExpression(circParser.ConditionalExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link circParser#conditionalExpression}.
	 * @param ctx the parse tree
	 */
	void exitConditionalExpression(circParser.ConditionalExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link circParser#assignmentExpression}.
	 * @param ctx the parse tree
	 */
	void enterAssignmentExpression(circParser.AssignmentExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link circParser#assignmentExpression}.
	 * @param ctx the parse tree
	 */
	void exitAssignmentExpression(circParser.AssignmentExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link circParser#assignmentOperator}.
	 * @param ctx the parse tree
	 */
	void enterAssignmentOperator(circParser.AssignmentOperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link circParser#assignmentOperator}.
	 * @param ctx the parse tree
	 */
	void exitAssignmentOperator(circParser.AssignmentOperatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link circParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpression(circParser.ExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link circParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpression(circParser.ExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link circParser#constantExpression}.
	 * @param ctx the parse tree
	 */
	void enterConstantExpression(circParser.ConstantExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link circParser#constantExpression}.
	 * @param ctx the parse tree
	 */
	void exitConstantExpression(circParser.ConstantExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link circParser#declaration}.
	 * @param ctx the parse tree
	 */
	void enterDeclaration(circParser.DeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link circParser#declaration}.
	 * @param ctx the parse tree
	 */
	void exitDeclaration(circParser.DeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link circParser#declarationSpecifiers}.
	 * @param ctx the parse tree
	 */
	void enterDeclarationSpecifiers(circParser.DeclarationSpecifiersContext ctx);
	/**
	 * Exit a parse tree produced by {@link circParser#declarationSpecifiers}.
	 * @param ctx the parse tree
	 */
	void exitDeclarationSpecifiers(circParser.DeclarationSpecifiersContext ctx);
	/**
	 * Enter a parse tree produced by {@link circParser#declarationSpecifiers2}.
	 * @param ctx the parse tree
	 */
	void enterDeclarationSpecifiers2(circParser.DeclarationSpecifiers2Context ctx);
	/**
	 * Exit a parse tree produced by {@link circParser#declarationSpecifiers2}.
	 * @param ctx the parse tree
	 */
	void exitDeclarationSpecifiers2(circParser.DeclarationSpecifiers2Context ctx);
	/**
	 * Enter a parse tree produced by {@link circParser#declarationSpecifier}.
	 * @param ctx the parse tree
	 */
	void enterDeclarationSpecifier(circParser.DeclarationSpecifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link circParser#declarationSpecifier}.
	 * @param ctx the parse tree
	 */
	void exitDeclarationSpecifier(circParser.DeclarationSpecifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link circParser#initDeclaratorList}.
	 * @param ctx the parse tree
	 */
	void enterInitDeclaratorList(circParser.InitDeclaratorListContext ctx);
	/**
	 * Exit a parse tree produced by {@link circParser#initDeclaratorList}.
	 * @param ctx the parse tree
	 */
	void exitInitDeclaratorList(circParser.InitDeclaratorListContext ctx);
	/**
	 * Enter a parse tree produced by {@link circParser#initDeclarator}.
	 * @param ctx the parse tree
	 */
	void enterInitDeclarator(circParser.InitDeclaratorContext ctx);
	/**
	 * Exit a parse tree produced by {@link circParser#initDeclarator}.
	 * @param ctx the parse tree
	 */
	void exitInitDeclarator(circParser.InitDeclaratorContext ctx);
	/**
	 * Enter a parse tree produced by {@link circParser#storageClassSpecifier}.
	 * @param ctx the parse tree
	 */
	void enterStorageClassSpecifier(circParser.StorageClassSpecifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link circParser#storageClassSpecifier}.
	 * @param ctx the parse tree
	 */
	void exitStorageClassSpecifier(circParser.StorageClassSpecifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link circParser#typeSpecifier}.
	 * @param ctx the parse tree
	 */
	void enterTypeSpecifier(circParser.TypeSpecifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link circParser#typeSpecifier}.
	 * @param ctx the parse tree
	 */
	void exitTypeSpecifier(circParser.TypeSpecifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link circParser#structOrUnionSpecifier}.
	 * @param ctx the parse tree
	 */
	void enterStructOrUnionSpecifier(circParser.StructOrUnionSpecifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link circParser#structOrUnionSpecifier}.
	 * @param ctx the parse tree
	 */
	void exitStructOrUnionSpecifier(circParser.StructOrUnionSpecifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link circParser#structOrUnion}.
	 * @param ctx the parse tree
	 */
	void enterStructOrUnion(circParser.StructOrUnionContext ctx);
	/**
	 * Exit a parse tree produced by {@link circParser#structOrUnion}.
	 * @param ctx the parse tree
	 */
	void exitStructOrUnion(circParser.StructOrUnionContext ctx);
	/**
	 * Enter a parse tree produced by {@link circParser#structDeclarationList}.
	 * @param ctx the parse tree
	 */
	void enterStructDeclarationList(circParser.StructDeclarationListContext ctx);
	/**
	 * Exit a parse tree produced by {@link circParser#structDeclarationList}.
	 * @param ctx the parse tree
	 */
	void exitStructDeclarationList(circParser.StructDeclarationListContext ctx);
	/**
	 * Enter a parse tree produced by {@link circParser#structDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterStructDeclaration(circParser.StructDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link circParser#structDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitStructDeclaration(circParser.StructDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link circParser#specifierQualifierList}.
	 * @param ctx the parse tree
	 */
	void enterSpecifierQualifierList(circParser.SpecifierQualifierListContext ctx);
	/**
	 * Exit a parse tree produced by {@link circParser#specifierQualifierList}.
	 * @param ctx the parse tree
	 */
	void exitSpecifierQualifierList(circParser.SpecifierQualifierListContext ctx);
	/**
	 * Enter a parse tree produced by {@link circParser#structDeclaratorList}.
	 * @param ctx the parse tree
	 */
	void enterStructDeclaratorList(circParser.StructDeclaratorListContext ctx);
	/**
	 * Exit a parse tree produced by {@link circParser#structDeclaratorList}.
	 * @param ctx the parse tree
	 */
	void exitStructDeclaratorList(circParser.StructDeclaratorListContext ctx);
	/**
	 * Enter a parse tree produced by {@link circParser#structDeclarator}.
	 * @param ctx the parse tree
	 */
	void enterStructDeclarator(circParser.StructDeclaratorContext ctx);
	/**
	 * Exit a parse tree produced by {@link circParser#structDeclarator}.
	 * @param ctx the parse tree
	 */
	void exitStructDeclarator(circParser.StructDeclaratorContext ctx);
	/**
	 * Enter a parse tree produced by {@link circParser#enumSpecifier}.
	 * @param ctx the parse tree
	 */
	void enterEnumSpecifier(circParser.EnumSpecifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link circParser#enumSpecifier}.
	 * @param ctx the parse tree
	 */
	void exitEnumSpecifier(circParser.EnumSpecifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link circParser#enumeratorList}.
	 * @param ctx the parse tree
	 */
	void enterEnumeratorList(circParser.EnumeratorListContext ctx);
	/**
	 * Exit a parse tree produced by {@link circParser#enumeratorList}.
	 * @param ctx the parse tree
	 */
	void exitEnumeratorList(circParser.EnumeratorListContext ctx);
	/**
	 * Enter a parse tree produced by {@link circParser#enumerator}.
	 * @param ctx the parse tree
	 */
	void enterEnumerator(circParser.EnumeratorContext ctx);
	/**
	 * Exit a parse tree produced by {@link circParser#enumerator}.
	 * @param ctx the parse tree
	 */
	void exitEnumerator(circParser.EnumeratorContext ctx);
	/**
	 * Enter a parse tree produced by {@link circParser#enumerationConstant}.
	 * @param ctx the parse tree
	 */
	void enterEnumerationConstant(circParser.EnumerationConstantContext ctx);
	/**
	 * Exit a parse tree produced by {@link circParser#enumerationConstant}.
	 * @param ctx the parse tree
	 */
	void exitEnumerationConstant(circParser.EnumerationConstantContext ctx);
	/**
	 * Enter a parse tree produced by {@link circParser#atomicTypeSpecifier}.
	 * @param ctx the parse tree
	 */
	void enterAtomicTypeSpecifier(circParser.AtomicTypeSpecifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link circParser#atomicTypeSpecifier}.
	 * @param ctx the parse tree
	 */
	void exitAtomicTypeSpecifier(circParser.AtomicTypeSpecifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link circParser#typeQualifier}.
	 * @param ctx the parse tree
	 */
	void enterTypeQualifier(circParser.TypeQualifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link circParser#typeQualifier}.
	 * @param ctx the parse tree
	 */
	void exitTypeQualifier(circParser.TypeQualifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link circParser#functionSpecifier}.
	 * @param ctx the parse tree
	 */
	void enterFunctionSpecifier(circParser.FunctionSpecifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link circParser#functionSpecifier}.
	 * @param ctx the parse tree
	 */
	void exitFunctionSpecifier(circParser.FunctionSpecifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link circParser#alignmentSpecifier}.
	 * @param ctx the parse tree
	 */
	void enterAlignmentSpecifier(circParser.AlignmentSpecifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link circParser#alignmentSpecifier}.
	 * @param ctx the parse tree
	 */
	void exitAlignmentSpecifier(circParser.AlignmentSpecifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link circParser#declarator}.
	 * @param ctx the parse tree
	 */
	void enterDeclarator(circParser.DeclaratorContext ctx);
	/**
	 * Exit a parse tree produced by {@link circParser#declarator}.
	 * @param ctx the parse tree
	 */
	void exitDeclarator(circParser.DeclaratorContext ctx);
	/**
	 * Enter a parse tree produced by {@link circParser#directDeclarator}.
	 * @param ctx the parse tree
	 */
	void enterDirectDeclarator(circParser.DirectDeclaratorContext ctx);
	/**
	 * Exit a parse tree produced by {@link circParser#directDeclarator}.
	 * @param ctx the parse tree
	 */
	void exitDirectDeclarator(circParser.DirectDeclaratorContext ctx);
	/**
	 * Enter a parse tree produced by {@link circParser#gccDeclaratorExtension}.
	 * @param ctx the parse tree
	 */
	void enterGccDeclaratorExtension(circParser.GccDeclaratorExtensionContext ctx);
	/**
	 * Exit a parse tree produced by {@link circParser#gccDeclaratorExtension}.
	 * @param ctx the parse tree
	 */
	void exitGccDeclaratorExtension(circParser.GccDeclaratorExtensionContext ctx);
	/**
	 * Enter a parse tree produced by {@link circParser#gccAttributeSpecifier}.
	 * @param ctx the parse tree
	 */
	void enterGccAttributeSpecifier(circParser.GccAttributeSpecifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link circParser#gccAttributeSpecifier}.
	 * @param ctx the parse tree
	 */
	void exitGccAttributeSpecifier(circParser.GccAttributeSpecifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link circParser#gccAttributeList}.
	 * @param ctx the parse tree
	 */
	void enterGccAttributeList(circParser.GccAttributeListContext ctx);
	/**
	 * Exit a parse tree produced by {@link circParser#gccAttributeList}.
	 * @param ctx the parse tree
	 */
	void exitGccAttributeList(circParser.GccAttributeListContext ctx);
	/**
	 * Enter a parse tree produced by {@link circParser#gccAttribute}.
	 * @param ctx the parse tree
	 */
	void enterGccAttribute(circParser.GccAttributeContext ctx);
	/**
	 * Exit a parse tree produced by {@link circParser#gccAttribute}.
	 * @param ctx the parse tree
	 */
	void exitGccAttribute(circParser.GccAttributeContext ctx);
	/**
	 * Enter a parse tree produced by {@link circParser#nestedParenthesesBlock}.
	 * @param ctx the parse tree
	 */
	void enterNestedParenthesesBlock(circParser.NestedParenthesesBlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link circParser#nestedParenthesesBlock}.
	 * @param ctx the parse tree
	 */
	void exitNestedParenthesesBlock(circParser.NestedParenthesesBlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link circParser#pointer}.
	 * @param ctx the parse tree
	 */
	void enterPointer(circParser.PointerContext ctx);
	/**
	 * Exit a parse tree produced by {@link circParser#pointer}.
	 * @param ctx the parse tree
	 */
	void exitPointer(circParser.PointerContext ctx);
	/**
	 * Enter a parse tree produced by {@link circParser#typeQualifierList}.
	 * @param ctx the parse tree
	 */
	void enterTypeQualifierList(circParser.TypeQualifierListContext ctx);
	/**
	 * Exit a parse tree produced by {@link circParser#typeQualifierList}.
	 * @param ctx the parse tree
	 */
	void exitTypeQualifierList(circParser.TypeQualifierListContext ctx);
	/**
	 * Enter a parse tree produced by {@link circParser#parameterTypeList}.
	 * @param ctx the parse tree
	 */
	void enterParameterTypeList(circParser.ParameterTypeListContext ctx);
	/**
	 * Exit a parse tree produced by {@link circParser#parameterTypeList}.
	 * @param ctx the parse tree
	 */
	void exitParameterTypeList(circParser.ParameterTypeListContext ctx);
	/**
	 * Enter a parse tree produced by {@link circParser#parameterList}.
	 * @param ctx the parse tree
	 */
	void enterParameterList(circParser.ParameterListContext ctx);
	/**
	 * Exit a parse tree produced by {@link circParser#parameterList}.
	 * @param ctx the parse tree
	 */
	void exitParameterList(circParser.ParameterListContext ctx);
	/**
	 * Enter a parse tree produced by {@link circParser#parameterDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterParameterDeclaration(circParser.ParameterDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link circParser#parameterDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitParameterDeclaration(circParser.ParameterDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link circParser#identifierList}.
	 * @param ctx the parse tree
	 */
	void enterIdentifierList(circParser.IdentifierListContext ctx);
	/**
	 * Exit a parse tree produced by {@link circParser#identifierList}.
	 * @param ctx the parse tree
	 */
	void exitIdentifierList(circParser.IdentifierListContext ctx);
	/**
	 * Enter a parse tree produced by {@link circParser#typeName}.
	 * @param ctx the parse tree
	 */
	void enterTypeName(circParser.TypeNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link circParser#typeName}.
	 * @param ctx the parse tree
	 */
	void exitTypeName(circParser.TypeNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link circParser#abstractDeclarator}.
	 * @param ctx the parse tree
	 */
	void enterAbstractDeclarator(circParser.AbstractDeclaratorContext ctx);
	/**
	 * Exit a parse tree produced by {@link circParser#abstractDeclarator}.
	 * @param ctx the parse tree
	 */
	void exitAbstractDeclarator(circParser.AbstractDeclaratorContext ctx);
	/**
	 * Enter a parse tree produced by {@link circParser#directAbstractDeclarator}.
	 * @param ctx the parse tree
	 */
	void enterDirectAbstractDeclarator(circParser.DirectAbstractDeclaratorContext ctx);
	/**
	 * Exit a parse tree produced by {@link circParser#directAbstractDeclarator}.
	 * @param ctx the parse tree
	 */
	void exitDirectAbstractDeclarator(circParser.DirectAbstractDeclaratorContext ctx);
	/**
	 * Enter a parse tree produced by {@link circParser#typedefName}.
	 * @param ctx the parse tree
	 */
	void enterTypedefName(circParser.TypedefNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link circParser#typedefName}.
	 * @param ctx the parse tree
	 */
	void exitTypedefName(circParser.TypedefNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link circParser#initializer}.
	 * @param ctx the parse tree
	 */
	void enterInitializer(circParser.InitializerContext ctx);
	/**
	 * Exit a parse tree produced by {@link circParser#initializer}.
	 * @param ctx the parse tree
	 */
	void exitInitializer(circParser.InitializerContext ctx);
	/**
	 * Enter a parse tree produced by {@link circParser#initializerList}.
	 * @param ctx the parse tree
	 */
	void enterInitializerList(circParser.InitializerListContext ctx);
	/**
	 * Exit a parse tree produced by {@link circParser#initializerList}.
	 * @param ctx the parse tree
	 */
	void exitInitializerList(circParser.InitializerListContext ctx);
	/**
	 * Enter a parse tree produced by {@link circParser#designation}.
	 * @param ctx the parse tree
	 */
	void enterDesignation(circParser.DesignationContext ctx);
	/**
	 * Exit a parse tree produced by {@link circParser#designation}.
	 * @param ctx the parse tree
	 */
	void exitDesignation(circParser.DesignationContext ctx);
	/**
	 * Enter a parse tree produced by {@link circParser#designatorList}.
	 * @param ctx the parse tree
	 */
	void enterDesignatorList(circParser.DesignatorListContext ctx);
	/**
	 * Exit a parse tree produced by {@link circParser#designatorList}.
	 * @param ctx the parse tree
	 */
	void exitDesignatorList(circParser.DesignatorListContext ctx);
	/**
	 * Enter a parse tree produced by {@link circParser#designator}.
	 * @param ctx the parse tree
	 */
	void enterDesignator(circParser.DesignatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link circParser#designator}.
	 * @param ctx the parse tree
	 */
	void exitDesignator(circParser.DesignatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link circParser#staticAssertDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterStaticAssertDeclaration(circParser.StaticAssertDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link circParser#staticAssertDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitStaticAssertDeclaration(circParser.StaticAssertDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link circParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatement(circParser.StatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link circParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatement(circParser.StatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link circParser#labeledStatement}.
	 * @param ctx the parse tree
	 */
	void enterLabeledStatement(circParser.LabeledStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link circParser#labeledStatement}.
	 * @param ctx the parse tree
	 */
	void exitLabeledStatement(circParser.LabeledStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link circParser#compoundStatement}.
	 * @param ctx the parse tree
	 */
	void enterCompoundStatement(circParser.CompoundStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link circParser#compoundStatement}.
	 * @param ctx the parse tree
	 */
	void exitCompoundStatement(circParser.CompoundStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link circParser#blockItemList}.
	 * @param ctx the parse tree
	 */
	void enterBlockItemList(circParser.BlockItemListContext ctx);
	/**
	 * Exit a parse tree produced by {@link circParser#blockItemList}.
	 * @param ctx the parse tree
	 */
	void exitBlockItemList(circParser.BlockItemListContext ctx);
	/**
	 * Enter a parse tree produced by {@link circParser#blockItem}.
	 * @param ctx the parse tree
	 */
	void enterBlockItem(circParser.BlockItemContext ctx);
	/**
	 * Exit a parse tree produced by {@link circParser#blockItem}.
	 * @param ctx the parse tree
	 */
	void exitBlockItem(circParser.BlockItemContext ctx);
	/**
	 * Enter a parse tree produced by {@link circParser#expressionStatement}.
	 * @param ctx the parse tree
	 */
	void enterExpressionStatement(circParser.ExpressionStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link circParser#expressionStatement}.
	 * @param ctx the parse tree
	 */
	void exitExpressionStatement(circParser.ExpressionStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link circParser#selectionStatement}.
	 * @param ctx the parse tree
	 */
	void enterSelectionStatement(circParser.SelectionStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link circParser#selectionStatement}.
	 * @param ctx the parse tree
	 */
	void exitSelectionStatement(circParser.SelectionStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link circParser#iterationStatement}.
	 * @param ctx the parse tree
	 */
	void enterIterationStatement(circParser.IterationStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link circParser#iterationStatement}.
	 * @param ctx the parse tree
	 */
	void exitIterationStatement(circParser.IterationStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link circParser#forCondition}.
	 * @param ctx the parse tree
	 */
	void enterForCondition(circParser.ForConditionContext ctx);
	/**
	 * Exit a parse tree produced by {@link circParser#forCondition}.
	 * @param ctx the parse tree
	 */
	void exitForCondition(circParser.ForConditionContext ctx);
	/**
	 * Enter a parse tree produced by {@link circParser#forDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterForDeclaration(circParser.ForDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link circParser#forDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitForDeclaration(circParser.ForDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link circParser#forExpression}.
	 * @param ctx the parse tree
	 */
	void enterForExpression(circParser.ForExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link circParser#forExpression}.
	 * @param ctx the parse tree
	 */
	void exitForExpression(circParser.ForExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link circParser#jumpStatement}.
	 * @param ctx the parse tree
	 */
	void enterJumpStatement(circParser.JumpStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link circParser#jumpStatement}.
	 * @param ctx the parse tree
	 */
	void exitJumpStatement(circParser.JumpStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link circParser#fichier}.
	 * @param ctx the parse tree
	 */
	void enterFichier(circParser.FichierContext ctx);
	/**
	 * Exit a parse tree produced by {@link circParser#fichier}.
	 * @param ctx the parse tree
	 */
	void exitFichier(circParser.FichierContext ctx);
	/**
	 * Enter a parse tree produced by {@link circParser#translationUnit}.
	 * @param ctx the parse tree
	 */
	void enterTranslationUnit(circParser.TranslationUnitContext ctx);
	/**
	 * Exit a parse tree produced by {@link circParser#translationUnit}.
	 * @param ctx the parse tree
	 */
	void exitTranslationUnit(circParser.TranslationUnitContext ctx);
	/**
	 * Enter a parse tree produced by {@link circParser#externalDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterExternalDeclaration(circParser.ExternalDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link circParser#externalDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitExternalDeclaration(circParser.ExternalDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link circParser#functionDefinition}.
	 * @param ctx the parse tree
	 */
	void enterFunctionDefinition(circParser.FunctionDefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link circParser#functionDefinition}.
	 * @param ctx the parse tree
	 */
	void exitFunctionDefinition(circParser.FunctionDefinitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link circParser#declarationList}.
	 * @param ctx the parse tree
	 */
	void enterDeclarationList(circParser.DeclarationListContext ctx);
	/**
	 * Exit a parse tree produced by {@link circParser#declarationList}.
	 * @param ctx the parse tree
	 */
	void exitDeclarationList(circParser.DeclarationListContext ctx);
}