export type ResourceValues = {
  projectTitle: string;
  close: string;
  error: {
    uploadError: string;
    downloadCodeError: string;
    downloadTokenError: string;
    downloadSyntaxTreeError: string;
    downloadSymbolsTableError: string;
    downloadLlvmIrCodeError: string;
    downloadAssemblyCodeError: string;
    downloadAnalysisComplexityError: string;
    formulaErrorRender: string;
  };
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
  token: {
    type: string;
    value: string;
    types: {
      [key: string]: string;
    };
  };
  syntax: {
    labels: {
      [key: string]: string;
    };
  };
  symbolsTable: {
    functions: string;
    returnType: string;
    parameters: string;
    scopes: string;
    strings: string;
    scope: string;
    variables: string;
    type: string;
    name: string;
    scopeDoesNotHaveNewVariables: string;
    insideScopes: string;
  };
  llvmIr: {
    optimization: string;
    selectOptimizationLevel: string;
    optimizationLevel: string;
    optimizationComparizon: string;
    compareOptimizationLevels: string;
    activateComparison: string;
    optimizationLevelToCompare: string;
    optimizations: {
      noOpt: string;
      o0: string;
      os: string;
      o1: string;
      o2: string;
      o3: string;
      oz: string;
    };
  };
  assembly: {
    optimization: string;
    selectOptimizationLevel: string;
    optimizationLevel: string;
    optimizationComparizon: string;
    compareOptimizationLevels: string;
    activateComparison: string;
    optimizationLevelToCompare: string;
    optimizations: {
      noOpt: string;
      o0: string;
      os: string;
      o1: string;
      o2: string;
      o3: string;
      oz: string;
    };
  };
  execution: {
    title: string;
    description: string;
    restart: string;
    clear: string;
    output: string;
    input: string;
    send: string;
    startingServerConnection: string;
    serverConnectionEstablished: string;
    serverConnectionClosed: string;
    serverError: string;
    clientError: string;
    codeFinished: string;
  };
  complexity: {
    title: string;
    description: string;
    costDescriptions: {
      functions: string;
      functionsReturns: string;
      variableDeclaration: string;
      attribuition: string;
      readWrite: string;
      ifElse: string;
      for: string;
      while: string;
      functionCall: string;
    };
    results: string;
    simplification: string;
    asymptoticNotation: string;
    cost: string;
  };
};

type NestedKeys<T> = T extends object
? { [K in keyof T]: `${K & string}` | `${K & string}.${NestedKeys<T[K]>}` }[keyof T]
: never;

export type Resource = NestedKeys<ResourceValues> | (string & Record<never, never>);
