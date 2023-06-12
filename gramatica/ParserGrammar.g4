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
    : tiporetorno id ( parametros ) bloco 
    ;

tiporetorno 
    : tipo 
    | void 
    ;

tipo 
    : tipobase dimensao 
    ;

tipobase 
    : char 
    | float 
    | int 
    | boolean 
    ;

dimensao 
    : [ num_int ] dimensao 
    | /* epsilon */ 
    ;

parametros 
    : tipo id listaparametros 
    | /* epsilon */ 
    ;

listaparametros 
    : , tipo id listaparametros 
    | /* epsilon */ 
    ;

principal 
    : main ( ) bloco 
    ;

bloco 
    : { listavariaveis comandos } 
    ;

listavariaveis 
    : tipo id listaid 
    ;
     listavariaveis 
    | /* epsilon */ 
    ;

listaid 
    : , id listaid 
    | /* epsilon */ 
    ;

comandos 
    : comando comandos 
    | /* epsilon */ 
    ;

comando 
    : comando_linha 
    ;

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
    : scanf ( termoleitura novotermoleitura ) 
    ;

termoleitura 
    : id dimensao2 
    ;

novotermoleitura 
    : , termoleitura novotermoleitura 
    | /* epsilon */ 
    ;

dimensao2 
    : [ expr_aditiva ] dimensao2 
    | /* epsilon */ 
    ;

escrita 
    : println ( termoescrita novotermoescrita ) 
    ;

termoescrita 
    : id dimensao2 
    | constante 
    | texto 
    ;

novotermoescrita 
    : , termoescrita novotermoescrita 
    | /* epsilon */ 
    ;

selecao 
    : if ( expressao ) bloco senão 
    ;

senão 
    : else bloco 
    | /* epsilon */ 
    ;

enquanto 
    : while ( expressao ) bloco 
    ;

para 
    : for ( para_atribuicoes 
    ;
     para_expressao 
    ;
     para_atribuicoes ) bloco 
    ;

atribuicao 
    : id = complemento 
    ;

para_atribuicoes 
    : atribuicao lista_atribuicao 
    | /* epsilon */ 
    ;

lista_atribuicao 
    : , atribuicao lista_atribuicao 
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
    : func id ( argumentos ) 
    ;

argumentos 
    : expressao novo_argumento 
    | /* epsilon */ 
    ;

novo_argumento 
    : , expressao novo_argumento 
    | /* epsilon */ 
    ;

retorno 
    : return expressao 
    ;

expressao 
    : expr_ou 
    ;

expr_ou 
    : expr_e expr_ou2 
    ;

expr_ou2 
    : 
    |
    | expr_e expr_ou2 
    | /* epsilon */ 
    ;

expr_e 
    : expr_relacional expr_e2 
    ;

expr_e2 
    : && expr_relacional expr_e2 
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