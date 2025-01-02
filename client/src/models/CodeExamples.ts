type CodeExample = {
  name: string;
  code: string;
}

export const basicExamples: CodeExample[] = [
  {
    name: 'examples.codes.helloWorld.name',
    code: 'examples.codes.helloWorld.code',
  },
  {
    name: 'examples.codes.circleCalculus.name',
    code: 'examples.codes.circleCalculus.code'
  },
  {
    name: 'examples.codes.gradeAverage.name',
    code: 'examples.codes.gradeAverage.code',
  },
  {
    name: 'examples.codes.matrix.name',
    code: 'examples.codes.matrix.code',
  }
]

export const sortingExamples: CodeExample[] = [
  {
    name: 'examples.codes.bubbleSort.name',
    code: 'examples.codes.bubbleSort.code',
  },
  {
    name: 'examples.codes.insertionSort.name',
    code: 'examples.codes.insertionSort.code',
  },
  {
    name: 'examples.codes.selectionSort.name',
    code: 'examples.codes.selectionSort.code',
  },
  {
    name: 'examples.codes.mergeSort.name',
    code: 'examples.codes.mergeSort.code',
  },
  {
    name: 'examples.codes.quickSort.name',
    code: 'examples.codes.quickSort.code',
  },
];

export const recursiveExamples: CodeExample[] = [
    {
        name: 'Fatorial',
        code: `int fatorial(int n) {
  if (n <= 1) {
    return 1;
  } else {
    return n * func fatorial(n - 1);
  }
}

main() {
  int num;
  println("Digite um numero para calcular o fatorial:");
  scanf(num);

  println("O fatorial de %d é %d", num, func fatorial(num));
}`
    },
    {
        name: 'Fibonacci',
        code: `int fibonacci(int n) {
  if (n < 2) {
    return n;
  }

  return func fibonacci(n-1) + func fibonacci(n - 2);
}

main() {
  int num;
  println("Digite um numero para calcular o fibonacci:");
  scanf(num);

  println("O fibonacci de %d é %d", num, func fibonacci(num));
}`
    }
]

