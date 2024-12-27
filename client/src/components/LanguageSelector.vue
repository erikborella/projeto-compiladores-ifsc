<template>
  <v-menu>

    <template v-slot:activator="{ props }">
      <v-btn icon="mdi-translate" v-bind="props"></v-btn>
    </template>

    <v-list>

      <v-list-item
        v-for="(lang, index) in languages"
        :key="index"
        :value="index"
        @click="switchLanguage(lang.code)"
      >
        <v-list-item-title>{{ lang.name }}</v-list-item-title>
      </v-list-item>

    </v-list>

  </v-menu>
</template>

<script lang="ts" setup>
  import { useI18n } from 'vue-i18n';
  import { languages } from '../locales';

  const { locale } = useI18n();

  function switchLanguage(lang: string) {
     locale.value = lang;
  }
</script>
