<template>
  <v-layout class="fill-height">

    <v-navigation-drawer v-model="isConfigMenuOpen" width="600">
      <div ref="referenceCodeEditor" class="code-editor"></div>
    </v-navigation-drawer>

    <v-main>
      <div class="treeContainer">
        <div ref="containerD3"></div>
      </div>
    </v-main>

    <v-overlay v-model="isLoading" :opacity="0.8" persistent class="align-center justify-center">
      <v-progress-circular indeterminate color="primary" width="10" size="100">
      </v-progress-circular>
    </v-overlay>

    <v-snackbar variant="flat" color="error" v-model="snackbar.show" :timeout="5000" top>
      {{ snackbar.message }}
      <template v-slot:actions>
        <v-btn
          color="white"
          variant="text"
          @click="snackbar.show = false">Fechar</v-btn>
      </template>
    </v-snackbar>

  </v-layout>
</template>

<style scoped>
  .treeContainer {
    width: 100%;
    height: 100%;
    overflow: auto;
    background-color: white
  }

  .code-editor {
    flex-grow: 1;
    height: 100%;
    overflow: auto;
  }
</style>

<script lang="ts" setup>
  import { defineModel, onMounted, ref, useTemplateRef } from 'vue';
  import { useRoute } from 'vue-router';
  import * as d3 from 'd3';
  import compilerApi from '../../services/compiler/compilerApi';
  import { basicSetup, EditorView } from 'codemirror';
  import { EditorState, Text, EditorSelection } from '@codemirror/state';
  import { cppLanguage } from '@codemirror/lang-cpp';
  import { oneDark } from '@codemirror/theme-one-dark';
  import { Position, TokenPosition } from '../../models/SyntaxTreeRepresentation';
  import { syntaxViewSetup } from '../../models/CodemirrorCustomSetups';

  const isConfigMenuOpen = defineModel<boolean>('isConfigMenuOpen');

  const route = useRoute();

  const isLoading = ref(false);

  const syntax = ref<any>(null);

  const referenceCode = ref<string>();
  let referenceCodeEditorView: EditorView;

  const containerD3 = useTemplateRef('containerD3');

  const snackbar = ref({
    show: false,
    message: ''
  });

  const referenceCodeEditor = useTemplateRef('referenceCodeEditor');

  onMounted(async () => {
    isLoading.value = true;
    const codeId = route.params.codeId as string;

    await downloadAndShowReferenceCode(codeId);
    await downloadAndShowTree(codeId);

    isLoading.value = false;
  });

  async function downloadAndShowTree(codeId: string) {
    try {
      syntax.value = await compilerApi.getSyntaxRepresentation(codeId);
      showTree();
    } catch (error) {
      console.error(error);
      showErrorMessage(`Falha ao fazer o da arvore sintática: ${error.message}`);
    }
  }

  function showTree() {
    const family = d3.hierarchy(syntax.value);

    const treeLayout = d3.tree();
    const treeData = treeLayout(family);

    const nodeCount = treeData.descendants().length;
    const rootHeight = nodeCount * 15;
    const rootWidth = treeData.height * 150;

    const svg = d3.select(containerD3.value)
      .append('svg')
      .attr('width', rootWidth)
      .attr('height', rootHeight)
      .append('g')
      .attr('transform', 'translate(40, 40)');

    treeLayout.size([rootHeight - 100, rootWidth - 100]);

    const treeNodes = treeLayout(family);

    svg.selectAll('.link')
      .data(treeNodes.links())
      .enter()
      .append('path')
      .attr('class', 'link')
      .attr('d', d3.linkHorizontal()
        .x((d: any) => d.y)
        .y((d: any) => d.x) as any)
      .style('fill', 'none')
      .style('stroke', '#ccc')
      .style('stroke-width', '2px');

    const node = svg.selectAll('.node')
      .data(treeNodes.descendants())
      .enter()
      .append('g')
      .attr('class', 'node')
      .attr('transform', d => `translate(${d.y}, ${d.x})`)
      .on('mouseover', (event, d: any) => { selectTokenInReferenceCodeEditor(d.data.position) });

    node.append('circle')
      .attr('r', 5)
      .style('fill', (d: any) => {
        if (d.data.type === 'leaf')
          return '#ff6347';

        return '#69b3a2';
      });

    node.append('text')
      .attr('dy', -10)
      .attr('text-anchor', 'middle')
      .text((d: any) => d.data.label);

    node.filter((d: any) => d.data.type === 'leaf')
      .append('text')
      .attr('dy', 20)
      .attr('text-anchor', 'middle')
      .style('font-size', '15px')
      .text((d: any) => d.data.value);
  }

  async function downloadAndShowReferenceCode(codeId: string) {
    try {
      referenceCode.value = await compilerApi.getCode(codeId);

      referenceCodeEditorView = new EditorView({
        doc: referenceCode.value,
        extensions: [
          syntaxViewSetup,
          cppLanguage,
          oneDark,
          EditorState.readOnly.of(true),
          EditorView.editable.of(false),
        ],
        parent: referenceCodeEditor.value!
      });

    } catch (error) {
      console.error(error);
      showErrorMessage(`Falha ao fazer o download do código: ${error.message}`);
    }
  }

  function showErrorMessage(message: string) {
    snackbar.value.message = message;
    snackbar.value.show = true;
  }

  function posToOffset(doc: Text, position: Position): number {
    return doc.line(position.line).from + position.column - 1;
  }

  function selectTokenInReferenceCodeEditor(tokenPosition: TokenPosition) {
    const { start, end } = tokenPosition;

    const startOffset = posToOffset(referenceCodeEditorView.state.doc, start);
    const endOffset = posToOffset(referenceCodeEditorView.state.doc, end);

    const editorSelection = EditorSelection.single(startOffset, endOffset);

    referenceCodeEditorView.dispatch({
      selection: editorSelection,
      scrollIntoView: true,
    });
  }
</script>