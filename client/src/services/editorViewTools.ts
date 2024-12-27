import { EditorView } from "codemirror";
import { Position, TokenPosition } from "../models/TokenPosition";
import { EditorSelection, Text } from '@codemirror/state';

function posToOffset(doc: Text, position: Position): number {
  return doc.line(position.line).from + position.column - 1;
}

function selectPositionInReferenceCodeEditor(editorView: EditorView, tokenPosition: TokenPosition) {
  const { start, end } = tokenPosition;

  const startOffset = posToOffset(editorView.state.doc, start);
  const endOffset = posToOffset(editorView.state.doc, end);

  const editorSelection = EditorSelection.single(startOffset, endOffset);

  editorView.dispatch({
    selection: editorSelection,
    scrollIntoView: true,
  });
}

function setContentInCodeEditor(editorView: EditorView, content: string) {
  editorView.dispatch({
    changes: {
      from: 0,
      to: editorView.state.doc.length,
      insert: content,
    },
  });
}

export { selectPositionInReferenceCodeEditor, setContentInCodeEditor, posToOffset };
