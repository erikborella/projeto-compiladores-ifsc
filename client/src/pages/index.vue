<template>
  <v-layout>

    <v-app-bar color="indigo">

      <template v-slot:prepend>
        <v-app-bar-nav-icon @click="drawer = !drawer"></v-app-bar-nav-icon>
      </template>

      <v-app-bar-title>Compilador</v-app-bar-title>

      <template v-slot:append>
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
      <v-container>
        <h1>Código</h1>
        <div id="codeContainer" style="height: 300px; width: 300px;"></div>
      </v-container>
    </v-main>

  </v-layout>
</template>

<script lang="ts" setup>
  import { ref } from 'vue';
  import * as monaco from "monaco-editor";

  const drawer = ref(true);

  const editorElement = document.getElementById("codeContainer") as HTMLElement;

  console.log(editorElement);
  
</script>
