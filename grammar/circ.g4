grammar circ;
@header {
    package main.grammar;
 }

fichier : decl* EOF;
decl : decl_typ | decl_fct;
decl_vars : 'int' idf (','idf)* ';'
		| 'struct' idf (',''*' idf)*';';
decl_typ : 'struct' idf '{' decl_vars* '}' ';';
decl_fct : 'int' idf '(' param_liste ')' bloc							//(param (',' param)*)? ')' bloc
		| 'struct' idf '*' idf '(' param_liste ')' bloc; 				//(param (',' param)*)? ')' bloc
param_liste :  param (',' param)*
			|;
param : 'int' idf
	| 'struct' idf '*' idf;
expr_primaire : valeur
	| idf
	| idf '(' ')'
	| idf '(' expr ( ',' expr)* ')'
	| 'sizeof' '(' 'struct' idf ')'
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
fleche : expr_primaire ('->' idf)*;

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
|'\\\\' | '\\\'' | '\\"' | ALPHABET | CHIFFRE ;

ALPHABET: ('a'..'z' |'A'..'Z');

idf: ALPHABET ( ALPHABET|'_' )*;
WS: ('\n' | '\t' | ' ')+ -> skip;
COM: '//'(.)*?'\n' -> skip;
COMBIS : '/*'(.|'\n')?'*/' -> skip;
