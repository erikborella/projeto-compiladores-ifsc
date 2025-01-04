const exampleMain = {
  title: "Main function",
  text: "The main function is the entry point of a program, where the code begins its execution.",
  code: `main() {'{'}
  // variable declarations...

  // commands...
{'}'}`,
};

const exampleVariables = {
  title: "Declaration of variables",
  description: "Variable declarations must be made before their use in code blocks.",
  variablesTypes: "The following types of variables are supported:",
  inlineVariables: "You can declare several variables of the same type on the same line, separating the names with a comma ({comma}).",
  arrayVariables: "Fixed-size arrays can be declared by adding {dimensionSize} next to the type. Arrays with multiple dimensions are also allowed, and there is no limit to the number of dimensions; just add more {dimensionSize} next to the type.",
  dimensionSize: "[dimension_size]",
  code: `// Simple variable declaration
int var1;

// Multiple variable declarations
boolean var2, var3;

// Array declarations
float[5] arr;
int[7][10] matrix;`,
};

const exampleAttribuition = {
  title: "Assigning variables",
  description: "You can assign values to variables using constants, mathematical expressions, values from arrays or values returned by functions.",
  operatorsDescription: "The following mathematical operators are supported:",
  operators: {
    add: "for additions",
    sub: "for subtractions",
    mul: "for multiplications",
    div: "for divisions",
    mod: "for the remainder value of the division",
  },
  code: `// Variable declarations
int num1;
boolean flag;
float f1;
int[5][5] matrix;

// Constant assignment
num1 = 10;
f1 = -5.2;
matrix[0][1] = 7;

// Assignment by mathematical expression
num1 = num1 + f1 * 3;
flag = 10 > 7;

// Assignment by an array value
num1 = matrix[0][0] + matrix[1][0];

// Assignment by a function return value
num1 = func function();`,
};

const exampleVariablePrint = {
  title: "Writing values",
  description: "To write data to the screen, you use the {print} command to put the text on the screen without a new line at the end and {println} to print the text with a new line at the end.",
  modelsDescription: "To print the values of variables, use the template models:",
  models: {
    int: "to display integer values",
    boolean: "to display boolean values",
    float: "to display decimal values",
    floatLimited: "to display decimal values by limiting the decimal places, where N is the number of places",
  },
  code: `// Writing a simple message
print("Hello world!");

// Writing an integer value with a new line
println("%d", int1);

// Writing an integer value with an accompanying message
println("The value of the variable is %d:", int2);

// Writing a decimal value with only two decimal places
println("The value of pi is %.2f:", pi);`,
};

const exampleVariableScan = {
  title: "Reading values",
  description: "To read values from the user, you can use the {scanf} command, passing the variable in which the value will be saved to it.",
  code: `int value;

print("Enter a value: ");
scanf(value);

println("The entered value was %d", value);`,
}

const exampleFunctionDeclaration = {
  title: "Function Declaration",
  descriptionStructure: "Function declarations must be made before the function. Start by specifying the return type of the function, which can be any of the supported types, or {void} if the function does not return any value.",
  descriptionName: "After the return type, a unique name for the function must be specified.",
  descriptionParameters: "Within parentheses, define the parameters that the function receives, specifying their type first and then their name, separating each parameter with a comma.",
  descriptionBlock: "Then, place the code to be executed by the function inside curly braces.",
  descriptionReturn: "Use the {return} command to return the function's values.",
  code: `// Declaration of a function named "add" that takes an int and a float and returns an int
int add(int n1, float n2) {'{'}
  // Function code
  var result;

  result = n1 + n2;

  // Return the function value
  return result;
{'}'}`,
}

const exampleFunctionCall = {
  title: "Function Call",
  descriptionCall: "To call declared functions, use the reserved word {func} followed by the function name and then parentheses.",
  descriptionParameters: "If the function takes arguments, they must be specified within the parentheses.",
  descriptionReturn: "If the function has a return value, it can be assigned to a variable or used like any other value, such as in a mathematical expression or a conditional expression.",
  code: `// Function call without return value and without arguments
func function1();

// Function call with arguments
func function2(10, 2);

// Function call with arguments and saving its return value into a variable
result = func function3(n1, n2);

// Using the value of a function in a mathematical expression
result = 2 * func factorial(10);`,
}

const exampleIf = {
  title: "If Conditional Block",
  description: "Use the {if} conditional block to control the execution flow of your program.",
  descriptionStructure: "Its structure is defined by first specifying the reserved word {if} and passing a conditional expression within parentheses. If the condition is true, the code block defined within curly braces will be executed.",
  descriptionElse: "Optionally, an {else} block can be defined at the end of the {if} block, which will be executed only if the {if} condition is false.",
  descriptionOperators: "The following conditional operators are supported:",
  operators: {
    greater: "Greater than",
    greaterEqual: "Greater than or equal to",
    less: "Less than",
    lessEqual: "Less than or equal to",
    equal: "Equal to",
    notEqual: "Not equal to"
  },
  descriptionConditionalOperators: "To combine conditional expressions, the following operators can be used, which are computed using short-circuit evaluation:",
  conditionalOperators: {
    and: "AND Operator",
    or: "OR Operator",
    not: "Negation Operator"
  },
  code: `if (a == 10 {'||'} b == 10) {'{'}
  // Code to be executed if the condition is true
{'}'}
else {'{'}
  // Code to be executed if the "if" condition was false
{'}'}`,
}

const exampleWhile = {
  title: "While Loop",
  description: "Use {while} to create repetition loops in the code.",
  descriptionStructure: "Its structure is similar to {if}, starting with the {while} keyword, followed by the conditional expression within parentheses, and then the code block that will be executed in a loop while the condition remains true.",
  code: `while (a > 10) {'{'}
  // Code block to be executed while the condition remains true.
{'}'}`,
};

const exampleFor = {
  title: "For Loop",
  description: "Use the {for} loop command if there is a well-defined number of repetitions to be performed.",
  descriptionStructure: "Its structure is similar to the {while} block, except that within the parentheses, you first define the initialization of a control variable followed by a semicolon. Then comes the conditional expression, also ending with a semicolon. Finally, the step of the control variable must be defined.",
  code: `// i = 0 {'|'} Initialization of the control variable
// i < 10 {'|'} Conditional expression
// i = i + 1 {'|'} Increment step of 1 per loop
for (i = 0; i < 10; i = i + 1) {'{'}
  // Code block to be executed while the condition remains true.
{'}'}`,
};

export const indexResources = {
  compile: "Compile",
  examples: "Examples",
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
};
