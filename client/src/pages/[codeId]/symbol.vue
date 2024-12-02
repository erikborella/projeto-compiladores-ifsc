<template>
  <v-layout class="fill-height">

    <v-navigation-drawer v-model="isConfigMenuOpen" width="600">
      <div ref="referenceCodeEditor" class="code-editor highlight-selection"></div>
    </v-navigation-drawer>

    <v-main>
      <div class="listContainer">
        <v-container>
          <v-row>
            <v-col cols="12" xl="4" xxl="4">
              <v-expansion-panels multiple>
                <v-expansion-panel elevation="4">

                  <v-expansion-panel-title>
                    <h2>Funções</h2>
                  </v-expansion-panel-title>

                  <v-expansion-panel-text>
                    <v-expansion-panels multiple>
                      <v-expansion-panel v-for="(declaredFunction, index) in symbolsTable?.functions" :key="index">
                        <v-divider></v-divider>
                        <v-expansion-panel-title>
                          <h2>
                            {{ declaredFunction.name }}
                            <v-btn flat density="compact" icon="mdi-magnify"
                            @click.native.stop="selectPositionInReferenceCodeEditor(referenceCodeEditorView, declaredFunction.position)"></v-btn>
                          </h2>
                        </v-expansion-panel-title>
 
                        <v-expansion-panel-text>
                          Tipo de retorno: <b>{{ declaredFunction.returnType }}</b>

                          <div v-if="declaredFunction.parameters != null">
                            Parâmetros:
                            <v-table class="elevation-1">
                              <thead>
                                <tr>
                                  <th class="text-left">
                                    Tipo
                                  </th>
                                  <th class="text-left">
                                    Nome
                                  </th>
                                </tr>
                              </thead>
                              <tbody>
                                <tr v-for="(parameter, index) in declaredFunction.parameters" :key="index">
                                  <td>{{ parameter.type }}</td>
                                  <td>{{ parameter.name }}</td>
                                </tr>
                              </tbody>
                            </v-table>
                          </div>
                        </v-expansion-panel-text>
                      </v-expansion-panel>
                    </v-expansion-panels>
                  </v-expansion-panel-text>
                </v-expansion-panel>
              </v-expansion-panels>
            </v-col>

            <v-col cols="12" xl="4" xxl="4">
              <v-expansion-panels multiple>
                <v-expansion-panel elevation="4">

                  <v-expansion-panel-title>
                    <h2>Escopos</h2>
                  </v-expansion-panel-title>

                  <v-expansion-panel-text>
                    <v-divider></v-divider>

                    <v-expansion-panels multiple>
                      <ScopeCard v-for="(scope, index) in symbolsTable?.scopes" :key="index" :scope="scope"
                        :id="(index + 1).toString()" :editorView="referenceCodeEditorView">
                      </ScopeCard>
                    </v-expansion-panels>
                  </v-expansion-panel-text>

                </v-expansion-panel>
              </v-expansion-panels>

            </v-col>

            <v-col cols="12" xl="4" xxl="4">
              <v-expansion-panels multiple>
                <v-expansion-panel elevation="4">
                  <v-expansion-panel-title>
                    <h2>Strings</h2>
                  </v-expansion-panel-title>

                  <v-expansion-panel-text >
                    <v-divider></v-divider>

                    <v-container>
                      <ul>
                        <li v-for="(declaredString, index) in symbolsTable?.strings" :key="index">
                          <p class="text-body-1 mb-2 mt-2">{{ declaredString }}</p>
                          <v-divider></v-divider>
                        </li>    
                      </ul>
                    </v-container>
                  </v-expansion-panel-text>
                </v-expansion-panel>
              </v-expansion-panels>

            </v-col>
          </v-row>
        </v-container>
      </div>
    </v-main>

    <v-overlay v-model="isLoading" :opacity="0.8" persistent class="align-center justify-center">
      <v-progress-circular indeterminate color="primary" width="10" size="100">
      </v-progress-circular>
    </v-overlay>

    <v-snackbar variant="flat" color="error" v-model="snackbar.show" :timeout="5000" top>
      {{ snackbar.message }}
      <template v-slot:actions>
        <v-btn color="white" variant="text" @click="snackbar.show = false">Fechar</v-btn>
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
    overflow: auto;
  }
</style>

<script lang="ts" setup>
  import { defineModel, onMounted, ref, useTemplateRef } from 'vue';
  import { useRoute } from 'vue-router';
  import { SymbolsTable } from '../../models/SymbolsTable';
  import compilerApi from '../../services/compiler/compilerApi';
  import { syntaxViewSetup } from '../../models/CodemirrorCustomSetups';
  import { EditorView } from 'codemirror';
  import { EditorState } from '@codemirror/state';
  import { cppLanguage } from '@codemirror/lang-cpp';
  import { oneDark } from '@codemirror/theme-one-dark';
  import { selectPositionInReferenceCodeEditor } from '../../services/editorViewTools';
  import ScopeCard from '../../components/ScopeCard.vue';

  const isConfigMenuOpen = defineModel<boolean>('isConfigMenuOpen');

  const route = useRoute();

  const isLoading = ref(false);

  const referenceCode = ref<string>();
  const referenceCodeEditor = useTemplateRef('referenceCodeEditor');
  let referenceCodeEditorView: EditorView;

  const symbolsTable = ref<SymbolsTable>();

  const snackbar = ref({
    show: false,
    message: '',
  });

  onMounted(async () => {
    isLoading.value = true;
    const codeId = route.params.codeId as string;

    await downloadAndShowReferenceCode(codeId);
    await downloadAndShowSymbolsTable(codeId);

    isLoading.value = false;
  })

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

  async function downloadAndShowSymbolsTable(codeId: string) {
    try {
      symbolsTable.value = await compilerApi.getSymbolsTable(codeId);
    } catch (error) {
      console.error(error);
      showErrorMessage(`Falha ao fazer o download da table de símbolos: ${error.message}`);
    }
  }

  function showErrorMessage(message: string) {
    snackbar.value.message = message;
    snackbar.value.show = true;
  }

</script>