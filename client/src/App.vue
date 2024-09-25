<template>
  <v-app>
    <router-view />
  </v-app>
</template>

<script lang="ts" setup>
  //
</script>

<style>
  .cm-mergeView {
    height: 100%;
    width: 100%;
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
</style>