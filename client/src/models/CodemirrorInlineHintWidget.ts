import {
  EditorView,
  Decoration,
  DecorationSet,
  WidgetType,
} from "@codemirror/view";
import { EditorState, StateEffect, StateField } from "@codemirror/state";
import { Position, TokenPosition } from "./TokenPosition";
import { posToOffset } from "../services/editorViewTools";

class InlineHintWidget extends WidgetType {
  public text: string;

  constructor(text: string) {
    super();
    this.text = text;
  }

  toDOM() {
    const span = document.createElement("span");
    span.textContent = this.text;
    span.style.padding = "2px 4px";
    span.style.backgroundColor = "#eee";
    span.style.border = "1px solid #ccc";
    span.style.borderRadius = "3px";
    span.style.fontSize = "0.8em";
    span.style.marginLeft = "5px";
    span.style.color = 'black';
    return span;
  }

  ignoreEvent() {
    return true;
  }
}

// Step 2: Define a state field to manage inline hints
const addInlineHint = StateEffect.define();
const inlineHints = StateField.define({
  create() {
    return Decoration.none;
  },
  update(decorations, transaction) {
    // Apply any added hints
    decorations = decorations.map(transaction.changes);
    for (let effect of transaction.effects) {
      if (effect.is(addInlineHint)) {
        decorations = decorations.update({
          add: [
            Decoration.widget({
              widget: new InlineHintWidget(effect.value.text),
              side: 1, // Place it at the end of the line
            }).range(effect.value.pos),
          ],
        });
      }
    }
    return decorations;
  },
  provide: (f) => EditorView.decorations.from(f),
});

// Step 3: Function to add a hint
function addHint(view: EditorView, line: number, text: string) {
  // const pos = posToOffset(view.state.doc, position);
  const pos = view.state.doc.line(line).to;

  view.dispatch({
    effects: addInlineHint.of({ pos, text }),
  });
}

export { inlineHints, addHint }