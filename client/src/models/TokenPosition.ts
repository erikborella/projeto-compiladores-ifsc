type Position = {
  line: number;
  column: number;
}

type TokenPosition = {
  start: Position;
  end: Position
}

export type { Position, TokenPosition };