grammar circ;
@header {
    package main.grammar;
 }

fichier : decl* EOF;
decl : decl_typ | decl_fct;
decl_vars : 'int' IDF (','IDF)* ';'
		| 'struct' IDF (',''*' IDF)*';';
decl_typ : 'struct' IDF '{' decl_vars* '}' ';';
decl_fct : 'int' IDF '(' param_liste ')' bloc							//(param (',' param)*)? ')' bloc
		| 'struct' IDF '*' IDF '(' param_liste ')' bloc; 				//(param (',' param)*)? ')' bloc
param_liste :  param (',' param)*
			|;
param : 'int' IDF
	| 'struct' IDF '*' IDF;
expr_primaire : valeur
	| IDF
	| IDF '(' ')'
	| IDF '(' expr ( ',' expr)* ')'
	| 'sizeof' '(' 'struct' IDF ')'
	| '(' expr ')';

instruction : ';'
			| expr ';'
			| 'if' '(' expr ')' instruction
			| 'if' '(' expr ')' instruction 'else' instruction
			| 'while' '(' expr ')' instruction
			| bloc
			| 'return' expr ';' ;
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

valeur : ENTIER | CHIFFRE | '\''carac'\''; 
CHIFFRE: '0'..'9';
ENTIER: '0'|('1'..'9')('0'..'9')*;
// ENTIER: '0'
// 		| ('1'..'9') CHIFFRE*
// 		| '\''CARACTERE'\''; 
// carac:'\u0021' 
// 		| '\u0023'..'\u0026'
// 		| '\u0028'..'\u005b'
// 		| '\u005d'..'\u007e'
// 		| '\\\\'
// 		| '\\\''
// 		| '\\"';

carac : '!' | '#' | '$' |'(' |')' |'*' |'+'|','|'.'| ':' |';'
| '<' | '=' |'>' |'?'|'@' | '[' | ']' | '|' | '~' |'{' |'}'
|'\\\\' | '\\\'' | '\\"' | IDF | CHIFFRE ;

IDF: ('a'..'z' |'A'..'Z')(
	'a'..'z' |'A'..'Z'
	|'_'
	)*;
WS: ('\n' | '\t' | ' ')+ -> skip;
COM: '//'(.)*?'\n' -> skip;
COMBIS : '/*'(.|'\n')?'*/' -> skip;
