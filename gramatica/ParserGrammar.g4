parser grammar ParserGrammar;
options { tokenVocab=LexerGrammar; }

teste 
    : sinal constante
    ;
 
programa 
    : listafuncoes principal 
    ;

listafuncoes 
    : decfuncao listafuncoes 
    | /* epsilon */ 
    ;

decfuncao 
    : tiporetorno ID PARENTESE_ABRE parametros PARENTESE_FECHA bloco 
    ;

tiporetorno 
    : tipo 
    | TIPO_VOID 
    ;

tipo 
    : tipobase dimensao 
    ;

tipobase 
    : TIPO_CHAR 
    | TIPO_FLOAT 
    | TIPO_INT 
    | TIPO_BOOLEAN 
    ;

dimensao 
    : COLCHETE_ABRE NUM_INT COLCHETE_FECHA dimensao 
    | /* epsilon */ 
    ;

parametros 
    : tipo ID listaparametros 
    | /* epsilon */ 
    ;

listaparametros 
    : VIRGULA tipo ID listaparametros 
    | /* epsilon */ 
    ;

principal 
    : MAIN PARENTESE_ABRE PARENTESE_FECHA bloco 
    ;

bloco 
    : CHAVE_ABRE listavariaveis comandos CHAVE_FECHA
    ;

listavariaveis 
    : tipo ID listaid PONTO_VIRGULA listavariaveis 
    | /* epsilon */ 
    ;

listaid 
    : VIRGULA ID listaid 
    | /* epsilon */ 
    ;

comandos 
    : comando comandos 
    | /* epsilon */ 
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
    ;

comando_bloco 
    : selecao 
    | enquanto 
    | para 
    | retorno 
    ;

leitura 
    : SCANF PARENTESE_ABRE termoleitura novotermoleitura PARENTESE_FECHA
    ;

termoleitura 
    : ID dimensao2 
    ;

novotermoleitura 
    : VIRGULA termoleitura novotermoleitura 
    | /* epsilon */ 
    ;

dimensao2 
    : COLCHETE_ABRE expr_aditiva COLCHETE_FECHA dimensao2 
    | /* epsilon */ 
    ;

escrita 
    : PRINTLN PARENTESE_ABRE termoescrita novotermoescrita PARENTESE_FECHA 
    ;

termoescrita 
    : ID dimensao2 
    | constante 
    | TEXTO 
    ;

novotermoescrita 
    : VIRGULA termoescrita novotermoescrita 
    | /* epsilon */ 
    ;

selecao 
    : IF PARENTESE_ABRE expressao PARENTESE_FECHA bloco senão 
    ;

senão 
    : ELSE bloco 
    | /* epsilon */ 
    ;

enquanto 
    : WHILE PARENTESE_ABRE expressao PARENTESE_FECHA bloco 
    ;

para 
    : FOR PARENTESE_ABRE para_atribuicoes PONTO_VIRGULA para_expressao PONTO_VIRGULA para_atribuicoes PARENTESE_FECHA bloco 
    ;

atribuicao 
    : ID OP_ATRIBUICAO complemento 
    ;

para_atribuicoes 
    : atribuicao lista_atribuicao 
    | /* epsilon */ 
    ;

lista_atribuicao 
    : VIRGULA atribuicao lista_atribuicao 
    | /* epsilon */ 
    ;

para_expressao 
    : expressao 
    | /* epsilon */ 
    ;

complemento 
    : expressao 
    ;

funcao 
    : FUNC ID PARENTESE_ABRE argumentos PARENTESE_FECHA 
    ;

argumentos 
    : expressao novo_argumento 
    | /* epsilon */ 
    ;

novo_argumento 
    : VIRGULA expressao novo_argumento 
    | /* epsilon */ 
    ;

retorno 
    : RETURN expressao 
    ;

expressao 
    : expr_ou 
    ;

expr_ou 
    : expr_e expr_ou2 
    ;

expr_ou2 
    : OP_OU expr_e expr_ou2 
    | /* epsilon */ 
    ;

expr_e 
    : expr_relacional expr_e2 
    ;

expr_e2 
    : OP_E expr_relacional expr_e2 
    | /* epsilon */ 
    ;

expr_relacional 
    : expr_aditiva expr_relacional2 
    ;

expr_relacional2 
    : op_relacional expr_aditiva 
    | /* epsilon */ 
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
    : expr_multiplicativa expr_aditiva2
    ;

expr_aditiva2
    : op_aditivo expr_multiplicativa expr_aditiva2
    | /* epsilon */
    ;

op_aditivo
    : SINAL_MAIS
    | SINAL_MENOS
    ;

expr_multiplicativa
    : fator expr_multiplicativa2
    ;

expr_multiplicativa2
    : op_multiplicativo fator expr_multiplicativa2
    | /* epsilon */
    ;

op_multiplicativo
    : OP_MULTIPLICACAO
    | OP_DIVISAO
    | OP_RESTO_DIVISAO
    ;

fator
    : sinal termo
    | TEXTO
    | OP_NEGACAO fator
    | PARENTESE_ABRE expressao PARENTESE_FECHA
    ;

termo
    : ID dimensao2
    | constante
    | funcao
    ;

sinal
    : SINAL_MAIS
    | SINAL_MENOS
    | /* epsilon */
    ;

constante
    : NUM_INT
    | NUM_DEC
    ;