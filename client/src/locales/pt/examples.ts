const helloWorldCode = `main() {'{'}
  println("Ola mundo!");
{'}'}`;

const circleCalculusCode = `main() {'{'}
  float raio;
  float pi;
  float area, perimetro;

  pi = 3.141592;

  println("Digite o raio do seu circulo: ");
  scanf(raio);

  area = pi * (raio * raio);
  perimetro = 2 * pi * raio;

  println("Raio: %.2f", raio);
  println("Area: %.2f", area);
  println("Perímetro: %.2f", perimetro);
{'}'}`;

const gradeAverageCode = `void lerNotas(float[5] notas) {'{'}
  int i;

  for (i = 0; i < 5; i = i + 1) {'{'}
    float nota;

    println("Digite a %d nota do aluno:", i + 1);
    scanf(nota);

    notas[i] = nota;
  {'}'}
{'}'}

void exibirNotas(float[5] notas) {'{'}
  int i;

  println("As notas do aluno são:");

  for (i = 0; i < 5; i = i + 1) {'{'}
    println("* %.1f", notas[i]);
  {'}'}
{'}'}

float calcularMedia(float[5] notas) {'{'}
  int i;
  float soma;

  soma = 0;

  for (i = 0; i < 5; i = i + 1) {'{'}
    soma = soma + notas[i];
  {'}'}

  return soma / 5;
{'}'}

main() {'{'}
  float[5] notas;
  float media;

  func lerNotas(notas);
  func exibirNotas(notas);

  media = func calcularMedia(notas);

  println("A média do aluno foi: %.1f", media);

  if (media < 7) {'{'}
    println("O aluno não atingiu a média 7");
  {'}'} else {'{'}
    println("O aluno atingiu a média 7");
  {'}'}
{'}'}`;

const matrixCode = `void multiplicar(int[5][5] a, int[5][5] b, int[5][5] c, input int m, input int n) {'{'}
  int i, j, k;

  for (i = 0; i < m; i = i + 1) {'{'}
    for (j = 0; j < n; j = j + 1) {'{'}
      c[i][j] = 0;

      for (k = 0; k < m; k = k + 1) {'{'}
        c[i][j] = c[i][j] + (a[i][k] * b[k][j]);
      {'}'}
    {'}'}
  {'}'}
{'}'}

void iniciarMatriz(int[5][5] matriz, input int n, input int m, int mult) {'{'}
  int i, j;

  for (i = 0; i < n; i = i + 1) {'{'}
    for (j = 0; j < m; j = j + 1) {'{'}
      int val;

      val = (i + j) * mult;
      matriz[i][j] = val;
    {'}'}
  {'}'}
{'}'}

void imprimirMatriz(int[5][5] matriz, input int n, input int m) {'{'}
  int i, j;

  for (i = 0; i < n; i = i + 1) {'{'}
    for (j = 0; j < m; j = j + 1) {'{'}
      print(" %d {'|'}", matriz[i][j]);
    {'}'}

    println("");
  {'}'}

{'}'}

main() {'{'}
  int[5][5] a;
  int[5][5] b;
  int[5][5] c;

  int valorInicialA;
  int valorInicialB;

  println("Digite o valor inicial da matriz A:");
  scanf(valorInicialA);

  println("Digite o valor inicial da matriz B:");
  scanf(valorInicialB);

  func iniciarMatriz(a, 5, 5, valorInicialA);
  func iniciarMatriz(b, 5, 5, valorInicialB);

  println("");

  println("Matriz A:");
  func imprimirMatriz(a, 5, 5);

  println("");

  println("Matriz B:");
  func imprimirMatriz(b, 5, 5);

  func multiplicar(a, b, c, 5, 5);

  println("");

  println("Matriz C (a * b):");
  func imprimirMatriz(c, 5, 5);
{'}'}`;

