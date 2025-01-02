const helloWorldCode = `main() {'{'}
  println("Hello world!");
{'}'}`;

const circleCalculusCode = `main() {'{'}
  float radius;
  float pi;
  float area, perimeter;

  pi = 3.141592;

  println("Enter the radius of your circle: ");
  scanf(radius);

  area = pi * (radius * radius);
  perimeter = 2 * pi * radius;

  println("Radius: %.2f", radius);
  println("Area: %.2f", area);
  println("Perimeter: %.2f", perimeter);
{'}'}`;

const gradeAverageCode = `void readGrades(float[5] grades) {'{'}
  int i;

  for (i = 0; i < 5; i = i + 1) {'{'}
    float grade;

    println("Enter the %d grade of the student:", i + 1);
    scanf(grade);

    grades[i] = grade;
  {'}'}
{'}'}

void displayGrades(float[5] grades) {'{'}
  int i;

  println("The student's grades are:");

  for (i = 0; i < 5; i = i + 1) {'{'}
    println("* %.1f", grades[i]);
  {'}'}
{'}'}

float calculateAverage(float[5] grades) {'{'}
  int i;
  float sum;

  sum = 0;

  for (i = 0; i < 5; i = i + 1) {'{'}
    sum = sum + grades[i];
  {'}'}

  return sum / 5;
{'}'}

main() {'{'}
  float[5] grades;
  float average;

  func readGrades(grades);
  func displayGrades(grades);

  average = func calculateAverage(grades);

  println("The student's average was: %.1f", average);

  if (average < 7) {'{'}
    println("The student did not reach an average of 7");
  {'}'} else {'{'}
    println("The student reached an average of 7");
  {'}'}
{'}'}`;

const matrixCode = `void multiply(int[5][5] a, int[5][5] b, int[5][5] c, input int m, input int n) {'{'}
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

void initializeMatrix(int[5][5] matrix, input int n, input int m, int multiplier) {'{'}
  int i, j;

  for (i = 0; i < n; i = i + 1) {'{'}
    for (j = 0; j < m; j = j + 1) {'{'}
      int value;

      value = (i + j) * multiplier;
      matrix[i][j] = value;
    {'}'}
  {'}'}
{'}'}

void printMatrix(int[5][5] matrix, input int n, input int m) {'{'}
  int i, j;

  for (i = 0; i < n; i = i + 1) {'{'}
    for (j = 0; j < m; j = j + 1) {'{'}
      print(" %d {'|'}", matrix[i][j]);
    {'}'}

    println("");
  {'}'}

{'}'}

main() {'{'}
  int[5][5] a;
  int[5][5] b;
  int[5][5] c;

  int initialValueA;
  int initialValueB;

  println("Enter the initial value for matrix A:");
  scanf(initialValueA);

  println("Enter the initial value for matrix B:");
  scanf(initialValueB);

  func initializeMatrix(a, 5, 5, initialValueA);
  func initializeMatrix(b, 5, 5, initialValueB);

  println("");

  println("Matrix A:");
  func printMatrix(a, 5, 5);

  println("");

  println("Matrix B:");
  func printMatrix(b, 5, 5);

  func multiply(a, b, c, 5, 5);

  println("");

  println("Matrix C (a * b):");
  func printMatrix(c, 5, 5);
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
    int value;

    println("Enter the value for position %d: ", i);
    scanf(value);

    arr[i] = value;
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

  println("Unsorted array: ");
  func printArray(arr, 10);

  func bubbleSort(arr, 10);

  println("Sorted array: ");
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
    int value;

    println("Enter the value for position %d: ", i);
    scanf(value);

    arr[i] = value;
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

  println("Unsorted array: ");
  func printArray(arr, 10);

  func insertionSort(arr, 10);

  println("Sorted array: ");
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
    int value;

    println("Enter the value for position %d: ", i);
    scanf(value);

    arr[i] = value;
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

  println("Unsorted array: ");
  func printArray(arr, 10);

  func selectionSort(arr, 10);

  println("Sorted array: ");
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
    int value;

    println("Enter the value for position %d: ", i);
    scanf(value);

    arr[i] = value;
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

  println("Unsorted array: ");
  func printArray(arr, 10);

  func mergeSort(arr, 0, 9);

  println("Sorted array: ");
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
    int value;

    println("Enter the value for position %d: ", i);
    scanf(value);

    arr[i] = value;
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

  println("Unsorted array: ");
  func printArray(arr, 10);

  func quickSort(arr, 0, 10 - 1);

  println("Sorted array: ");
  func printArray(arr, 10);
{'}'}`;

export const examplesResources = {
  sections: {
    basic: "Basic",
    recursive: "Recursive",
    sorting: "Sorting",
    games: "Games"
  },
  codes: {
    "helloWorld": {
      name: 'Hello World',
      code: helloWorldCode,
    },
    "circleCalculus": {
      name: 'Circle Calculation',
      code: circleCalculusCode,
    },
    "gradeAverage": {
      name: 'Grade Average',
      code: gradeAverageCode,
    },
    "matrix": {
      name: 'Matrices',
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
