import { TokenPosition } from "./TokenPosition";

type SyntaxTreeFragment = {
  position: TokenPosition;
  label: string;
  type: string;
}

type SyntaxTreeLeaf = SyntaxTreeFragment & {
  type: 'leaf';
  value: string;
}

type SyntaxTreeNode = SyntaxTreeFragment & {
  type: 'node';
  children: [SyntaxTreeFragment];
}

export type SyntaxTreeRepresentation = SyntaxTreeNode | SyntaxTreeLeaf;