<template>
  <v-layout>

    <v-app-bar color="indigo">

      <template v-slot:prepend>
        <v-app-bar-nav-icon @click="isConfigMenuOpen = !isConfigMenuOpen"></v-app-bar-nav-icon>
      </template>
      
      <v-app-bar-title>Projeto Compilador</v-app-bar-title>
      
      <template v-slot:append>
        <v-btn size="x-large" icon="mdi-arrow-left" @click="backRoute()"></v-btn>
        <v-btn size="x-large" :icon="theme.global.current.value.dark ? 'mdi-white-balance-sunny' : 'mdi-weather-night'"
          @click="toggleTheme"></v-btn>
        <v-btn size="x-large" href="https://github.com/erikborella/projeto-compiladores-ifsc" target="_blank"
          icon="mdi-github"></v-btn>
      </template>

      <template v-slot:extension>
        <v-tabs align-tabs="center" grow>
          <v-tab :to="{ name: '/[codeId]/syntax' }">
            Arvore sintática
          </v-tab>
          <v-tab :to="{ name: '/[codeId]/llvm' }">
            LLVM IR
          </v-tab>
          <v-tab :to="{ name: '/[codeId]/asm' }">
            Assembly
          </v-tab>
          <v-tab :to="{ name: '/[codeId]/execution' }">
            Execução
          </v-tab>
          <v-tab>
            Complexidade de algoritmo
          </v-tab>
        </v-tabs>
      </template>
    </v-app-bar>


    <v-main>
      <router-view v-slot="{ Component }">
        <component 
          :is="Component"
          v-model:isConfigMenuOpen="isConfigMenuOpen"
        />
      </router-view>
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

  const isConfigMenuOpen = ref(true);

  onMounted(() => {
    const currentRoute = route.name;

    if (currentRoute === '/[codeId]') {
      router.replace({ name: '/[codeId]/syntax' });
    }
  })

  function backRoute() {
    router.push('/');
  }

  function toggleTheme() {
    theme.global.name.value = theme.global.current.value.dark ? 'light' : 'dark';
  }

</script>