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

  .cm-selectionLayer .cm-selectionBackground {
    border: 1px solid white;
  }

  .remove-details > .v-input__details {
    display: none;
  }

  html, body, #app {
    height: 100%;
    margin: 0;
    overflow: hidden;
  }
</style>