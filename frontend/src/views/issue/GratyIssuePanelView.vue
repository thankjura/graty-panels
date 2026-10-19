<script setup lang="ts">
import { computed, inject, onMounted, ref, useTemplateRef } from "vue";
import { Injections } from "luna";
import { issuePanelService } from "@/service/issuePanelService.ts";
import type { IssuePanelDto } from "@/interfaces/issue.ts";
import PanelComponent from "@/components/issue/PanelComponent.vue";

const issueKey = inject(Injections.issueKey);
const panels = ref<Array<IssuePanelDto>>([]);
const selectedPanelId = ref(0);
const panelRef = useTemplateRef<HTMLDivElement>('panel');

const currentPanel = computed(() => {
  return panels.value[selectedPanelId.value];
});

const setCurrentPanel = (idx: number) => {
  selectedPanelId.value = idx;
}

onMounted(() => {
  issuePanelService.getPanelsForIssue(issueKey.value).then((data) => {
    panels.value = data.data;
    if (panels.value.length == 1) {
      panelRef.value.closest(".toggle-panel").querySelector(".toggle-panel-title").textContent = data.data[0].name;
    }
  })
});

</script>

<template>
  <div class="graty-issue-panel" ref="panel">
    <template v-if="panels.length > 1">
      <ul class="tabs">
        <li class="tab-item" v-for="(panel, idx) in panels" :key="panel.id" @click="setCurrentPanel(idx)">{{ panel.name }}</li>
      </ul>
    </template>

    <div class="panel-body">
      <PanelComponent :issue-key="issueKey" :panel="currentPanel" v-if="currentPanel" />
    </div>
  </div>
</template>

<style>

</style>