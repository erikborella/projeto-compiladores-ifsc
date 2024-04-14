<template>
  <v-layout>

    <v-app-bar color="indigo">

      <template v-slot:prepend>
        <v-app-bar-nav-icon @click="drawer = !drawer"></v-app-bar-nav-icon>
      </template>

      <v-app-bar-title>Projeto Compilador</v-app-bar-title>

      <template v-slot:append>
        <v-btn text="Compilar" append-icon="mdi-send"></v-btn>
        <v-btn size="x-large" :icon="theme.global.current.value.dark ? 'mdi-white-balance-sunny' : 'mdi-weather-night'" @click="toggleTheme"></v-btn>
        <v-btn size="x-large" href="https://github.com/erikborella/projeto-compiladores-ifsc" target="_blank" icon="mdi-github"></v-btn>
      </template>

    </v-app-bar>

    <v-navigation-drawer v-model="drawer" width="700">
      <v-container class="d-flex flex-column ga-2">
        <v-card text="Lorem ipsum dolor sit amet consectetur adipisicing elit. Commodi, ratione debitis quis est labore voluptatibus! Eaque cupiditate minima, at placeat totam, magni doloremque veniam neque porro libero rerum unde voluptatem!"></v-card>
        <v-card text="Lorem ipsum dolor sit amet consectetur adipisicing elit. Commodi, ratione debitis quis est labore voluptatibus! Eaque cupiditate minima, at placeat totam, magni doloremque veniam neque porro libero rerum unde voluptatem!"></v-card>
        <v-card text="Lorem ipsum dolor sit amet consectetur adipisicing elit. Commodi, ratione debitis quis est labore voluptatibus! Eaque cupiditate minima, at placeat totam, magni doloremque veniam neque porro libero rerum unde voluptatem!"></v-card>
        <v-card text="Lorem ipsum dolor sit amet consectetur adipisicing elit. Commodi, ratione debitis quis est labore voluptatibus! Eaque cupiditate minima, at placeat totam, magni doloremque veniam neque porro libero rerum unde voluptatem!"></v-card>
        <v-card text="Lorem ipsum dolor sit amet consectetur adipisicing elit. Commodi, ratione debitis quis est labore voluptatibus! Eaque cupiditate minima, at placeat totam, magni doloremque veniam neque porro libero rerum unde voluptatem!"></v-card>
      </v-container>
    </v-navigation-drawer>

    <v-main>
      <v-container class="fill-height">
        <v-row class="fill-height">
          <v-col class="fill-height">
            <div ref="codeEditorElement" class="fill-height"></div>
          </v-col>
        </v-row>
      </v-container>
    </v-main>

  </v-layout>
</template>

<script lang="ts" setup>
  import { onMounted, ref } from 'vue';
  import * as monaco from "monaco-editor";
  import { useTheme } from 'vuetify';

  const drawer = ref(true);
  const codeEditorElement = ref(null);
  const theme = useTheme();

  let codeEditor: monaco.editor.IStandaloneCodeEditor;

  onMounted(() => {
    const codeEditorHTMLElement = codeEditorElement.value! as HTMLElement;
    codeEditor = monaco.editor.create(codeEditorHTMLElement, {
      theme: 'vs-dark',
      language: 'c',
      automaticLayout: true,
    });

    codeEditor.setValue("main() {\n\tprintf(\"Hello, World!\");\n}");
  });

  function toggleTheme() {
    theme.global.name.value = theme.global.current.value.dark ? 'light' : 'dark';
  }

</script>
