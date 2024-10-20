import { TokenPosition } from "./TokenPosition";

type Token = {
  position: TokenPosition;
  type: string;
  value: string;
};

export type { Token };