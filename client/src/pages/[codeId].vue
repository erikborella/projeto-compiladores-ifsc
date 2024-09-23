<template>
  <v-layout>

    <v-app-bar color="indigo">
      <v-app-bar-title>Projeto Compilador</v-app-bar-title>

      <template v-slot:append>
        <v-btn size="x-large" :icon="theme.global.current.value.dark ? 'mdi-white-balance-sunny' : 'mdi-weather-night'"
          @click="toggleTheme"></v-btn>
        <v-btn size="x-large" href="https://github.com/erikborella/projeto-compiladores-ifsc" target="_blank"
          icon="mdi-github"></v-btn>
      </template>

      <template v-slot:extension>
        <v-tabs grow>
          <v-tab :to="{ name: '/[codeId]/llvm' }">LLVM</v-tab>
          <v-tab :to="{ name: '/[codeId]/asm' }">Assembly</v-tab>
        </v-tabs>
      </template>
    </v-app-bar>


    <v-main>
      <router-view />
    </v-main>

  </v-layout>
</template>

<script lang="ts" setup>
  import { onMounted } from 'vue';
  import { ref } from 'vue';
  import { useRoute, useRouter } from 'vue-router';
  import { useTheme } from 'vuetify';

  const theme = useTheme();
  const route = useRoute();
  const router = useRouter();

  onMounted(() => {
    const currentRoute = route.name;

    if (currentRoute === '/[codeId]') {
      router.push({ name: '/[codeId]/llvm' });
    }
  })

  function toggleTheme() {
    theme.global.name.value = theme.global.current.value.dark ? 'light' : 'dark';
  }

</script>