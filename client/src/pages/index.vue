<template>
  <v-layout>

    <v-app-bar color="indigo">

      <template v-slot:prepend>
        <v-app-bar-nav-icon icon="mdi-file-code-outline" @click="drawer = !drawer"></v-app-bar-nav-icon>
      </template>

      <v-app-bar-title>{{ t('index.title') }}</v-app-bar-title>

      <template v-slot:append>
        <v-btn :text="t('index.compile')" @click="uploadCodeBtn()" append-icon="mdi-send"></v-btn>
        <v-menu>
          <template v-slot:activator="{ props }">
            <v-btn v-bind="props" append-icon="mdi-menu-down">
              {{ t('index.examples') }}
            </v-btn>
          </template>
          <v-list>
            <v-list-subheader>{{ t('examples.sections.basic') }}</v-list-subheader>
            <v-list-item
              v-for='(item, index) in basicExamples'
              :key='index'
              :value='index'
              @click="updateCodeEditorStateCode(t(item.code))"
            >
              <v-list-item-title>{{ t(item.name) }}</v-list-item-title>
            </v-list-item>
            <v-divider></v-divider>

            <v-list-subheader>{{ t('examples.sections.recursive') }}</v-list-subheader>
            <v-list-item
              v-for='(item, index) in recursiveExamples'
              :key='index'
              :value='index'
              @click="updateCodeEditorStateCode(item.code)"
            >
              <v-list-item-title>{{ item.name }}</v-list-item-title>
            </v-list-item>
            <v-divider></v-divider>

            <v-list-subheader>{{ t('examples.sections.sorting') }}</v-list-subheader>
            <v-list-item
              v-for='(item, index) in sortingExamples'
              :key='index'
              :value='index'
              @click="updateCodeEditorStateCode(t(item.code))"
            >
              <v-list-item-title>{{ t(item.name) }}</v-list-item-title>
            </v-list-item>
            <v-divider></v-divider>

            <v-list-subheader>{{ t('examples.sections.games') }}</v-list-subheader>
            <v-list-item
              v-for='(item, index) in gamesExample'
              :key='index'
              :value='index'
              @click="updateCodeEditorStateCode(item.code)"
            >
              <v-list-item-title>{{ item.name }}</v-list-item-title>
            </v-list-item>
          </v-list>
        </v-menu>
        <v-btn size="x-large" :icon="theme.global.current.value.dark ? 'mdi-white-balance-sunny' : 'mdi-weather-night'" @click="toggleTheme"></v-btn>
        <LanguageSelector />
        <v-btn size="x-large" href="https://github.com/erikborella/projeto-compiladores-ifsc" target="_blank" icon="mdi-github"></v-btn>
      </template>

    </v-app-bar>

    <v-navigation-drawer v-model="drawer" width="700">
      <v-container class="d-flex flex-column ga-2">
        <v-card
          elevation="4"
          :title="t('index.exampleMain.title')"
          :text="t('index.exampleMain.text')"
        >
          <div ref="exampleCodeEditorMain" class="code-editor"></div>
        </v-card>

        <v-card
          elevation="4"
          :title="t('index.exampleVariables.title')"
        >
          <template v-slot:text>
            <p>{{ t('index.exampleVariables.description') }}</p>
            <p>{{ t('index.exampleVariables.variablesTypes') }}</p>

            <v-card-text>
              <ul>
                <li><code>boolean</code></li>
                <li><code>char</code></li>
                <li><code>int</code></li>
                <li><code>float</code></li>
              </ul>
            </v-card-text>

            <i18n-t keypath="index.exampleVariables.inlineVariables" tag="p">
              <template v-slot:comma>
                <code>,</code>
              </template>
            </i18n-t>

            <br>

            <i18n-t keypath="index.exampleVariables.arrayVariables" tag="p">
              <template v-slot:dimensionSize>
                <code>{{ t('index.exampleVariables.dimensionSize') }}</code>
              </template>
            </i18n-t>
          </template>
          <div ref="exampleCodeEditorVariables" class="code-editor"></div>
        </v-card>

        <v-card
          elevation="4"
          :title="t('index.exampleAttribuition.title')"
        >
          <template v-slot:text>
            <p>{{ t('index.exampleAttribuition.description') }}</p>
            <p>{{ t('index.exampleAttribuition.operatorsDescription') }}</p>

            <v-card-text>
              <ul>
                <li><code>+</code> {{ t('index.exampleAttribuition.operators.add') }}</li>
                <li><code>-</code> {{ t('index.exampleAttribuition.operators.sub') }}</li>
                <li><code>*</code> {{ t('index.exampleAttribuition.operators.mul') }}</li>
                <li><code>/</code> {{ t('index.exampleAttribuition.operators.div') }}</li>
                <li><code>%</code> {{ t('index.exampleAttribuition.operators.mod') }}</li>
              </ul>
            </v-card-text>
          </template>
          <div ref="exampleCodeEditorVariableAttribuition" class="code-editor"></div>
        </v-card>

        <v-card
          elevation="4"
          :title="t('index.exampleVariablePrint.title')"
        >
          <template v-slot:text>
            <i18n-t keypath="index.exampleVariablePrint.description" tag="p">
              <template v-slot:print>
                <code>print</code>
              </template>
              <template v-slot:println>
                <code>println</code>
              </template>
            </i18n-t>
            <br>
            <p>{{ t('index.exampleVariablePrint.modelsDescription') }}</p>

            <v-card-text>
              <ul>
                <li><code>%d</code> {{ t('index.exampleVariablePrint.models.int') }}</li>
                <li><code>%b</code> {{ t('index.exampleVariablePrint.models.boolean') }}</li>
                <li><code>%f</code> {{ t('index.exampleVariablePrint.models.float') }}</li>
                <li><code>%.Nf</code> {{ t('index.exampleVariablePrint.models.floatLimited') }}</li>
              </ul>
            </v-card-text>
          </template>
          <div ref="exampleCodeEditorVariablePrint" class="code-editor"></div>
        </v-card>

        <v-card
          elevation="4"
          :title="t('index.exampleVariableScan.title')"
        >
          <template v-slot:text>
            <i18n-t keypath="index.exampleVariableScan.description" tag="p">
              <template v-slot:scanf>
                <code>scanf</code>
              </template>
            </i18n-t>
          </template>

          <div ref="exampleCodeEditorVariableScanf" class="code-editor"></div>
        </v-card>

        <v-card
          elevation="4"
          :title="t('index.exampleFunctionDeclaration.title')"
        >
          <template v-slot:text>
            <i18n-t keypath="index.exampleFunctionDeclaration.descriptionStructure" tag="p">
              <template v-slot:void>
                <code>void</code>
              </template>
            </i18n-t>
            <p>{{ t('index.exampleFunctionDeclaration.descriptionName') }}</p>
            <p>{{ t('index.exampleFunctionDeclaration.descriptionParameters') }}</p>
            <p>{{ t('index.exampleFunctionDeclaration.descriptionBlock') }}</p>
            <br>
            <i18n-t keypath="index.exampleFunctionDeclaration.descriptionReturn" tag="p">
              <template v-slot:return>
                <code>return</code>
              </template>
            </i18n-t>
          </template>
          <div ref="exampleCodeEditorVariableFunctionDeclaration" class="code-editor"></div>
        </v-card>

        <v-card
          elevation="4"
          :title="t('index.exampleFunctionCall.title')"
        >
          <template v-slot:text>
            <!-- <p>Para chamar as funções declaradas, deve-se usar primeiro a palavra reservada <code>func</code> seguido do nome da função, seguido de parêntesis.</p> -->
            <i18n-t keypath="index.exampleFunctionCall.descriptionCall" tag="p">
              <template v-slot:func>
                <code>func</code>
              </template>
            </i18n-t>

            <p>{{ t('index.exampleFunctionCall.descriptionParameters') }}</p>
            <br>
            <p>{{ t('index.exampleFunctionCall.descriptionReturn') }}</p>
          </template>
          <div ref="exampleCodeEditorVariableFunctionCall" class="code-editor"></div>
        </v-card>

        <v-card
          elevation="4"
          :title="t('index.exampleIf.title')"
        >
          <template v-slot:text>
            <i18n-t keypath="index.exampleIf.description" tag="p">
              <template v-slot:if>
                <code>if</code>
              </template>
            </i18n-t>

            <br>

            <i18n-t keypath="index.exampleIf.descriptionStructure" tag="p">
              <template v-slot:if>
                <code>if</code>
              </template>
            </i18n-t>

            <i18n-t keypath="index.exampleIf.descriptionElse" tag="p">
              <template v-slot:if>
                <code>if</code>
              </template>
              <template v-slot:else>
                <code>else</code>
              </template>
            </i18n-t>

            <br>
            <p>{{ t('index.exampleIf.descriptionOperators') }}</p>

            <v-card-text>
              <ul>
                <li><code> > </code>: {{ t('index.exampleIf.operators.greater') }}</li>
                <li><code> >= </code>: {{ t('index.exampleIf.operators.greaterEqual') }}</li>
                <li><code> < </code>: {{ t('index.exampleIf.operators.less') }}</li>
                <li><code> <= </code>: {{ t('index.exampleIf.operators.lessEqual') }}</li>
                <li><code> == </code>: {{ t('index.exampleIf.operators.equal') }}</li>
                <li><code> != </code>: {{ t('index.exampleIf.operators.notEqual') }}</li>
              </ul>
            </v-card-text>

            <p>{{ t('index.exampleIf.descriptionConditionalOperators') }}</p>

            <v-card-text>
              <ul>
                <li><code> && </code>: {{ t('index.exampleIf.conditionalOperators.and') }}</li>
                <li><code> || </code>: {{ t('index.exampleIf.conditionalOperators.or') }}</li>
                <li><code> ! </code>: {{ t('index.exampleIf.conditionalOperators.not') }}</li>
              </ul>
            </v-card-text>
          </template>
          <div ref="exampleCodeEditorVariableIf" class="code-editor"></div>
        </v-card>


        <v-card
          elevation="4"
          :title="t('index.exampleWhile.title')"
        >
          <template v-slot:text>
            <i18n-t keypath="index.exampleWhile.description" tag="p">
              <template v-slot:while>
                <code>while</code>
              </template>
            </i18n-t>

            <br>

            <i18n-t keypath="index.exampleWhile.descriptionStructure" tag="p">
              <template v-slot:while>
                <code>while</code>
              </template>
              <template v-slot:if>
                <code>if</code>
              </template>
            </i18n-t>
          </template>
          <div ref="exampleCodeEditorVariableWhile" class="code-editor"></div>
        </v-card>

        <v-card
          elevation="4"
          :title="t('index.exampleFor.title')"
        >
          <template v-slot:text>
            <i18n-t keypath="index.exampleFor.description" tag="p">
              <template v-slot:for>
                <code>for</code>
              </template>
            </i18n-t>

            <br>

            <i18n-t keypath="index.exampleFor.descriptionStructure" tag="p">
              <template v-slot:while>
                <code>while</code>
              </template>
            </i18n-t>
          </template>
          <div ref="exampleCodeEditorVariableFor" class="code-editor"></div>
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
  import { ref, onMounted, onBeforeUnmount, useTemplateRef, watch } from 'vue';
  import { useTheme } from 'vuetify';
  import { useRouter } from 'vue-router';
  import { useTranslate } from '../locales/index.ts';
  import { basicSetup, EditorView } from 'codemirror';
  import { EditorState } from '@codemirror/state';
  import { keymap } from '@codemirror/view';
  import { indentWithTab } from '@codemirror/commands';
  import { oneDark } from '@codemirror/theme-one-dark';
  import { cppLanguage } from '@codemirror/lang-cpp';
  import { gamesExample, basicExamples, sortingExamples, recursiveExamples } from '../models/CodeExamples';
  import { setContentInCodeEditor } from '../services/editorViewTools.ts';

  import LanguageSelector from '../components/LanguageSelector.vue';

  import compilerApi from '../services/compiler/compilerApi';

  const USER_CACHE_CODE_STORAGE_KEY = 'savedCode';

  const drawer = ref(true);
  const theme = useTheme();
  const router = useRouter();
  const { translate: t, locale } = useTranslate();

  const isLoading = ref(false);

  const snackbar = ref({
    show: false,
    message: ''
  });

  const mainCodeEditorRef = useTemplateRef('codeEditorElement');
  let mainCodeEditor: EditorView;

  const exampleCodeEditorMain = useTemplateRef('exampleCodeEditorMain');
  let exampleCodeEditorMainView: EditorView;

  const exampleCodeEditorVariables = useTemplateRef('exampleCodeEditorVariables');
  let exampleCodeEditorVariablesView: EditorView;

  const exampleCodeEditorVariableAttribution = useTemplateRef('exampleCodeEditorVariableAttribuition');
  let exampleCodeEditorVariableAttributionView: EditorView;

  const exampleCodeEditorVariablePrint = useTemplateRef('exampleCodeEditorVariablePrint');
  let exampleCodeEditorVariablePrintView: EditorView;

  const exampleCodeEditorVariableScanf = useTemplateRef('exampleCodeEditorVariableScanf');
  let exampleCodeEditorVariableScanfView: EditorView;

  const exampleCodeEditorVariableFunctionDeclaration = useTemplateRef('exampleCodeEditorVariableFunctionDeclaration');
  let exampleCodeEditorVariableFunctionDeclarationView: EditorView;

  const exampleCodeEditorVariableFunctionCall = useTemplateRef('exampleCodeEditorVariableFunctionCall');
  let exampleCodeEditorVariableFunctionCallView: EditorView;

  const exampleCodeEditorVariableIf = useTemplateRef('exampleCodeEditorVariableIf');
  let exampleCodeEditorVariableIfView: EditorView;

  const exampleCodeEditorVariableWhile = useTemplateRef('exampleCodeEditorVariableWhile');
  let exampleCodeEditorVariableWhileView: EditorView;

  const exampleCodeEditorVariableFor = useTemplateRef('exampleCodeEditorVariableFor');
  let exampleCodeEditorVariableForView: EditorView;

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

    exampleCodeEditorMainView = new EditorView({
      extensions: [
        basicSetup,
        cppLanguage,
        oneDark,
        EditorState.readOnly.of(true)
      ],
      parent: exampleCodeEditorMain.value!
    });

    exampleCodeEditorVariablesView = new EditorView({
      extensions: [
        basicSetup,
        cppLanguage,
        oneDark,
        EditorState.readOnly.of(true)
      ],
      parent: exampleCodeEditorVariables.value!
    });

    exampleCodeEditorVariableAttributionView = new EditorView({
      extensions: [
        basicSetup,
        cppLanguage,
        oneDark,
        EditorState.readOnly.of(true)
      ],
      parent: exampleCodeEditorVariableAttribution.value!
    });

    exampleCodeEditorVariablePrintView = new EditorView({
      extensions: [
        basicSetup,
        cppLanguage,
        oneDark,
        EditorState.readOnly.of(true)
      ],
      parent: exampleCodeEditorVariablePrint.value!
    });

    exampleCodeEditorVariableScanfView = new EditorView({
      extensions: [
        basicSetup,
        cppLanguage,
        oneDark,
        EditorState.readOnly.of(true)
      ],
      parent: exampleCodeEditorVariableScanf.value!
    });

    exampleCodeEditorVariableFunctionDeclarationView = new EditorView({
      extensions: [
        basicSetup,
        cppLanguage,
        oneDark,
        EditorState.readOnly.of(true)
      ],
      parent: exampleCodeEditorVariableFunctionDeclaration.value!
    });

    exampleCodeEditorVariableFunctionCallView = new EditorView({
      extensions: [
        basicSetup,
        cppLanguage,
        oneDark,
        EditorState.readOnly.of(true)
      ],
      parent: exampleCodeEditorVariableFunctionCall.value!
    });

    exampleCodeEditorVariableIfView = new EditorView({
      extensions: [
        basicSetup,
        cppLanguage,
        oneDark,
        EditorState.readOnly.of(true)
      ],
      parent: exampleCodeEditorVariableIf.value!
    });

    exampleCodeEditorVariableWhileView = new EditorView({
      extensions: [
        basicSetup,
        cppLanguage,
        oneDark,
        EditorState.readOnly.of(true)
      ],
      parent: exampleCodeEditorVariableWhile.value!
    });

    exampleCodeEditorVariableForView = new EditorView({
      extensions: [
        basicSetup,
        cppLanguage,
        oneDark,
        EditorState.readOnly.of(true)
      ],
      parent: exampleCodeEditorVariableFor.value!
    });

    updateExamplesCode();
  });

  onBeforeUnmount(saveCodeToLocalStorage);
  window.onbeforeunload = saveCodeToLocalStorage;

  watch(locale, () => {
    updateExamplesCode();
  });

  function updateExamplesCode() {
    setContentInCodeEditor(exampleCodeEditorMainView, t('index.exampleMain.code'));
    setContentInCodeEditor(exampleCodeEditorVariablesView, t('index.exampleVariables.code'));
    setContentInCodeEditor(exampleCodeEditorVariableAttributionView, t('index.exampleAttribuition.code'));
    setContentInCodeEditor(exampleCodeEditorVariablePrintView, t('index.exampleVariablePrint.code'));
    setContentInCodeEditor(exampleCodeEditorVariableScanfView, t('index.exampleVariableScan.code'));
    setContentInCodeEditor(exampleCodeEditorVariableFunctionDeclarationView, t('index.exampleFunctionDeclaration.code'));
    setContentInCodeEditor(exampleCodeEditorVariableFunctionCallView, t('index.exampleFunctionCall.code'));
    setContentInCodeEditor(exampleCodeEditorVariableIfView, t('index.exampleIf.code'));
    setContentInCodeEditor(exampleCodeEditorVariableWhileView, t('index.exampleWhile.code'));
    setContentInCodeEditor(exampleCodeEditorVariableForView, t('index.exampleFor.code'));
  }

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

      if (!!error?.response?.data?.message) {
        showErrorMessage(`Falha ao fazer o upload do código: ${error.response.data.message}`);
      }
      else {
        showErrorMessage(`Falha ao fazer o upload do código: ${error.message}`);
      }

    }

    isLoading.value = false;
  }

  function updateCodeEditorStateCode(newCode: string) {
    setContentInCodeEditor(mainCodeEditor, newCode);
  }
</script>
