type CodeExample = {
  name: string;
  code: string;
}

export const codeExamples: CodeExample[] = [
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
    name: 'Merge sort',
    code: `void merge(int[10] arr, int l, int m, int r) {
    int i, j, k, n1, n2;
    int[10] L, R;

    n1 = m - l + 1;
    n2 = r - m;

    for (i = 0; i < n1; i = i + 1) {
        L[i] = arr[l + i];
    }

    for (j = 0; j < n2; j = j + 1) {
        R[j] = arr[m + 1 + j];
    }

    i = 0;
    j = 0;
    k = l;
    while (i < n1 && j < n2) {
        if (L[i] <= R[j]) {
            arr[k] = L[i];
            i = i + 1;
        }
        else {
            arr[k] = R[j];
            j = j + 1;
        }
        k = k + 1;
    }

    while (i < n1) {
        arr[k] = L[i];
        i = i + 1;
        k = k + 1;
    }

    while (j < n2) {
        arr[k] = R[j];
        j = j + 1;
        k = k + 1;
    }
}

void mergeSort(int[10] arr, int l, int r) {
    if (l < r) {
        int m;

        m = l + (r - l) / 2;

        func mergeSort(arr, l, m);
        func mergeSort(arr, m + 1, r);

        func merge(arr, l, m, r);
    }
}

void printArray(int[10] A, int size) {
    int i;

    for (i = 0; i < size; i = i + 1) {
        print("%d ", A[i]);
    }
    println("");
}

main() {
    int[10] arr;

    arr[0] = 5;
    arr[1] = 1;
    arr[2] = 4;
    arr[3] = 8;
    arr[4] = 9;
    arr[5] = 3;
    arr[6] = 5;
    arr[7] = 6;
    arr[8] = 2;
    arr[9] = 0;

    func printArray(arr, 10);

    func mergeSort(arr, 0, 9);

    func printArray(arr, 10);
}`
  }
]