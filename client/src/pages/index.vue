<template>
  <v-layout>

    <v-app-bar color="indigo">

      <template v-slot:prepend>
        <v-app-bar-nav-icon @click="drawer = !drawer"></v-app-bar-nav-icon>
      </template>

      <v-app-bar-title>Projeto Compilador</v-app-bar-title>

      <template v-slot:append>
        <v-btn text="Compilar" @click="uploadCodeBtn()" append-icon="mdi-send"></v-btn>
        <v-btn size="x-large" :icon="theme.global.current.value.dark ? 'mdi-white-balance-sunny' : 'mdi-weather-night'" @click="toggleTheme"></v-btn>
        <v-btn size="x-large" href="https://github.com/erikborella/projeto-compiladores-ifsc" target="_blank" icon="mdi-github"></v-btn>
      </template>

    </v-app-bar>

    <v-navigation-drawer v-model="drawer" width="700">
      <v-container class="d-flex flex-column ga-2">
        <v-card text="Lorem ipsum dolor sit amet consectetur adipisicing elit. Commodi, ratione debitis quis est labore voluptatibus! Eaque cupiditate minima, at placeat totam, magni doloremque veniam neque porro libero rerum unde voluptatem!">
          <div ref="codeEditorElement2" class="code-editor"></div>
        </v-card>
        <v-card text="Lorem ipsum dolor sit amet consectetur adipisicing elit. Commodi, ratione debitis quis est labore voluptatibus! Eaque cupiditate minima, at placeat totam, magni doloremque veniam neque porro libero rerum unde voluptatem!"></v-card>
        <v-card text="Lorem ipsum dolor sit amet consectetur adipisicing elit. Commodi, ratione debitis quis est labore voluptatibus! Eaque cupiditate minima, at placeat totam, magni doloremque veniam neque porro libero rerum unde voluptatem!"></v-card>
        <v-card text="Lorem ipsum dolor sit amet consectetur adipisicing elit. Commodi, ratione debitis quis est labore voluptatibus! Eaque cupiditate minima, at placeat totam, magni doloremque veniam neque porro libero rerum unde voluptatem!"></v-card>
        <v-card text="Lorem ipsum dolor sit amet consectetur adipisicing elit. Commodi, ratione debitis quis est labore voluptatibus! Eaque cupiditate minima, at placeat totam, magni doloremque veniam neque porro libero rerum unde voluptatem!"></v-card>
      </v-container>
    </v-navigation-drawer>

    <v-main>
      <div class="root">
        <div ref="codeEditorElement" class="code-editor"></div>
      </div>
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

<style lang="css">
  .root {
    display: flex;
    flex-direction: column;
    flex-grow: 1;
    height: 100%;
  }

  .code-editor {
    flex-grow: 1;
    height: 100%;
    overflow: auto;
  }

  .cm-editor {
    height: 100%;
    width: 100%;
  }

  html, body, #app {
    height: 100%;
    margin: 0;
    overflow: hidden;
  }

  v-main {
    display: flex;
    flex-direction: column;
    flex-grow: 1;
  }
</style>

<script lang="ts" setup>
  import { onMounted, ref } from 'vue';
  import { useTheme } from 'vuetify';
  import { useRouter } from 'vue-router';
  import { basicSetup, EditorView } from 'codemirror';
  import { oneDark } from '@codemirror/theme-one-dark';
  import { cppLanguage } from '@codemirror/lang-cpp';

  import compilerApi from '../services/compiler/compilerApi';

  const drawer = ref(true);
  const theme = useTheme();
  const router = useRouter();

  const isLoading = ref(false);

  const snackbar = ref({
    show: false,
    message: ''
  });

  const codeEditorElement = ref(null);
  const codeEditorElement2 = ref(null);
  
  let mainCodeEditor: EditorView;

  onMounted(() => {
    mainCodeEditor = new EditorView({
      doc: 'main() {\n\tprintf(\"Hello, World!\");\n}',
      extensions: [basicSetup, oneDark, cppLanguage],
      parent: codeEditorElement.value!
    });

    new EditorView({
      doc: 'Outro código aqui\nDe exemplo\nAqui',
      extensions: [basicSetup, oneDark, cppLanguage],
      parent: codeEditorElement2.value!
    });
    
  });

  function toggleTheme() {
    theme.global.name.value = theme.global.current.value.dark ? 'light' : 'dark';
  }

  function showErrorMessage(message) {
    snackbar.value.message = message;
    snackbar.value.show = true;
  }

  async function uploadCodeBtn() {
    isLoading.value = true;
    const code = mainCodeEditor.state.doc.toString();

    try {
      const codeId = await compilerApi.uploadCode(code);

      router.push(codeId);
    } catch (error) {
      console.log(error);
      // console.error(`Failed to upload the code: ${code}`);
      showErrorMessage(`Falha ao fazer o upload do código: ${error.message}`);
    }
    finally {
      isLoading.value = false;
    }
  }
</script>
