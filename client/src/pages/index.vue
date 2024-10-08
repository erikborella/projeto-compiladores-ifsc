<template>
  <v-layout>

    <v-app-bar color="indigo">

      <template v-slot:prepend>
        <v-app-bar-nav-icon icon="mdi-file-code-outline" @click="drawer = !drawer"></v-app-bar-nav-icon>
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
        <v-card
          elevation="4"
          title="Função main" 
          text="A função main é o ponto de entrada da execução de um programa, onde o código inicia sua execução."
        >
          <div ref="exampleCodeEditorMain" class="code-editor"></div>
        </v-card>

        <v-card 
          elevation="4"
          title="Declaração de váriaveis" 
        >
        <template v-slot:text>
          <p>As declarações de variáveis devem ser feitas antes de seu uso nos blocos de código.</p>
          <p>Os seguintes tipos de variáveis são suportados:</p>

          <v-card-text>
            <ul>
              <li><code>boolean</code></li>
              <li><code>char</code></li>
              <li><code>int</code></li>
              <li><code>float</code></li>
            </ul>
          </v-card-text>

          <p>É possível declarar várias variáveis do mesmo tipo na mesma linha, separando os nomes por vírgula (<code>,</code>).</p>
          <br>
          <p>Arrays de tamanho fixo podem ser declarados adicionando <code>[tamanho_da_dimensão]</code> ao lado do tipo. Arrays de múltiplas dimensões também são permitidos, e não há um limite de dimensões; basta adicionar mais <code>[tamanho_da_dimensão]</code> ao lado do tipo.</p>
        </template>
          <div ref="exampleCodeEditorVariables" class="code-editor"></div>
        </v-card>

        <v-card
          elevation="4"
          title="Atribuição de váriaveis"
          text="É possível atribuir valores a variáveis utilizando constantes, expressões matemáticas, valores de arrays ou valores retornados por funções"
        >
          <div ref="exampleCodeEditorVariableAttribuition" class="code-editor"></div>
        </v-card>

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

<style lang="css" scoped>
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
</style>

<script lang="ts" setup>
  import { ref, onMounted, onBeforeUnmount, useTemplateRef } from 'vue';
  import { useTheme } from 'vuetify';
  import { useRouter } from 'vue-router';
  import { basicSetup, EditorView } from 'codemirror';
  import { EditorState } from '@codemirror/state';
  import { keymap } from '@codemirror/view';
  import { indentWithTab } from '@codemirror/commands';
  import { oneDark } from '@codemirror/theme-one-dark';
  import { cppLanguage } from '@codemirror/lang-cpp';

  import compilerApi from '../services/compiler/compilerApi';

  const USER_CACHE_CODE_STORAGE_KEY = 'savedCode';

  const drawer = ref(true);
  const theme = useTheme();
  const router = useRouter();

  const isLoading = ref(false);

  const snackbar = ref({
    show: false,
    message: ''
  });

  const mainCodeEditorRef = useTemplateRef('codeEditorElement');
  let mainCodeEditor: EditorView;

  const exampleCodeEditorMain = useTemplateRef('exampleCodeEditorMain');
  const exampleCodeEditorVariables = useTemplateRef('exampleCodeEditorVariables');
  const exampleCodeEditorVariableAttribution = useTemplateRef('exampleCodeEditorVariableAttribuition');

  onMounted(() => {
    const cachedCode = loadCodeFromLocalStorage() ?? 'main() {\n\tprintln("Hello, Word!");\n}';

    mainCodeEditor = new EditorView({
      doc: cachedCode,
      extensions: [
        basicSetup,
        cppLanguage,
        oneDark,
        keymap.of([
          indentWithTab,
        ])
      ],
      parent: mainCodeEditorRef.value!,
    });

    new EditorView({
      doc: 
`main() {
  // declarações de váriaveis...
  
  // comandos...
}`,
      extensions: [
        basicSetup,
        cppLanguage,
        oneDark,
        EditorState.readOnly.of(true)
      ],
      parent: exampleCodeEditorMain.value!
    });

    new EditorView({
      doc:
`// Declaração de váriavel simples
int var1;

// Declaração de multiplas váriaveis;
boolean var2, var3;

// Declaração de arrays
float[5] vet;/
int[7][10] mat;`,
      extensions: [
        basicSetup,
        cppLanguage,
        oneDark,
        EditorState.readOnly.of(true)
      ],
      parent: exampleCodeEditorVariables.value!
    });

    new EditorView({
      doc: 
`// Declaração de váriaveis
int n1;
boolean flag;
float f1;
int[5][5] mat;

// Atribuição de constante;
n1 = 10;
f1 = -5.2;
mat[0][1] = 7;

// Atribuição por expressão matemática
n1 = n1 + f1 * 3;
flag = 10 > 7;

// Atribuição por valor de um array
n1 = mat[0][0] + mat[1][0];

// Atribuição por um valor de retorno de função
n1 = func funcao();`,
      extensions: [
        basicSetup,
        cppLanguage,
        oneDark,
        EditorState.readOnly.of(true)
      ],
      parent: exampleCodeEditorVariableAttribution.value!
    })
  })

  onBeforeUnmount(saveCodeToLocalStorage);
  window.onbeforeunload = saveCodeToLocalStorage;

  function saveCodeToLocalStorage() {
    const code = mainCodeEditor.state.doc.toString();
    localStorage.setItem(USER_CACHE_CODE_STORAGE_KEY, code);
  }

  function loadCodeFromLocalStorage(): string | null {
    const code = localStorage.getItem(USER_CACHE_CODE_STORAGE_KEY);
    return code;
  }

  function toggleTheme() {
    theme.global.name.value = theme.global.current.value.dark ? 'light' : 'dark';
  }

  function showErrorMessage(message: string) {
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
      console.error(error);
      showErrorMessage(`Falha ao fazer o upload do código: ${error.message}`);
    }
    
    isLoading.value = false;
  }
</script>