const bubbleSortCode = `void printArray(int[10] arr, input int size) {'{'}
  int i;

  for (i = 0; i < size; i = i + 1) {'{'}
    print("%d ", arr[i]);
  {'}'}
  println("");
{'}'}

void initArray(int[10] arr, input int size) {'{'}
  int i;

  for (i = 0; i < size; i = i + 1) {'{'}
    int valor;

    println("Digite o valor da posição %d: ", i);
    scanf(valor);

    arr[i] = valor;
  {'}'}
{'}'}

void bubbleSort(int[10] arr, input int size) {'{'}
  int i, j, temp;

  for (i = 0; i < size - 1; i = i + 1) {'{'}
    for (j = 0; j < size - i - 1; j = j + 1) {'{'}
      if (arr[j] > arr[j + 1]) {'{'}
        temp = arr[j];
        arr[j] = arr[j + 1];
        arr[j + 1] = temp;
      {'}'}
    {'}'}
  {'}'}
{'}'}

main() {'{'}
  int[10] arr;
  int i;

  func initArray(arr, 10);

  println("Array desordenado: ");
  func printArray(arr, 10);

  func bubbleSort(arr, 10);

  println("Array ordenado: ");
  func printArray(arr, 10);
{'}'}`;

const insertionSortCode = `void printArray(int[10] arr, input int size) {'{'}
  int i;

  for (i = 0; i < size; i = i + 1) {'{'}
    print("%d ", arr[i]);
  {'}'}
  println("");
{'}'}

void initArray(int[10] arr, input int size) {'{'}
  int i;

  for (i = 0; i < size; i = i + 1) {'{'}
    int valor;

    println("Digite o valor da posição %d: ", i);
    scanf(valor);

    arr[i] = valor;
  {'}'}
{'}'}

void insertionSort(int[10] arr, input int size) {'{'}
  int i;

  for (i = 1; i < size; i = i + 1) {'{'}
    int key, j;

    key = arr[i];
    j = i - 1;

    while (j >= 0 && arr[j] > key) {'{'}
      arr[j + 1] = arr[j];
      j = j - 1;
    {'}'}

    arr[j + 1] = key;
  {'}'}
{'}'}

main() {'{'}
  int[10] arr;

  func initArray(arr, 10);

  println("Array desordenado: ");
  func printArray(arr, 10);

  func insertionSort(arr, 10);

  println("Array ordenado: ");
  func printArray(arr, 10);
{'}'}`;

const selectionSortCode = `void printArray(int[10] arr, input int size) {'{'}
  int i;

  for (i = 0; i < size; i = i + 1) {'{'}
    print("%d ", arr[i]);
  {'}'}
  println("");
{'}'}

void initArray(int[10] arr, input int size) {'{'}
  int i;

  for (i = 0; i < size; i = i + 1) {'{'}
    int valor;

    println("Digite o valor da posição %d: ", i);
    scanf(valor);

    arr[i] = valor;
  {'}'}
{'}'}

void selectionSort(int[10] arr, input int size) {'{'}
  int i;

  for (i = 0; i < size - 1; i = i + 1) {'{'}
    int j, temp;
    int min_idx;

    min_idx = i;

    for (j = i + 1; j < size; j = j + 1) {'{'}
      if (arr[j] < arr[min_idx]) {'{'}
        min_idx = j;
      {'}'}
    {'}'}

    temp = arr[i];
    arr[i] = arr[min_idx];
    arr[min_idx] = temp;
  {'}'}
{'}'}

main() {'{'}
  int[10] arr;

  func initArray(arr, 10);

  println("Array desordenado: ");
  func printArray(arr, 10);

  func selectionSort(arr, 10);

  println("Array ordenado: ");
  func printArray(arr, 10);
{'}'}`;

