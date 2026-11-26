<script setup lang="ts">
import { computed, inject, onMounted, ref, useTemplateRef, watch } from "vue";
import { BusyIconComponent, Injections } from "luna";
import { issuePanelService } from "@/service/issuePanelService.ts";
import type { IssuePanelDto } from "@/interfaces/issue.ts";
import PanelComponent from "@/components/issue/PanelComponent.vue";

const issueKey = inject(Injections.issueKey);
const panels = ref<Array<IssuePanelDto>>([]);
const selectedPanelId = ref(0);
const loading = ref(false);
const panelRef = useTemplateRef<HTMLDivElement>('panel');

const currentPanel = computed(() => {
  return panels.value[selectedPanelId.value];
});

const setCurrentPanel = (idx: number) => {
  selectedPanelId.value = idx;
};

const loadPanels = () => {
  loading.value = true;
  selectedPanelId.value = 0;
  issuePanelService.getPanelsForIssue(issueKey.value).then((data) => {
    panels.value = data.data;
    if (panels.value.length === 1) {
      const titleEl = panelRef.value?.closest(".toggle-panel")?.querySelector(".toggle-panel-title");
      if (titleEl) {
        titleEl.textContent = data.data[0].name;
      }
    }
  }).finally(() => {
    loading.value = false;
  });
};

watch(issueKey, () => {
  panels.value = [];
  loadPanels();
});

onMounted(() => {
  loadPanels();
});

</script>

<template>
  <div class="graty-issue-panel" ref="panel">
    <div class="graty-issue-panel-loading" v-if="loading && panels.length === 0">
      <BusyIconComponent />
    </div>
    <template v-else>
      <template v-if="panels.length > 1">
        <ul class="tabs">
          <li
            class="tab-item"
            :class="{ active: idx === selectedPanelId }"
            v-for="(panel, idx) in panels"
            :key="panel.id"
            @click="setCurrentPanel(idx)"
          >
            {{ panel.name }}
          </li>
        </ul>
      </template>

      <div class="panel-body">
        <PanelComponent :issue-key="issueKey" :panel="currentPanel" v-if="currentPanel" />
      </div>
    </template>
  </div>
</template>

<style lang="scss">
.graty-issue-panel {
  .graty-issue-panel-loading {
    display: flex;
    justify-content: center;
    padding: 12px;
  }
}
</style>
