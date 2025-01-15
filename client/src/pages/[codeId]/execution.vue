<template>
  <v-layout class="fill-height">

    <v-navigation-drawer v-model="isConfigMenuOpen" width="350">
      <v-container class="d-flex flex-column ga-2">
        <v-card
          elevation="4"
          :title="t('execution.title')"
          :text="t('execution.description')"
        >

          <v-divider></v-divider>

          <v-container>
            <v-btn @click="restartProgram()" block :text="t('execution.restart')" append-icon="mdi-restart"></v-btn>
          </v-container>

          <v-container>
            <v-btn @click="cleanOutput()" block :text="t('execution.clear')" append-icon="mdi-broom"></v-btn>
          </v-container>

        </v-card>
      </v-container>
    </v-navigation-drawer>

    <v-main class="main-area">
      <textarea
        class="input-textarea main-area-background-color"
        :placeholder="t('execution.output')"
        ref="outputTextArea"
        readonly
      >{{ outputText }}</textarea>

      <v-container fluid>
        <v-text-field
          v-model="inputText"
          :label="t('execution.input')"
          variant="outlined"
          class="remove-details"
          ref="inputTextField"
          @keyup.enter="sendInput()"
          :disabled="webSocket === null"
        >
          <template v-slot:append>
            <v-btn @click="sendInput()" color="primary" :text="t('execution.send')" append-icon="mdi-send" :disabled="webSocket === null">
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
  import { useTranslate } from '../../locales';

  const { translate: t } = useTranslate();

  const route = useRoute();

  const isConfigMenuOpen = defineModel<boolean>('isConfigMenuOpen');

  const outputText = ref('');
  const inputText = ref('');

  const outputTextArea = useTemplateRef('outputTextArea');
  const inputTextField = useTemplateRef('inputTextField');

  let webSocket = ref<WebSocket | null>(null);

  onBeforeUnmount(closeSocket);
  window.onbeforeunload = closeSocket;

  onMounted(() => {
    startProgram();
  });

  watch(outputText, () => {
    outputTextArea.value?.scrollTo({ top: outputTextArea.value?.scrollHeight});
  })

  function restartProgram() {
    webSocket.value?.close();
    startProgram();
  }

  function cleanOutput() {
    outputText.value = '';
  }

  function startProgram() {
    const codeId = route.params.codeId as string;

    outputText.value += '-----------------------------------\n\n';
    outputText.value += `-- ${t('execution.startingServerConnection')}\n`;

    const ws = compilerRunner.getRunnerWebSocket(codeId);

    ws.onopen = () => {
      outputText.value += `-- ${t('execution.serverConnectionEstablished')}\n\n`;
    }

    ws.onmessage = (event) => {
      const message = event.data;

      if (message.startsWith(':-:')) {
        handleTemplateMessage(message);
        return;
      }

      outputText.value += event.data + '\n';
    };

    ws.onclose = () => {
      outputText.value += `-- ${t('execution.serverConnectionClosed')}\n\n`;
      webSocket.value = null;
    }

    webSocket.value = ws;
    inputTextField.value?.focus();
  }

  function handleTemplateMessage(message: string) {
    const messageWithoutIndicator = message.split(':-:')[1] ?? '';
    const [ type, value ] = messageWithoutIndicator.split(':');

    if (type === 'codeFinished') {
      handleCodeFinished(value);
      return;
    }

    const translatedType = t(`execution.${type}`);
    const translatedValue = t(`execution.${value}`);

    outputText.value += `-- ${translatedType}: ${translatedValue}\n`;
  }

  function handleCodeFinished(exitCode: string) {
    outputText.value += `\n\n-- ${t('execution.codeFinished')}: ${exitCode}\n`;
  }

  function closeSocket() {
    webSocket.value?.close();
    webSocket.value = null;
  }

  function sendInput() {
    if (webSocket.value === null)
      return;

    webSocket.value.send(inputText.value);

    outputText.value += `${inputText.value}\n`;
    inputText.value = '';


    inputTextField.value?.focus();
  }
</script>
