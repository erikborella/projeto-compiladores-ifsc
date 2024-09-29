type Position = {
  line: number;
  column: number;
}

type TokenPosition = {
  start: Position;
  end: Position;
}

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

export type { Position, TokenPosition };
export type SyntaxTreeRepresentation = SyntaxTreeNode | SyntaxTreeLeaf;