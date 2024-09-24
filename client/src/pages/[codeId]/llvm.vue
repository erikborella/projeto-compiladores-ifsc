<template>
  <v-layout class="fill-height">

    <v-navigation-drawer v-model="drawer" width="350">
      <v-container class="d-flex flex-column ga-2">
        <v-card
          elevation="4"
          title="Otimização"
          text="Selecione aqui o nível de otimização do código intermediário do LLVM.">
          <v-select
            variant="outlined"
            class="pa-3"
            label="Nível de otimização"
            :items="['Sem otimização']"
          ></v-select>
        </v-card>
      </v-container>
    </v-navigation-drawer>

    <v-main>
      <div ref="llvmIrCodeEditorElement" class="code-editor"></div>
    </v-main>
  </v-layout>
</template>

<style lang="css" scoped>

 .code-editor {
  height: 100%;
  overflow: auto;
 }

</style>

<script lang='ts' setup>
  import { ref, onMounted, useTemplateRef } from 'vue';
  import { useRoute } from 'vue-router';
  import compilerApi from '../../services/compiler/compilerApi';
  import { EditorView, basicSetup } from 'codemirror';
  import { EditorState } from '@codemirror/state';
  import { oneDark } from '@codemirror/theme-one-dark';
  import { cppLanguage } from '@codemirror/lang-cpp';

  const route = useRoute();

  const isLoading = ref(false);
  const drawer = ref(true);

  const snackbar = ref({
    show: false,
    message: ''
  });

  const llvmIrCodeEditorElement = useTemplateRef('llvmIrCodeEditorElement');

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