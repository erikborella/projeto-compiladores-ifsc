import { TokenPosition } from "./TokenPosition";

type Variable = {
  type: String;
  name: String;
};

type Function = {
  returnType: string;
  name: String;
  position: TokenPosition;
  parameters: Variable[];
};

type Scope = {
  scopeVariables: Variable[];
  children: Scope[];
  position: TokenPosition;
};

type SymbolsTable = {
  functions: Function[];
  scopes: Scope[];
  strings: string[]
};

export type { Variable, Function, Scope, SymbolsTable };