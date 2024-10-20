<template>
  <v-layout class="fill-height">

    <v-navigation-drawer v-model="isConfigMenuOpen" width="600">
      <div ref="referenceCodeEditor" class="code-editor"></div>
    </v-navigation-drawer>

    <v-main>
      <div class="listContainer">
        <v-table height="100%" hover>
          <thead>
            <tr>
              <th class="text-left">
                Tipo
              </th>
              <th class="text-left">
                Valor
              </th>
            </tr>
          </thead>
          <tbody>
            <tr 
              v-for="(token, index) in tokens" 
              :key="index"
              @mouseover="selectTokenInReferenceCodeEditor(token.position)">
              <td>{{ token.type }}</td>
              <td>{{ token.value }}</td>
            </tr>
          </tbody>
        </v-table>
      </div>
    </v-main>

    <v-overlay v-model="isLoading" :opacity="0.8" persistent class="align-center justify-center">
      <v-progress-circular indeterminate color="primary" width="10" size="100">
      </v-progress-circular>
    </v-overlay>

    <v-snackbar variant="flat" color="error" v-model="snackbar.show" :timeout="5000" top>
      {{ snackbar.message }}
      <template v-slot:actions>
        <v-btn
          color="white"
          variant="text"
          @click="snackbar.show = false">Fechar</v-btn>
      </template>
    </v-snackbar>

  </v-layout>
</template>

<style scoped>
  .listContainer {
    width: 100%;
    height: 100%;
    overflow: auto;
  }

  .code-editor {
    flex-grow: 1;
    height: 100%;
    overflow: auto
  }
</style>

<script lang="ts" setup>
  import { defineModel, onMounted, ref, useTemplateRef } from 'vue';
  import { useRoute } from 'vue-router';
  import compilerApi from '../../services/compiler/compilerApi';
  import { EditorView } from 'codemirror';
  import { EditorSelection, EditorState, Text } from '@codemirror/state';
  import { syntaxViewSetup } from '../../models/CodemirrorCustomSetups';
  import { cppLanguage } from '@codemirror/lang-cpp';
  import { oneDark } from '@codemirror/theme-one-dark';
  import { Token } from '../../models/TokenRepresentation';
  import { Position, TokenPosition } from '../../models/TokenPosition';
  

  const isConfigMenuOpen = defineModel<boolean>('isConfigMenuOpen');

  const route = useRoute();

  const isLoading = ref(false);

  const referenceCode = ref<string>();
  let referenceCodeEditorView: EditorView;

  const tokens = ref<Token[]>();

  const snackbar = ref({
    show: false,
    message: ''
  });

  const referenceCodeEditor = useTemplateRef('referenceCodeEditor');

  onMounted(async () => {
    isLoading.value = true;
    const codeId = route.params.codeId as string;

    await downloadAndShowReferenceCode(codeId);
    await downloadAndShowTokenList(codeId);
    
    isLoading.value = false;
  });

  async function downloadAndShowReferenceCode(codeId: string) {
    try {
      referenceCode.value = await compilerApi.getCode(codeId);

      referenceCodeEditorView = new EditorView({
        doc: referenceCode.value,
        extensions: [
          syntaxViewSetup,
          cppLanguage,
          oneDark,
          EditorState.readOnly.of(true),
          EditorView.editable.of(false),
        ],
        parent: referenceCodeEditor.value!
      });

    } catch (error) {
      console.error(error);
      showErrorMessage(`Falha ao fazer o download do código: ${error.message}`);
    }
  } 

  async function downloadAndShowTokenList(codeId: string) {
    try {
      tokens.value = await compilerApi.getTokenList(codeId);
    } catch (error) {
      console.error(error);
      showErrorMessage(`Falha ao fazer o da list de tokens: ${error.message}`);
    }
  }

  function showErrorMessage(message: string) {
    snackbar.value.message = message;
    snackbar.value.show = true;
  }

  function posToOffset(doc: Text, position: Position): number {
    return doc.line(position.line).from + position.column - 1;
  }

  function selectTokenInReferenceCodeEditor(tokenPosition: TokenPosition) {
    const { start, end } = tokenPosition;

    const startOffset = posToOffset(referenceCodeEditorView.state.doc, start);
    const endOffset = posToOffset(referenceCodeEditorView.state.doc, end);

    const editorSelection = EditorSelection.single(startOffset, endOffset);

    referenceCodeEditorView.dispatch({
      selection: editorSelection,
      scrollIntoView: true,
    });
  }
</script>