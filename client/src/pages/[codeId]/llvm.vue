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
            @update:modelValue="test()"
          ></v-select>
        </v-card>
      </v-container>
    </v-navigation-drawer>

    <v-main>
      <div ref="llvmIrCodeEditorElement" class="code-editor"></div>
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
 }

</style>

<script lang='ts' setup>
  import { ref, toRef, onMounted, useTemplateRef, defineProps, watch, defineEmits } from 'vue';
  import { useRoute } from 'vue-router';
  import { EditorView, basicSetup } from 'codemirror';
  import { EditorState } from '@codemirror/state';
  import { oneDark } from '@codemirror/theme-one-dark';
  import { cppLanguage } from '@codemirror/lang-cpp';
  import { OptimizationLevel } from '../../models/OptimizationLevel';
  import compilerApi from '../../services/compiler/compilerApi';

  const isConfigMenuOpen = defineModel<boolean>('isConfigMenuOpen');

  const route = useRoute();

  const isLoading = ref(false);

  const optimizationSelected = ref(OptimizationLevel.o0);
  const optimizationLevels = [
    { value: OptimizationLevel.o0, text: 'Sem otimização' },
    { value: OptimizationLevel.os, text: 'Padrão (os)' },
    { value: OptimizationLevel.o1, text: 'Leve (o1)' },
    { value: OptimizationLevel.o2, text: 'Média (o2)' },
    { value: OptimizationLevel.o3, text: 'Pesada (o3)' },
    { value: OptimizationLevel.oz, text: 'Tamanho (oz)' },
  ];

  const snackbar = ref({
    show: false,
    message: ''
  });

  const llvmIrCodeEditorElement = useTemplateRef('llvmIrCodeEditorElement');

  function test() {
    console.log(optimizationSelected);
  }

  onMounted(async () => {
    isLoading.value = true;

    let llvmIrCode: string;

    try {
      const codeId = route.params.codeId as string;

      llvmIrCode = await compilerApi.getLlvmIrCode(codeId);
    } catch (error) {
      console.error(error);
      showErrorMessage(`Falha ao fazer o download do código IR: ${error.message}`);

      return;
    }
    finally {
      isLoading.value = false;
    }

    new EditorView({
      doc: llvmIrCode,
      extensions: [
        basicSetup,
        cppLanguage,
        oneDark,
        EditorState.readOnly.of(true)
      ],
      parent: llvmIrCodeEditorElement.value!
    });

  });

  function showErrorMessage(message: string) {
    snackbar.value.message = message;
    snackbar.value.show = true;
  }

</script>