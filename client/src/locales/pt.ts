import { ResourceValues } from "./resource";

const exampleMain = {
  title: "Função main",
  text: "A função main é o ponto de entrada da execução de um programa, onde o código inicia sua execução.",
  code: `main() {
  // declarações de variáveis...

  // comandos...
}`,
};

const exampleVariables = {
  title: "Declaração de variáveis",
  description: "As declarações de variáveis devem ser feitas antes de seu uso nos blocos de código.",
  variablesTypes: "Os seguintes tipos de variáveis são suportados:",
  inlineVariables: "É possível declarar várias variáveis do mesmo tipo na mesma linha, separando os nomes por vírgula ({comma}).",
  arrayVariables: "Arrays de tamanho fixo podem ser declarados adicionando {dimensionSize} ao lado do tipo. Arrays de múltiplas dimensões também são permitidos, e não há um limite de dimensões; basta adicionar mais {dimensionSize} ao lado do tipo.",
  dimensionSize: "[tamanho_da_dimensão]",
  code: `// Declaração de variável simples
int var1;

// Declaração de múltiplas variáveis;
boolean var2, var3;

// Declaração de arrays
float[5] vet;
int[7][10] mat;`,
};

const exampleAttribuition = {
  title: "Atribuição de variáveis",
  description: "É possível atribuir valores a variáveis utilizando constantes, expressões matemáticas, valores de arrays ou valores retornados por funções",
  operatorsDescription: "Os seguintes operadores matemáticos são suportados:",
  operators: {
    add: "para adições",
    sub: "para subtrações",
    mul: "para multiplicações",
    div: "para divisões",
    mod: "para o valor de resto da divisão",
  },
  code: `// Declaração de variáveis
int n1;
boolean flag;
float f1;
int[5][5] mat;

// Atribuição de constante;
n1 = 10;
f1 = -5.2;
mat[0][1] = 7;

// Atribuição por expressão matemática
n1 = n1 + f1 * 3;
flag = 10 > 7;

// Atribuição por valor de um array
n1 = mat[0][0] + mat[1][0];

// Atribuição por um valor de retorno de função
n1 = func funcao();`,
};

const exampleVariablePrint = {
  title: "Escrita de valores",
  description: "Para escrever dados na tela, é usado os comando {print}, para colocar o texto na tela sem uma nova linha no final e {println} para imprimir o texto colocando uma nova linha no final.",
  modelsDescription: "Para imprimir os valores de variáveis, deve-se os modelos do template:",
  models: {
    int: "para exibir valores inteiros",
    boolean: "para exibir valores booleanos",
    float: "para exibir valores decimais",
    floatLimited: "para exibir valores decimais limitando as casas decimais, onde N é o numero de casas"
  },
  code: `// Escrita de uma mensagem simples
print("Ola mundo!");

// Escrita de um valor inteiro, com uma nova linha
println("%d", int1);

// Escrita de um valor inteiro com uma mensagem junto
pritnln("O valor da variavel eh %d:", int2);

// Escrita de um valor decimal com apenas duas casas decimais
pritnln("O valor de pi eh %.2f:", pi);`,
};

const exampleVariableScan = {
  title: "Leitura de valores",
  description: "Para ler valores do usuário pode-se usar o comando {scanf}, passando a variável na qual o valor será salvo para ela.",
  code: `int valor;

print("Digite um valor: ");
scanf(valor);

println("O valor digitado foi %d", valor);`,
}

const exampleFunctionDeclaration = {
  title: "Declaração de funções",
  descriptionStructure: "As declarações de funções devem ser feitas antes da função. Inicia-se especificando o tipo de retorno da função, podendo ser qualquer um dos tipos suportados, ou {void} caso a função não retorne nenhum valor.",
  descriptionName: "Após o tipo de retorno, deve ser especificado um nome único para função.",
  descriptionParameters: "Entre parêntesis, são definidos os parâmetros que a função recebe, especificando primeiro o seu tipo e depois o seu nome, separando cada parâmetro por virgula.",
  descriptionBlock: "Após, deve-se colocar entre chaves o código que será executado pela função.",
  descriptionReturn: "Use o comando {return} para retornar os valores das funções.",
  code: `// Declaração de uma função nomeada "somar" que recebe um int e um float e retorna um int
int somar(int n1, float n2) {
  // Código da função
  var resultado;

  resultado = n1 + n2;

  // Retorno do valor da função
  return resultado;
}`,
}

