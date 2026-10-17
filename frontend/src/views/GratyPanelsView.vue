<script setup lang="ts">
import { $i18n } from "@/utils/i18n.ts";
import { onMounted, ref, useTemplateRef } from "vue";
import type { PanelSearchRequest, PanelValueDto } from "@/interfaces/panel.ts";
import { panelService } from "@/service/panelService.ts";
import PanelDialog from "@/components/dialog/PanelDialog.vue";
import type { ComponentExposed } from "vue-component-type-helpers";
import '@/assets/general.scss';

const busy = ref(false);
const query = ref<PanelSearchRequest>({page: 1, limit: 20});
const panels = ref<Array<PanelValueDto>>([]);
const total = ref(0);
const panelDialog = useTemplateRef<ComponentExposed<typeof PanelDialog>>('panelDialog');

const search = (page?: number) => {
  const nextPage = page?? query.value.page;
  busy.value = true;
  panelService.search({...query.value, page: nextPage}).then((data) => {
    panels.value = data.data.results;
    total.value = data.data.total;
    query.value.page = data.data.page;
    query.value.limit = data.data.limit;
  }).finally(() => {
    busy.value = false;
  });
}

const showCreatePanelDialog = () => {
  panelDialog.value.create();
}

onMounted(() => {
  search(1);
});

</script>

<template>
  <div class="graty-page pad">
    <nav class="breadcrumbs">
      <ol role="list">
        <li>
          {{ $i18n.t("Graty") }}
        </li>
        <li>
          {{ $i18n.t("Panels") }}
        </li>
      </ol>
      <div class="actions">
        <button type="button" @click="showCreatePanelDialog">{{ $i18n.t("Create panel") }}</button>
      </div>
    </nav>


    <table class="hover">
      <thead>
        <tr>
          <th>{{ $i18n.t("Name") }}</th>
          <th>{{ $i18n.t("Display name") }}</th>
          <th>{{ $i18n.t("Actions") }}</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="panel in panels" :key="panel.id" :class="{disabled: panel.disabled}">
          <td>{{ panel.name }}</td>
          <td>{{ panel.displayName }}</td>
          <td>
            <button type="button" class="button-link">{{ $i18n.t("Edit") }}</button>
            <button type="button" class="button-link">{{ $i18n.t("Delete") }}</button>
          </td>
        </tr>
      </tbody>
    </table>
    <PanelDialog ref="panelDialog" @created="search(1)" @updated="search(1)"></PanelDialog>
  </div>
</template>

<style scoped>

</style>
