<template>
    <v-layout>

        <v-app-bar color="indigo">
            <v-app-bar-title>Projeto Compilador</v-app-bar-title>

            <template v-slot:append>
                <!-- <v-btn size="x-large" :icon="theme.global.current.value.dark ? 'mdi-white-balance-sunny' : 'mdi-weather-night'" @click="toggleTheme"></v-btn> -->
                <v-btn size="x-large" href="https://github.com/erikborella/projeto-compiladores-ifsc" target="_blank" icon="mdi-github"></v-btn>
            </template>
        </v-app-bar>

        <v-main>
            <v-container class="fill-height">
                <v-row class="fill-height">
                    <v-col class="fill-height">
                        <div ref="codeEditorElement" class="fill-height"></div>
                    </v-col>
                    <v-col class="fill-height">
                        <h1>tesdjahdkhaskda</h1>
                    </v-col>
                </v-row>
            </v-container>
        </v-main>

    </v-layout>
</template>

<script lang="ts" setup>
    import { onMounted } from 'vue';
    import { useRoute } from 'vue-router';
    import { ref } from 'vue';

    // import * as monaco from 'monaco-editor';

    const route = useRoute();
    const codeEditorElement = ref(null);

    // let codeEditor: monaco.editor.IStandaloneCodeEditor;

    // onMounted(() => {
    //     const codeEditorHTMLElement = codeEditorElement.value! as HTMLElement;
    //     codeEditor = monaco.editor.create(codeEditorHTMLElement, {
    //         theme: 'vs-dark',
    //         language: 'c',
    //         automaticLayout: true,
    //     });

    //     codeEditor.setValue(route.params.codeId as string);
    // })
</script>