const exampleFunctionCall = {
  title: "Chamada de funções",
  descriptionCall: "Para chamar as funções declaradas, deve-se usar primeiro a palavra reservada {func} seguido do nome da função, seguido de parêntesis.",
  descriptionParameters: "Caso a função receba argumentos, eles devem ser especificados entre os parêntesis.",
  descriptionReturn: "Caso a função tenha um valor de retorno, ele poderá ser atribuído em uma variável ou usado como qualquer outro valor, como em uma expressão matemática ou em uma expressão condicional.",
  code: `// Chamada de função sem valor de retorno e sem argumentos
func funcao1();

// Chamada de função com argumentos
func funcao2(10, 2);

// Chamada de função com argumentos e salvando o seu retorno em uma variável
resultado = func funcao3(n1, n2);

// Uso do valor de uma função em uma expressão matemática
resultado = 2 * func fatorial(10);`,
}

const exampleIf = {
  title: "Bloco condicional if",
  description: "Use o bloco condiciona {if} para controlar o fluxo de execução do seu programa.",
  descriptionStructure: "A sua estrutura é definida primeiro especificando a palavra reservada {if} e entre parêntesis deve ser passada uma expressão condicional, que caso seja verdadeira, o bloco de código definido em sequencia por chaves será executado.",
  descriptionElse: "Opcionalmente pode-se definir um bloco {else} ao final do {if}, que será executado apenas se a condição do {if} for falsa.",
  descriptionOperators: "Os seguintes operadores condicionais são suportados:",
  operators: {
    greater: "Maior",
    greaterEqual: "Maior ou igual",
    less: "Menor",
    lessEqual: "Menor ou igual",
    equal: "Igual",
    notEqual: "Diferente"
  },
  descriptionConditionalOperators: "Para juntar expressões condicionais, pode-se ser usado os seguintes operadores, onde são computados usando curto-circuito:",
  conditionalOperators: {
    and: "Operador AND",
    or: "Operador OR",
    not: "Operador de negação"
  },
  code: `if (a == 10 || b == 10) {
  // Código a ser executado caso a condição seja verdadeira
}
else {
  // Código a ser executado caso a condição do if tenha sido falsa
}`,
}

const exampleWhile = {
  title: "Loop while",
  description: "Utilize o {while} para criar loops de repetição no código.",
  descriptionStructure: "A sua estrutura é parecida com a do {if}, iniciando com a palavra chave {while}, seguido da expressão condicional entre parêntesis e após o bloco de código que será executado em loop enquanto que a condição permanecer verdadeira.",
  code: `while (a > 10) {
  // Bloco de código que será executado enquanto a condição permanecer verdadeira.
}`,
};

const exampleFor = {
  title: "Loop for",
  description: "Utilize o comando de repetição {for} caso tenha um numero bem definido de repetições a serem feitas",
  descriptionStructure: "A sua estrutura é semelhante com a do bloco {while}, com a diferença de que no parêntesis, primeiro deve-se definir a inicialização de uma variável de controle seguido por um ponto e virgula. Após vem a expressão condicional, terminada também por um ponto e virgula. E por fim deve-se definir o passo da variável de controle.",
  code: `// i = 0 | Inicialização da variável de controle
// i < 10 | Expressão condicional
// i = i + 1 | Passo de incremento de 1 a cada loop
for (i = 0; i < 10; i = i + 1) {
  // Bloco de código que será executado enquanto a condição permanecer verdadeira.
}`,
}

export const pt: ResourceValues = {
  index: {
    title: "Projeto Compilador",
    compile: "Compilar",
    examples: "Exemplos",
    exampleMain,
    exampleVariables,
    exampleAttribuition,
    exampleVariablePrint,
    exampleVariableScan,
    exampleFunctionDeclaration,
    exampleFunctionCall,
    exampleIf,
    exampleWhile,
    exampleFor,
  },
};
