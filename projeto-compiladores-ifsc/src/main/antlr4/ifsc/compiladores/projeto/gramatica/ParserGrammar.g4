parser grammar ParserGrammar;
options { tokenVocab=LexerGrammar; }

programa
    : (decfuncao)* principal
    ;

decfuncao
    : tiporetorno ID PARENTESE_ABRE (parametros)? PARENTESE_FECHA bloco
    ;

tiporetorno
    : tipo
    | TIPO_VOID
    ;

tipo
    : tipobase (dimensao)*
    ;

tipobase
    : TIPO_CHAR
    | TIPO_FLOAT
    | TIPO_INT
    | TIPO_BOOLEAN
    ;

dimensao
    : COLCHETE_ABRE NUM_INT COLCHETE_FECHA
    ;

parametros
    : tipo ID (VIRGULA tipo ID)*
    ;

principal
    : MAIN PARENTESE_ABRE PARENTESE_FECHA bloco
    ;

bloco
    : CHAVE_ABRE (decvariavel)* (comando)* CHAVE_FECHA
    ;

decvariavel
    : tipo ID (VIRGULA ID)* PONTO_VIRGULA
    ;

comando
    : comando_linha PONTO_VIRGULA
    | comando_bloco
    ;

comando_linha
    : leitura
    | escrita
    | atribuicao
    | funcao
    | retorno
    ;

comando_bloco
    : selecao
    | enquanto
    | para
    ;

leitura
    : SCANF PARENTESE_ABRE termoleitura (VIRGULA termoleitura)* PARENTESE_FECHA
    ;

termoleitura
    : ID (dimensao2)*
    ;

dimensao2
    : COLCHETE_ABRE expr_aditiva COLCHETE_FECHA
    ;

escrita
    : PRINTLN PARENTESE_ABRE termoescrita (VIRGULA termoescrita)* PARENTESE_FECHA
    ;

termoescrita
    : ID (dimensao2)*
    | constante
    | TEXTO
    ;

selecao
    : IF PARENTESE_ABRE expressao PARENTESE_FECHA bloco (senao)?
    ;

senao
    : ELSE bloco
    ;

enquanto
    : WHILE PARENTESE_ABRE expressao PARENTESE_FECHA bloco
    ;

para
    : FOR PARENTESE_ABRE (para_atribuicoes)? PONTO_VIRGULA (expressao)? PONTO_VIRGULA (para_atribuicoes)? PARENTESE_FECHA bloco
    ;

atribuicao
    : ID OP_ATRIBUICAO complemento
    ;

para_atribuicoes
    : atribuicao (VIRGULA atribuicao)*
    ;

complemento
    : expressao
    ;

funcao
    : FUNC ID PARENTESE_ABRE (argumentos)? PARENTESE_FECHA
    ;

argumentos
    : expressao (VIRGULA expressao)*
    ;

retorno
    : RETURN expressao
    ;

expressao
    : expr_ou
    ;

expr_ou
    : expr_e (OP_OU expr_e)*
    ;

expr_e
    : expr_relacional (OP_E expr_relacional)*
    ;

expr_relacional
    : expr_aditiva (op_relacional expr_aditiva)*
    ;

op_relacional
    : OP_IGUAL
    | OP_DIFERENTE
    | OP_MAIOR
    | OP_MENOR
    | OP_MAIOR_IGUAL
    | OP_MENOR_IGUAL
    ;

expr_aditiva
    : expr_multiplicativa (op_aditivo expr_multiplicativa)*
    ;

op_aditivo
    : SINAL_MAIS
    | SINAL_MENOS
    ;

expr_multiplicativa
    : fator (op_multiplicativo fator)*
    ;

op_multiplicativo
    : OP_MULTIPLICACAO
    | OP_DIVISAO
    | OP_RESTO_DIVISAO
    ;

fator
    : (sinal)? termo
    | TEXTO
    | OP_NEGACAO fator
    | PARENTESE_ABRE expressao PARENTESE_FECHA
    ;

termo
    : ID (dimensao2)* #TermoVariavel
    | constante #TermoConstante
    | funcao #TermoFuncao
    ;

sinal
    : SINAL_MAIS
    | SINAL_MENOS
    ;

constante
    : NUM_INT
    | NUM_DEC
    ;