const mergeSortCode = `void printArray(int[10] arr, input int size) {'{'}
  int i;

  for (i = 0; i < size; i = i + 1) {'{'}
    print("%d ", arr[i]);
  {'}'}
  println("");
{'}'}

void initArray(int[10] arr, input int size) {'{'}
  int i;

  for (i = 0; i < size; i = i + 1) {'{'}
    int valor;

    println("Digite o valor da posição %d: ", i);
    scanf(valor);

    arr[i] = valor;
  {'}'}
{'}'}

void merge(int[10] arr, int l, int m, int r) {'{'}
  int i, j, k, n1, n2;
  int[10] L, R;

  n1 = m - l + 1;
  n2 = r - m;

  for (i = 0; i < n1; i = i + 1) {'{'}
    L[i] = arr[l + i];
  {'}'}

  for (j = 0; j < n2; j = j + 1) {'{'}
    R[j] = arr[m + 1 + j];
  {'}'}

  i = 0;
  j = 0;
  k = l;
  while (i < n1 && j < n2) {'{'}
    if (L[i] <= R[j]) {'{'}
      arr[k] = L[i];
      i = i + 1;
    {'}'}
    else {'{'}
      arr[k] = R[j];
      j = j + 1;
    {'}'}
    k = k + 1;
  {'}'}

  while (i < n1) {'{'}
    arr[k] = L[i];
    i = i + 1;
    k = k + 1;
  {'}'}

  while (j < n2) {'{'}
    arr[k] = R[j];
    j = j + 1;
    k = k + 1;
  {'}'}
{'}'}

void mergeSort(int[10] arr, int l, int r) {'{'}
  if (l < r) {'{'}
    int m;

    m = l + (r - l) / 2;

    func mergeSort(arr, l, m);
    func mergeSort(arr, m + 1, r);

    func merge(arr, l, m, r);
  {'}'}
{'}'}

main() {'{'}
  int[10] arr;

  func initArray(arr, 10);

  println("Array desordenado: ");
  func printArray(arr, 10);

  func mergeSort(arr, 0, 9);

  println("Array ordenado: ");
  func printArray(arr, 10);
{'}'}`;

const quickSortCode = `void printArray(int[10] arr, input int size) {'{'}
  int i;

  for (i = 0; i < size; i = i + 1) {'{'}
    print("%d ", arr[i]);
  {'}'}
  println("");
{'}'}

void initArray(int[10] arr, input int size) {'{'}
  int i;

  for (i = 0; i < size; i = i + 1) {'{'}
    int valor;

    println("Digite o valor da posição %d: ", i);
    scanf(valor);

    arr[i] = valor;
  {'}'}
{'}'}

int partition(int[10] arr, int low, int high) {'{'}
  int pivot, i, j;
  int temp;

  pivot = arr[high];
  i = low - 1;

  for (j = low; j <= high - 1; j = j + 1) {'{'}
    if (arr[j] < pivot) {'{'}
      i = i + 1;

      temp = arr[i];
      arr[i] = arr[j];
      arr[j] = temp;
    {'}'}
  {'}'}

  temp = arr[i + 1];
  arr[i + 1] = arr[high];
  arr[high] = temp;

  return i + 1;
{'}'}

void quickSort(int[10] arr, int low, int high) {'{'}
  if (low < high) {'{'}
    int pi;

    pi = func partition(arr, low, high);

    func quickSort(arr, low, pi - 1);
    func quickSort(arr, pi + 1, high);
  {'}'}
{'}'}

main() {'{'}
  int[10] arr;

  func initArray(arr, 10);

  println("Array desordenado: ");
  func printArray(arr, 10);

  func quickSort(arr, 0, 10 - 1);

  println("Array ordenado: ");
  func printArray(arr, 10);
{'}'}`;

export const examplesResources = {
  sections: {
    basic: "Básico",
    recursive: "Recursivo",
    sorting: "Ordenação",
    games: "Jogos"
  },
  codes: {
    "helloWorld": {
      name: 'Olá mundo',
      code: helloWorldCode,
    },
    "circleCalculus": {
      name: 'Cálculo de círculos',
      code: circleCalculusCode,
    },
    "gradeAverage": {
      name: 'Média de notas',
      code: gradeAverageCode,
    },
    "matrix": {
      name: 'Matrizes',
      code: matrixCode,
    },
    "bubbleSort": {
      name: 'Bubble sort',
      code: bubbleSortCode,
    },
    "insertionSort": {
      name: 'Insertion sort',
      code: insertionSortCode,
    },
    "selectionSort": {
      name: 'Selection sort',
      code: selectionSortCode,
    },
    "mergeSort": {
      name: 'Merge sort',
      code: mergeSortCode,
    },
    "quickSort": {
      name: 'Quick sort',
      code: quickSortCode,
    },
  },
};