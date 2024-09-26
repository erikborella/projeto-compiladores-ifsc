<template>
    <v-layout class="fill-height">
  
      <v-navigation-drawer v-model="isConfigMenuOpen" width="350">
        <v-container class="d-flex flex-column ga-2">
          <v-card
            elevation="4"
            title="Otimização"
            text="Selecione aqui o nível de otimização do código Assembly.">
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
            <v-card-text>Compare o código Assembly entre diferentes níveis de otimizações</v-card-text>
  
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
        <div v-if="isMergeEditorVisible" ref="asmCodeMergeElement" class="code-editor"></div>
        <div v-else ref="asmCodeEditorElement" class="code-editor"></div>
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
  
    const asmCodeEditorElement = useTemplateRef('asmCodeEditorElement');
    let asmEditorView: EditorView | null;
  
    const asmCodeMergeElement = useTemplateRef('asmCodeMergeElement');
    let asmMergeView: MergeView | null;
  
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
  
      await downloadAndShowAsmCode();
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
  
    async function downloadAndShowAsmCode() {
      if (isMergeEditorVisible.value) {
        await downloadAndShowMergeAsmCode();
      }
      else {
        await downloadAndShowEditorAsmCode();
      }
    }
  
    async function downloadAndShowMergeAsmCode() {
      isLoading.value = true;
  
      if (optimizationToCompareSelected.value === optimizationSelected.value) {
        optimizationToCompareSelected.value = getOptimizationLevelsToCompare.value[0].value;
        updateQueryState({ 'optimizationLevelToCompare': optimizationToCompareSelected.value });
      }
  
      asmMergeView?.destroy();
  
      const aAsmCode = await downloadAsmCode(optimizationSelected.value);
      const bAsmCode = await downloadAsmCode(optimizationToCompareSelected.value);
  
      const aAsmCodeWithIdentification = `// Otimização ${optimizationLevels.find(v => v.value === optimizationSelected.value)?.text}\n\n${aAsmCode}`;
      const bAsmCodeWithIdentification = `// Otimização ${optimizationLevels.find(v => v.value === optimizationToCompareSelected.value)?.text}\n\n${bAsmCode}`;
  
      asmMergeView = new MergeView({
        parent: asmCodeMergeElement.value!,
        gutter: true,
        highlightChanges: true,
        orientation: 'a-b',
        a: {
          doc: aAsmCodeWithIdentification,
          extensions: [
            basicSetup,
            cppLanguage,
            oneDark,
            EditorState.readOnly.of(true),
          ],
        },
        b: {
          doc: bAsmCodeWithIdentification,
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
  
    async function downloadAndShowEditorAsmCode() {
      isLoading.value = true;
  
      asmEditorView?.destroy();
  
      const asmCode = await downloadAsmCode(optimizationSelected.value);
  
      asmEditorView = new EditorView({
        parent: asmCodeEditorElement.value!,
        doc: asmCode,
        extensions: [
          basicSetup,
          cppLanguage,
          oneDark,
          EditorState.readOnly.of(true),
        ],
      });
  
      isLoading.value = false;
    }
  
    async function downloadAsmCode(optimizationLevel: OptimizationLevel): Promise<string> {
      try {
        const codeId = route.params.codeId as string;
  
        const asmCode = await compilerApi.getAsmCode(codeId, optimizationLevel);
        return asmCode;
      } catch (error) {
        console.error(error);
        showErrorMessage(`Falha ao fazer o download do código Assembly: ${error.message}`);
  
        return `Falha ao fazer o download do código Assembly: ${error.message}`;
      }
    }
  
    async function showEditorView() {
      isMergeEditorVisible.value = false;
      asmMergeView = null;
  
      await nextTick();
  
      await downloadAndShowAsmCode();
    }
  
    async function showMergeView() {
      isMergeEditorVisible.value = true;
      asmEditorView = null;
  
      await nextTick();
  
      await downloadAndShowMergeAsmCode();
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
  
      router.push({ name: '/[codeId]/asm', query: updatedQuery});
    }
  
  </script>