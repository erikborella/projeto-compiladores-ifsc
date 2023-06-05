lexer grammar ExprLexer;

MAIN : 'main' ;
SCANF : 'scanf' ;
PRINTLN : 'println' ;
IF : 'if' ;
ELSE : 'else' ;
WHILE : 'while' ;
FOR : 'for' ;
FUNC : 'func' ;
RETURN : 'return';

TIPO_VOID : 'void' ;
TIPO_CHAR : 'char' ;
TIPO_FLOAT : 'float' ;
TIPO_INT : 'int' ;
TIPO_BOOLEAN : 'boolean' ;

VIRGULA : ',' ;
PONTO_VIRGULA : ';' ;

PARENTESE_ABRE : '(';
PARENTESE_FECHA : ')';

COLCHETE_ABRE : '[';
COLCHETE_FECHA : ']';

CHAVE_ABRE : '{';
CHAVE_FECHA : '}';

TEXTO : '"'.*?'"'|'\''.*?'\'';

SINAL_MAIS : '+' ;
SINAL_MENOS : '-' ;

OP_MULTIPLICACAO : '*' ;
OP_DIVISAO : '/' ;
OP_RESTO_DIVISAO : '%' ;

OP_NEGACAO : '!' ;
OP_E : '&&' ;
OP_OU : '||' ;

OP_IGUAL : '==' ;
OP_DIFERENTE : '!=' ;

OP_MAIOR : '>';
OP_MAIOR_IGUAL : '>=';

OP_MENOR : '<' ;
OP_MENOR_IGUAL : '<=' ;

OP_ATRIBUICAO : '=' ;

NUM_INT : [0-9]+ ;
NUM_DEC : [0-9]+'.'[0-9]+;

ID: [a-zA-Z_][a-zA-Z_0-9]* ;
WS: [ \t\n\r\f]+ -> skip ;