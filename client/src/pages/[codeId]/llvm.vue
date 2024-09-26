<template>
  <v-layout class="fill-height">

    <v-navigation-drawer v-model="isConfigMenuOpen" width="350">
      <v-container class="d-flex flex-column ga-2">
        <v-card
          elevation="4"
          title="Otimização"
          text="Selecione aqui o nível de otimização do código intermediário do LLVM.">
          <v-select
            variant="outlined"
            class="pa-3"
            label="Nível de otimização"
            v-model="optimizationSelected"
            :items="optimizationLevels"
            item-title="text"
            item-value="value"
            @update:modelValue="changeOptimizationLevel()"
            ></v-select>
            
          <v-divider />

          <v-card-title>Comparação de otimização</v-card-title>
          <v-card-text>Compare o código do LLVM IR entre diferentes níveis de otimizações</v-card-text>

          <v-checkbox 
            label="Ativar comparação" 
            v-model="isMergeEditorVisible"
            @update:modelValue="($value: boolean) => toggleEditorMode($value)"
          ></v-checkbox>

          <v-select
            v-if="isMergeEditorVisible"
            variant="outlined"
            class="px-3"
            label="Nível de otimização para comparar"
            v-model="optimizationToCompareSelected"
            :items="getOptimizationLevelsToCompare"
            item-title="text"
            item-value="value"
            @update:modelValue="changeOptimizationLevel()"
          ></v-select>            
        </v-card>
      </v-container>
    </v-navigation-drawer>

    <v-main>
      <div v-if="isMergeEditorVisible" ref="llvmIrCodeMergeElement" class="code-editor"></div>
      <div v-else ref="llvmIrCodeEditorElement" class="code-editor"></div>
    </v-main>

    <v-overlay v-model="isLoading" :opacity="0.8" persistent class="align-center justify-center">
        <v-progress-circular
          indeterminate
          color="primary"
          width="10"
          size="100">
        </v-progress-circular>
    </v-overlay>
  </v-layout>
</template>

<style lang="css" scoped>

 .code-editor {
  height: 100%;
  overflow: auto;
  background-color: #282c34;
 }

</style>

