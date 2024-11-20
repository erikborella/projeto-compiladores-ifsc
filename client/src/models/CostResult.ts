import { TokenPosition } from "./TokenPosition";

type CostResult = {
  position: TokenPosition;
  value: number;
  stringRepresentation: string;
  shouldShowInPlace: boolean;
}

type BlockCost = CostResult & {
  blockCost: CostResult;
  costs: CostResult[];
  topLevel: boolean;
  topLevelId: string;
}

type VariableCost = CostResult & {
  variable: string;
  costRange: string;
  blockCost: BlockCost;
}

function isBlockCost(costResult: CostResult): costResult is BlockCost {
  return 'costs' in costResult;
}

function isVariableCost(costResult: CostResult): costResult is VariableCost {
  return 'variable' in costResult;
}

export type { CostResult, BlockCost, VariableCost};
export {  isBlockCost, isVariableCost };