grammar circ;
@header {
    package compilateur.grammar;
 }

fichier : decl* EOF;
decl : decl_typ | decl_fct;
decl_vars : 'int' IDF (','IDF)* ';' 							#DeclVarInt
		| 'struct' IDF '*' IDF (',''*' IDF)*';'					#DeclVarStruct
		;
decl_typ : 'struct' IDF '{' decl_vars* '}' ';';
decl_fct : 'int' IDF '(' param_liste ')' bloc					#DeclFctInt
		| 'struct' IDF '*' IDF '(' param_liste ')' bloc 		#DeclFctStruct
		;
param_liste :  param (',' param)*								#ParamListMulti
			|													#ParamListNone
			;
param : 'int' IDF												#ParamInt
	| 'struct' IDF '*' IDF										#ParamStruct
	;
expr_primaire : valeur											#Valeur
	| IDF														#Idf
	| IDF '(' ')'												#IdfParenthesisEmpty
	| IDF '(' expr ( ',' expr)* ')'								#IdfParenthesis
	| 'sizeof' '(' 'struct' IDF ')'								#Sizeof
	| '(' expr ')'												#Parenthesis
	;
instruction : ';'												#Semicolon
			| expr ';'											#InstExpr
			| 'if' '(' expr ')' instruction						#IfThen
			| 'if' '(' expr ')' instruction 'else' instruction	#IfThenElse
			| 'while' '(' expr ')' instruction					#While
			| bloc												#InstBloc
			| 'return' expr ';'									#Return
			;
bloc : '{' decl_vars* instruction* '}';

expr: affectation;

affectation : expr_ou ('=' expr_ou)*;
expr_ou : expr_et ('||' expr_et)*;
expr_et : comparaison ('&&' comparaison)*;
comparaison : ordre (('=='|'!=') ordre)*;
ordre : addition (('<'|'<='|'>'|'>=') addition)*;
addition : multiplication (('+' | '-') multiplication )*;
multiplication : unaire (('*'|'/') unaire)*;
unaire : ('!'|'-')* fleche;
fleche : expr_primaire ('->' IDF)*;

valeur : ENTIER													#Integer
		| CARACTERE												#Char
		; 



CARACTERE:'\''('\u0021' 
		| '\u0023'..'\u0026'
		| '\u0028'..'\u005b'
		| '\u005d'..'\u007e'
		| '\\\\'
		| '\\\''
		| '\\"')'\'';

// CARACTERE : '\''('!' | '#' | '$' |'(' |')' |'*' |'+'|','|'.'| ':' |';'
// | '<' | '=' |'>' |'?'|'@' | '[' | ']' | '|' | '~' |'{' |'}'
// |'\\\\' | '\\\'' | '\\"' | 'a'..'z' |'A'..'Z' | '0'..'9')'\'' ;


ENTIER: '0'|('1'..'9')('0'..'9')*;
IDF: ('a'..'z' |'A'..'Z') ('a'..'z'|'A'..'Z'|'_'|'0'..'9')*;
WS: ('\n' | '\t' | ' ')+ -> skip;
COMLINE: '//'(.)*?('\n'|EOF) -> skip;
COMMULTILINE : '/*'(.|'\n')*?'*/' -> skip;