<script lang='ts' setup>
  import { ref, onMounted, computed, useTemplateRef, nextTick } from 'vue';
  import { useRoute, useRouter } from 'vue-router';
  import { EditorView, basicSetup } from 'codemirror';
  import { MergeView } from '@codemirror/merge';
  import { EditorState } from '@codemirror/state';
  import { oneDark } from '@codemirror/theme-one-dark';
  import { cppLanguage } from '@codemirror/lang-cpp';
  import { OptimizationLevel } from '../../models/OptimizationLevel';
  import compilerApi from '../../services/compiler/compilerApi';

  const isConfigMenuOpen = defineModel<boolean>('isConfigMenuOpen');

  const route = useRoute();
  const router = useRouter();

  const isLoading = ref(false);

  const optimizationLevels = [
    { value: OptimizationLevel.o0, text: 'Sem otimização' },
    { value: OptimizationLevel.os, text: 'Padrão (os)' },
    { value: OptimizationLevel.o1, text: 'Leve (o1)' },
    { value: OptimizationLevel.o2, text: 'Média (o2)' },
    { value: OptimizationLevel.o3, text: 'Pesada (o3)' },
    { value: OptimizationLevel.oz, text: 'Tamanho (oz)' },
  ];
  const optimizationSelected = ref(optimizationLevels[0].value);

  const getOptimizationLevelsToCompare = computed(() => {
    const optimizationLevelsAvailable = [...optimizationLevels]
      .filter(el => el.value !== optimizationSelected.value);

      return optimizationLevelsAvailable;
  });
  const optimizationToCompareSelected = ref(getOptimizationLevelsToCompare.value[0].value);

  const snackbar = ref({
    show: false,
    message: ''
  });

  const isMergeEditorVisible = ref(false);

  const llvmIrCodeEditorElement = useTemplateRef('llvmIrCodeEditorElement');
  let llvmIrEditorView: EditorView | null;

  const llvmIrCodeMergeElement = useTemplateRef('llvmIrCodeMergeElement');
  let llvmIrMergeView: MergeView | null;

  function getQueryStateOptimizationLevel(): OptimizationLevel {
    const queryOptimizationLevel = route.query.optimizationLevel;

    const optimizationLevelConverted = OptimizationLevel[queryOptimizationLevel?.valueOf() as string] as OptimizationLevel | undefined;
    const validOptimizationLevel = optimizationLevelConverted ?? OptimizationLevel.o0;
    
    return validOptimizationLevel;
  }

  function getQueryStateIsMergeEditorVisible(): boolean {
    const queryIsMergeEditorVisible = route.query.compareOptimization;
    const hasValidValue = (queryIsMergeEditorVisible === 'true') || (queryIsMergeEditorVisible === 'false');

    if (!hasValidValue) {
      const defaultValue = false;
      return defaultValue;
    }

    return queryIsMergeEditorVisible === 'true';
  }

  function getQueryStateOptimizationLevelToCompare(): OptimizationLevel {
    const queryOptimizationLevel = route.query.optimizationLevelToCompare;

    const optimizationLevelConverted = OptimizationLevel[queryOptimizationLevel?.valueOf() as string] as OptimizationLevel | undefined;
    const validOptimizationLevel = optimizationLevelConverted ?? getOptimizationLevelsToCompare.value[0].value;
    
    return validOptimizationLevel;
  }

  function setupQueryStates() {
    const currentOptimizationLevel = getQueryStateOptimizationLevel();
    optimizationSelected.value = currentOptimizationLevel;

    const isMergeEditorCurrentlyVisible = getQueryStateIsMergeEditorVisible();
    isMergeEditorVisible.value = isMergeEditorCurrentlyVisible;

    const optimizationLevelToCompare = getQueryStateOptimizationLevelToCompare();
    optimizationToCompareSelected.value = optimizationLevelToCompare;

    const queryStatesToUpdate = {
      'optimizationLevel': currentOptimizationLevel,
      'compareOptimization': isMergeEditorCurrentlyVisible,
      'optimizationLevelToCompare': optimizationLevelToCompare,
    };

    updateQueryState(queryStatesToUpdate);
  }
  
  async function changeOptimizationLevel() {
    isLoading.value = true;

    updateQueryState({
      'optimizationLevel': optimizationSelected.value.toString(),
      'optimizationLevelToCompare': optimizationToCompareSelected.value.toString(),
    });

    await downloadAndShowIrCode();
  }

  onMounted(async () => {
    setupQueryStates();

    if (isMergeEditorVisible.value) {
      await showMergeView();
    }
    else {
      await showEditorView();
    }

  });

  function showErrorMessage(message: string) {
    snackbar.value.message = message;
    snackbar.value.show = true;
  }

  async function downloadAndShowIrCode() {
    if (isMergeEditorVisible.value) {
      await downloadAndShowMergeIrCode();
    }
    else {
      await downloadAndShowEditorIrCode();
    }
  }

  async function downloadAndShowMergeIrCode() {
    isLoading.value = true;

    if (optimizationToCompareSelected.value === optimizationSelected.value) {
      optimizationToCompareSelected.value = getOptimizationLevelsToCompare.value[0].value;
      updateQueryState({ 'optimizationLevelToCompare': optimizationToCompareSelected.value });
    }

    llvmIrMergeView?.destroy();

    const aIrCode = await downloadIrCode(optimizationSelected.value);
    const bIrCode = await downloadIrCode(optimizationToCompareSelected.value);

    const aIrCodeWithIdentification = `// Otimização ${optimizationLevels.find(v => v.value === optimizationSelected.value)?.text}\n\n${aIrCode}`;
    const bIrCodeWithIdentification = `// Otimização ${optimizationLevels.find(v => v.value === optimizationToCompareSelected.value)?.text}\n\n${bIrCode}`;

    llvmIrMergeView = new MergeView({
      parent: llvmIrCodeMergeElement.value!,
      gutter: true,
      highlightChanges: true,
      orientation: 'a-b',
      a: {
        doc: aIrCodeWithIdentification,
        extensions: [
          basicSetup,
          cppLanguage,
          oneDark,
          EditorState.readOnly.of(true),
        ],
      },
      b: {
        doc: bIrCodeWithIdentification,
        extensions: [
          basicSetup,
          cppLanguage,
          oneDark,
          EditorState.readOnly.of(true),
        ],
      }
    });
   
    isLoading.value = false;
  }

  async function downloadAndShowEditorIrCode() {
    isLoading.value = true;

    llvmIrEditorView?.destroy();

    const irCode = await downloadIrCode(optimizationSelected.value);

    llvmIrEditorView = new EditorView({
      parent: llvmIrCodeEditorElement.value!,
      doc: irCode,
      extensions: [
        basicSetup,
        cppLanguage,
        oneDark,
        EditorState.readOnly.of(true),
      ],
    });

    isLoading.value = false;
  }

  async function downloadIrCode(optimizationLevel: OptimizationLevel): Promise<string> {
    try {
      const codeId = route.params.codeId as string;

      const llvmIrCode = await compilerApi.getLlvmIrCode(codeId, optimizationLevel);
      return llvmIrCode;
    } catch (error) {
      console.error(error);
      showErrorMessage(`Falha ao fazer o download do código IR: ${error.message}`);

      return `Falha ao fazer o download do código IR: ${error.message}`;
    }
  }

  async function showEditorView() {
    isMergeEditorVisible.value = false;
    llvmIrMergeView = null;

    await nextTick();

    await downloadAndShowIrCode();
  }

  async function showMergeView() {
    isMergeEditorVisible.value = true;
    llvmIrEditorView = null;

    await nextTick();

    await downloadAndShowMergeIrCode();
  }

  async function toggleEditorMode(shouldShowMergeEditor: boolean) {
    updateQueryState({ 'compareOptimization': shouldShowMergeEditor });

    if (shouldShowMergeEditor) {
      await showMergeView();
      return;
    }
    
    await showEditorView();
  }

  function updateQueryState(newStates: { [key: string]: any }) {

    const updatedQuery = {
      ...route.query,
      ...newStates
    };

    router.push({ name: '/[codeId]/llvm', query: updatedQuery});
  }

</script>