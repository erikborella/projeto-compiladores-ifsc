<template>
    <v-container class="fill-height">
        <v-btn @click="increment()">teste: {{ counter }}</v-btn>

    </v-container>
</template>

<script lang="ts" setup>
    import { ref } from 'vue';

    const counter = ref(0);

    function increment() {
        counter.value++;
    }
</script>