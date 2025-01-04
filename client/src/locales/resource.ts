export type ResourceValues = {
  projectTitle: string;
  index: {
    compile: string;
    examples: string;
    exampleMain: {
      title: string;
      text: string;
      code: string;
    };
    exampleVariables: {
      title: string;
      description: string;
      variablesTypes: string;
      inlineVariables: string;
      arrayVariables: string;
      dimensionSize: string;
      code: string;
    };
    exampleAttribuition: {
      title: string;
      description: string;
      operatorsDescription: string;
      operators: {
        add: string;
        sub: string;
        mul: string;
        div: string;
        mod: string;
      };
      code: string;
    };
    exampleVariablePrint: {
      title: string;
      description: string;
      modelsDescription: string;
      models: {
        int: string;
        boolean: string;
        float: string;
        floatLimited: string;
      };
      code: string;
    };
    exampleVariableScan: {
      title: string;
      description: string,
      code: string;
    };
    exampleFunctionDeclaration: {
      title: string;
      descriptionStructure: string;
      descriptionName: string;
      descriptionParameters: string;
      descriptionBlock: string;
      descriptionReturn: string;
      code: string;
    };
    exampleFunctionCall: {
      title: string;
      descriptionCall: string;
      descriptionParameters: string;
      descriptionReturn: string;
      code: string;
    };
    exampleIf: {
      title: string;
      description: string;
      descriptionStructure: string;
      descriptionElse: string;
      descriptionOperators: string;
      operators: {
        greater: string;
        greaterEqual: string;
        less: string;
        lessEqual: string;
        equal: string;
        notEqual: string;
      };
      descriptionConditionalOperators: string;
      conditionalOperators: {
        and: string;
        or: string;
        not: string;
      };
      code: string;
    };
    exampleWhile: {
      title: string;
      description: string;
      descriptionStructure: string;
      code: string;
    };
    exampleFor: {
      title: string;
      description: string;
      descriptionStructure: string;
      code: string;
    };
  };
  examples: {
    sections: {
      basic: string;
      recursive: string;
      sorting: string;
      games: string;
    };
    codes: {
      [key: string]: {
        name: string;
        code: string;
      };
    };
  };
  codeId: {
    tokens: string;
    syntaxTree: string;
    symbolsTable: string;
    llvmIr: string;
    assembly: string;
    execution: string;
    algorithmsComplexity: string;
  };
};

type NestedKeys<T> = T extends object
? { [K in keyof T]: `${K & string}` | `${K & string}.${NestedKeys<T[K]>}` }[keyof T]
: never;

export type Resource = NestedKeys<ResourceValues> | (string & Record<never, never>);
