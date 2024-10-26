<template>
  <v-container>
    <v-card>

      <v-card-title class="align-center">
        {{ `Escopo: ${props.id}` }}
        <v-btn 
          flat 
          density="compact" 
          icon="mdi-magnify"
          @click="selectPositionInReferenceCodeEditor(props.editorView, props.scope.position)"></v-btn>
      </v-card-title>

      <v-card-text>

        <div v-if="props.scope.scopeVariables.length > 0">
          Variáveis:
          <br><br>
          <v-table class="elevation-1">
            <thead>
              <tr>
                <th class="text-left">
                  Tipo
                </th>
                <th class="text-left">
                  Nome
                </th>
              </tr>
            </thead>
            <tbody>
              <tr 
                v-for="(variable, index) in props.scope.scopeVariables" 
                :key="index">
                <td>{{ variable.type }}</td>
                <td>{{ variable.name }}</td>
              </tr>
            </tbody>
          </v-table>
          <v-divider></v-divider>
        </div>
        <div v-else>
          Esse escopo não tem novas declarações de variáveis
        </div>

        <div v-if="props.scope.children.length > 0">
          <br>
          Escopos internos:
          <ScopeCard 
            v-for="(internalScope, index) in props.scope.children"
            :key="index"
            :scope="internalScope"
            :id="`${props.id}-${index + 1}`"
            :editorView="props.editorView"></ScopeCard>
        </div>

      </v-card-text>
    </v-card>
  </v-container>
</template>

<style scoped>
  .hover-box {
    background-color: white;
    transition: background-color 0.3s ease;
  }

  .hover-box:hover {
    background-color: #e0e0e0;
  }
</style>

<script lang="ts" setup>
  import { EditorView } from 'codemirror';
  import { Scope } from '../models/SymbolsTable';
  import { selectPositionInReferenceCodeEditor } from '../services/editorViewTools';

  const props = defineProps<{
    id: string,
    scope: Scope,
    editorView: EditorView
  }>();
</script>