export const gamesExample: CodeExample[] = [
    {
        name: 'Jogo da velha',
        code: `void iniciarTabuleiro(int[3][3] tabuleiro) {
    int i, j;

    for (i = 0; i < 3; i = i + 1) {
        for (j = 0; j < 3; j = j + 1) {
            tabuleiro[i][j] = 0;
        }
    }
}

void imprimeTabuleiro(int[3][3] tabuleiro) {
    int i, j, count;

    count = 1;

    for (i = 0; i < 3; i = i + 1) {
        for (j = 0; j < 3; j = j + 1) {
            int cel;

            cel = tabuleiro[i][j];

            if (cel == 0) {
                print(" %d", count);
            }
            else {
                if (cel == 1) {
                    print(" x");
                }
                else {
                    print(" o");
                }
            }

            if (j < 2) {
                print(" |");
            }

            count = count + 1;
        }

        println("");
    }
}

void jogar(int[3][3] tabuleiro, boolean jogadorTurno1, int posicao) {
    int i, j, peca;

    i = posicao / 3;
    j = posicao % 3;

    if (jogadorTurno1 == true) {
        peca = 1;
    }
    else {
        peca = -1;
    }


    tabuleiro[i][j] = peca;
}

int verificarGanhador(int[3][3] tabuleiro) {
    if (tabuleiro[0][0] == 1 && tabuleiro[0][1] == 1 && tabuleiro[0][2] == 1 ||
        tabuleiro[1][0] == 1 && tabuleiro[1][1] == 1 && tabuleiro[1][2] == 1 ||
        tabuleiro[2][0] == 1 && tabuleiro[2][1] == 1 && tabuleiro[2][2] == 1 ||
        tabuleiro[0][0] == 1 && tabuleiro[1][0] == 1 && tabuleiro[2][0] == 1 ||
        tabuleiro[0][1] == 1 && tabuleiro[1][1] == 1 && tabuleiro[2][1] == 1 ||
        tabuleiro[0][2] == 1 && tabuleiro[1][2] == 1 && tabuleiro[2][2] == 1 ||
        tabuleiro[0][0] == 1 && tabuleiro[1][1] == 1 && tabuleiro[2][2] == 1 ||
        tabuleiro[0][2] == 1 && tabuleiro[1][1] == 1 && tabuleiro[2][0] == 1)
    {
        return 1;
    }

    if (tabuleiro[0][0] == -1 && tabuleiro[0][1] == -1 && tabuleiro[0][2] == -1 ||
        tabuleiro[1][0] == -1 && tabuleiro[1][1] == -1 && tabuleiro[1][2] == -1 ||
        tabuleiro[2][0] == -1 && tabuleiro[2][1] == -1 && tabuleiro[2][2] == -1 ||
        tabuleiro[0][0] == -1 && tabuleiro[1][0] == -1 && tabuleiro[2][0] == -1 ||
        tabuleiro[0][1] == -1 && tabuleiro[1][1] == -1 && tabuleiro[2][1] == -1 ||
        tabuleiro[0][2] == -1 && tabuleiro[1][2] == -1 && tabuleiro[2][2] == -1 ||
        tabuleiro[0][0] == -1 && tabuleiro[1][1] == -1 && tabuleiro[2][2] == -1 ||
        tabuleiro[0][2] == -1 && tabuleiro[1][1] == -1 && tabuleiro[2][0] == -1)
    {
        return -1;
    }

    return 0;
}

main() {
    int[3][3] tabuleiro;
    boolean jogadorTurno1, jogoEstaFinalizado;
    int posicao;

    jogadorTurno1 = true;
    jogoEstaFinalizado = false;

    func iniciarTabuleiro(tabuleiro);

    while (jogoEstaFinalizado != true) {
        int estado;

        func imprimeTabuleiro(tabuleiro);

        if (jogadorTurno1 == true) {
            println("Jogador X digite aonde deseja jogar: ");
        } else {
            println("Jogador O digite aonde deseja jogar: ");
        }

        scanf(posicao);

        posicao = posicao - 1;

        func jogar(tabuleiro, jogadorTurno1, posicao);

        jogadorTurno1 = !jogadorTurno1;

        estado = func verificarGanhador(tabuleiro);

        if (estado == 1) {
            println("X ganhou!!!");
            jogoEstaFinalizado = true;
        }

        if (estado == -1) {
            println("O ganhou!!!");
            jogoEstaFinalizado = true;
        }

    }
}`
    },
    {
        name: 'Jogo da forca',
        code: `void iniciarPalavraSecreta(char[10] palavraSecreta) {
  // Letras precisam ser expressas em ASCII
  palavraSecreta[0] = 99;  // c
  palavraSecreta[1] = 111; // o
  palavraSecreta[2] = 109; // m
  palavraSecreta[3] = 112; // p
  palavraSecreta[4] = 105; // i
  palavraSecreta[5] = 108; // l
  palavraSecreta[6] = 97;  // a
  palavraSecreta[7] = 100; // d
  palavraSecreta[8] = 111; // o
  palavraSecreta[9] = 114; // r
}

void iniciarPalavraAdivinhada(char[10] palavraAdivinhada) {
  int i;

  for (i = 0; i < 10; i = i + 1) {
    palavraAdivinhada[i] = 95; // _
  }
}

void imprimirTexto(char[10] palavra) {
  int i;

  for (i = 0; i < 10; i = i + 1) {
    print("%c", palavra[i]);
  }

  println("");
}

void imprimirTextoAdivinhadas(char[10] palavra) {
  int i;

  for (i = 0; i < 10; i = i + 1) {
    print("%c ", palavra[i]);
  }

  println("");
}

main() {
  char[10] palavraSecreta;
  char[10] palavraAdivinhada;
  int tentativas, acertos, i;
  char letra;
  boolean acertou;

  tentativas = 10;
  acertos = 0;

  func iniciarPalavraSecreta(palavraSecreta);
  func iniciarPalavraAdivinhada(palavraAdivinhada);

  println("Bem-vindo ao jogo da Forca!");
  println("A palavra secreta tem 10 letras e %d tentativas.", tentativas);

  while (tentativas > 0 && acertos < 10) {
    print("Palavra: ");
    func imprimirTextoAdivinhadas(palavraAdivinhada);

    println("Digite uma letra: ");
    scanf(letra);

    acertou = 0;

    for (i = 0; i < 10; i = i + 1) {
      if (palavraSecreta[i] == letra && palavraAdivinhada[i] == 95) {
        palavraAdivinhada[i] = letra;
        acertos = acertos + 1;
        acertou = true;
      }
    }

    if (!acertou) {
      tentativas = tentativas - 1;
      println("Letra incorreta! Tentativas restantes: %d", tentativas);
    }
    else {
      println("Letra correta!");
    }
  }

  if (acertos == 10) {
    println("Parabéns! Você adivinhou a palavra: compilador");
  }
  else {
    println("Você perdeu! A palavra era: compilador");
  }
}`
    }
];
