<template>
  <v-layout class="fill-height">

    <v-navigation-drawer v-model="isConfigMenuOpen" width="350">
      <v-container class="d-flex flex-column ga-2">
        <v-card 
          elevation="4"
          title="Execução"
          text="Execute o seu código de forma interativa, recebendo a saída do programa e enviando a entrada em tempo real."
        >

          <v-divider></v-divider>

          <v-container>
            <v-btn @click="restartProgram()" block text="reiniciar" append-icon="mdi-restart"></v-btn>
          </v-container>

        </v-card>
      </v-container>
    </v-navigation-drawer>

    <v-main class="main-area">
      <textarea
        class="input-textarea main-area-background-color"
        placeholder="Saída do programa"
        ref="outputTextArea"
        readonly
      >{{ outputText }}</textarea>

      <v-container fluid>
        <v-text-field 
          v-model="inputText"
          label="Dados de entrada"
          variant="outlined"
          class="remove-details"
          ref="inputTextField"
          @keyup.enter="sendInput()"
        >
          <template v-slot:append>
            <v-btn @click="sendInput()" color="primary" text="Enviar" append-icon="mdi-send">
            </v-btn>
          </template>
        </v-text-field>
      </v-container>

    </v-main>


  </v-layout>
</template>

<style scoped>
  .main-area {
    display: flex;
    flex-direction: column;
  }

  .main-area-background-color {
    background-color: #282c34;
  }

  .input-textarea {
    color: white;
    width: 100%;
    height: 100%;
    padding: 16px;
    resize: none;
    outline: none;
  }
</style>


<script lang="ts" setup>

  import { defineModel, onMounted, onBeforeUnmount, ref, useTemplateRef, watch } from 'vue';
  import { useRoute } from 'vue-router';
  import compilerRunner from '../../services/compiler/compilerRunner';

  const route = useRoute();

  const isConfigMenuOpen = defineModel<boolean>('isConfigMenuOpen');

  const outputText = ref('');
  const inputText = ref('');

  const outputTextArea = useTemplateRef('outputTextArea');
  const inputTextField = useTemplateRef('inputTextField');

  let webSocket: WebSocket | null = null;

  onBeforeUnmount(closeSocket);
  window.onbeforeunload = closeSocket;

  onMounted(() => {
    startProgram();
  });

  watch(outputText, () => {
    outputTextArea.value?.scrollTo({ top: outputTextArea.value?.scrollHeight});
  })

  function restartProgram() {
    webSocket?.close();
    startProgram();
  }

  function startProgram() {
    const codeId = route.params.codeId as string;

    outputText.value += '-- Iniciando conexão com o servidor\n'

    const ws = compilerRunner.getRunnerWebSocket(codeId);

    ws.onopen = () => {
      outputText.value += '-- Conexão com o servidor estabelecida\n\n'
    }
    
    ws.onmessage = (event) => {
      outputText.value += event.data + '\n';
    };

    ws.onclose = () => {
      outputText.value += '-- Conexão com o servidor encerrada\n\n';
    }

    webSocket = ws;
    inputTextField.value?.focus();
  }

  function closeSocket() {
    webSocket?.close();
  }

  function sendInput() {
    webSocket?.send(inputText.value);

    outputText.value += `${inputText.value}\n`;
    inputText.value = '';


    inputTextField.value?.focus();
  }
</script>