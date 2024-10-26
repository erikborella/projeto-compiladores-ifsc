<template>
  <v-expansion-panel>
    <v-expansion-panel-title>
      <h2>
        {{ `Escopo: ${props.id}` }}
        <v-btn 
          flat 
          density="compact" 
          icon="mdi-magnify"
          @click.native.stop="selectPositionInReferenceCodeEditor(props.editorView, props.scope.position)">
        </v-btn>
      </h2>
    </v-expansion-panel-title>

    <v-expansion-panel-text>

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
        <v-expansion-panels multiple>
          <ScopeCard 
            v-for="(internalScope, index) in props.scope.children"
            :key="index"
            :scope="internalScope"
            :id="`${props.id}-${index + 1}`"
            :editorView="props.editorView"></ScopeCard>
        </v-expansion-panels>
      </div>

    </v-expansion-panel-text>
  </v-expansion-panel>
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