<template>
  <v-expansion-panel>
    <v-expansion-panel-title>
      <h2>
        {{ `${t('symbolsTable.scope')}: ${props.id}` }}
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
        {{ t('symbolsTable.variables') }}:
        <br><br>
        <v-table class="elevation-1">
          <thead>
            <tr>
              <th class="text-left">
                {{ t('symbolsTable.type') }}
              </th>
              <th class="text-left">
                {{ t('symbolsTable.name') }}
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
        {{ t('symbolsTable.scopeDoesNotHaveNewVariables') }}
      </div>

      <div v-if="props.scope.children.length > 0">
        <br>
        {{ t('symbolsTable.insideScopes') }}:
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
  import { useTranslate } from '../locales';

  const { translate: t } = useTranslate();

  const props = defineProps<{
    id: string,
    scope: Scope,
    editorView: EditorView
  }>();
</script>
