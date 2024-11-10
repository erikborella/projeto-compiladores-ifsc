import { TokenPosition } from "./TokenPosition";

type CostResult = {
  position: TokenPosition;
  value: number;
}

type BlockCost = CostResult & {
  blockCost: CostResult;
  costs: CostResult[];
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