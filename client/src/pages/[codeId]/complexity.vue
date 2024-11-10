<template>
  <v-layout class="fill-height">

    <v-navigation-drawer v-model="isConfigMenuOpen" width="350">
      <v-container class="d-flex flex-column ga-2">
        <v-card 
          elevation="4"
          title="Execução"
        >
          <template v-slot:text>
            <v-card-text>
              Veja o cálculo da complexidade do programa
            </v-card-text>

            <v-card-text>
              <ul>
                <li>Definições de funções: <code>T(n) = 0</code>.</li>
                <li>Retornos de funções: <code>T(n) = 0</code>.</li>
                <li>Declarações de variáveis: <code>T(n) = 0</code>.</li>
                <li>Atribuições: <code>T(n) = 1</code>.</li>
                <li>Escrita e lida de valores: <code>T(n) = 1</code>.</li>
                <li><code>if</code> e <code>else</code>: Considera <code>T(n)</code> do bloco com maior custo.</li>
                <li><code>for</code> Realiza o calculo junto da variável marcada como <code>input</code>.</li>
                <li><code>while</code> não é suportado para o cálculo ainda.</li>
                <li>Chamadas de funções não tem o calculo do <code>T(n)</code> suportado ainda.</li>
              </ul>
            </v-card-text>
          </template>
        </v-card>
      </v-container>
    </v-navigation-drawer>

    <v-main>
      <div ref="referenceCodeEditor" class="code-editor"></div>
    </v-main>

    <v-overlay v-model="isLoading" :opacity="0.8" persistent class="align-center justify-center">
        <v-progress-circular
          indeterminate
          color="primary"
          width="10"
          size="100">
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

<style lang="css" scoped>

  .code-editor {
    height: 100%;
    overflow: auto;
    background-color: #282c34;
  }

</style>

<script lang="ts" setup>
  import { ref, useTemplateRef, onMounted } from 'vue';
  import { useRoute } from 'vue-router';
  import compilerApi from '../../services/compiler/compilerApi';
  import { EditorView } from 'codemirror';
  import { EditorState } from '@codemirror/state';
  import { syntaxViewSetup } from '../../models/CodemirrorCustomSetups';
  import { cppLanguage } from '@codemirror/lang-cpp';
  import { oneDark } from '@codemirror/theme-one-dark';
  import { addHint, inlineHints } from '../../models/CodemirrorInlineHintWidget';
  import { CostResult, isBlockCost, isVariableCost } from '../../models/CostResult';

  const route = useRoute();

  const isLoading = ref(false);

  const isConfigMenuOpen = defineModel<boolean>('isConfigMenuOpen');

  const snackbar = ref({
    show: false,
    message: ''
  });

  const referenceCodeEditor = useTemplateRef('referenceCodeEditor');
  const referenceCode = ref<string>();
  let referenceCodeEditorView: EditorView;

  onMounted(async () => {
    isLoading.value = true;

    const codeId = route.params.codeId as string;

    await downloadAndShowReferenceCode(codeId);
    await downloadAndShowComplexityCosts(codeId);

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
          inlineHints,
        ],
        parent: referenceCodeEditor.value!
      });

    } catch (error) {
      console.error(error);
      showErrorMessage(`Falha ao fazer o download do código: ${error.message}`);
    }
  }

  async function downloadAndShowComplexityCosts(codeId: string) {
    try {
      const costResult = await compilerApi.getComplexityAnalysis(codeId);
      addHintsFromCostResult(costResult);
    } catch (error) {
      console.error(error);
      showErrorMessage(`Falha ao fazer o download da análise da complexidade do código: ${error.message}`);
    }
  }

  function addHintsFromCostResult(costResult: CostResult) {
    if (!costResult.position)
      return;

    addHint(referenceCodeEditorView, costResult.position.start.line, `T(n) = ${costResult.value}`);

    if (isBlockCost(costResult)) {
      for (const cost of costResult.costs) {
        addHintsFromCostResult(cost);
      }
    }

    if (isVariableCost(costResult)) {
      console.log('teste');
    }
  }

  function showErrorMessage(message: string) {
    snackbar.value.message = message;
    snackbar.value.show = true;
  }
</script>