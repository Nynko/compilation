grammar grammaire;
@header{
package parser;
}


program : instruction* EOF;

exp :   (INT|IDF) ( ('+'|'-'|'*'|'/')   (INT|IDF) )*
        ;

aff :   IDF '=' exp
        ;

instruction :   ((aff | print) ';')
                | si
                ;

cond :  exp 
        | IDF
        ;

si :    'if' '(' cond  ')' '{' instruction+ '}' ('else' '{' instruction+ '}')?
        ;

print : 'print' (exp | IDF)
        ;

WS :    ('\n'|'\t'|' ')+ -> skip
        ;

INT :   ('0'..'9')+
        ;

IDF :   ('a'..'z'|'A'..'Z'|'_')('0'..'9'|'a'..'z'|'A'..'Z'|'_')*
        ;
