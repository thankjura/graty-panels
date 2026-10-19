<script setup lang="ts">
import { $i18n } from "@/utils/i18n.ts";
import { onMounted, ref, useTemplateRef } from "vue";
import type { ComponentExposed } from "vue-component-type-helpers";
import '@/assets/general.scss';
import {
  ButtonBusy, type SearchQuery
} from "luna";
import type { PanelRenderValueDto } from "@/interfaces/render.ts";
import DeletePanelRenderDialog from "@/components/dialog/DeletePanelRenderDialog.vue";
import PanelRenderDialog from "@/components/dialog/PanelRenderDialog.vue";
import { adminRenderService } from "@/service/adminRenderService.ts";

const busy = ref(false);
const query = ref<SearchQuery>({page: 1, limit: 20});
const renders = ref<Array<PanelRenderValueDto>>([]);
const total = ref(0);
const renderDialog = useTemplateRef<ComponentExposed<typeof PanelRenderDialog>>('renderDialog');
const deleteRenderDialog = useTemplateRef<ComponentExposed<typeof DeletePanelRenderDialog>>('deleteRenderDialog');

const search = (page?: number) => {
  const nextPage = page?? query.value.page;
  busy.value = true;
  adminRenderService.search({...query.value, page: nextPage}).then((data) => {
    renders.value = data.data.results;
    total.value = data.data.total;
    query.value.page = data.data.page;
    query.value.limit = data.data.limit;
  }).finally(() => {
    busy.value = false;
  });
}

const resetSearch = () => {
  query.value = {
    page: 1,
    limit: 20,
  };

  search(1);
}

const showCreateRenderDialog = () => {
  renderDialog.value.create();
}

const showEditRenderDialog = (panel: PanelRenderValueDto) => {
  renderDialog.value.edit(panel);
}

const showDeleteRenderDialog = (panel: PanelRenderValueDto) => {
  deleteRenderDialog.value.show(panel);
}

onMounted(() => {
  search(1);
});

</script>

<template>
  <div class="graty-page">
    <nav class="breadcrumbs pad-x pad-top">
      <ol role="list">
        <li>
          {{ $i18n.t("Graty") }}
        </li>
        <li>
          {{ $i18n.t("Panel renders") }}
        </li>
      </ol>
      <div class="actions">
        <button type="button" @click="showCreateRenderDialog()">{{ $i18n.t("Create panel render") }}</button>
      </div>
    </nav>

    <div class="pad">
      <form class="panel pad horizontal" @submit.prevent="search(1)">
        <div class="field-group">
          <label for="term">{{ $i18n.t("Filter") }}</label>
          <input id="term" type="text" v-model="query.term">
        </div>
        <div class="form-footer">
          <ButtonBusy :busy="busy" :disabled="busy" @click="search(1)">{{ $i18n.t("Search") }}</ButtonBusy>
          <button type="button" @click="resetSearch()" :disabled="busy" class="button-link">{{ $i18n.t("Reset") }}</button>
        </div>
      </form>
    </div>

    <div class="pad">
      <table class="hover">
        <thead>
          <tr>
            <th>{{ $i18n.t("Name/Description") }}</th>
            <th>{{ $i18n.t("Actions") }}</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="render in renders" :key="render.id">
            <td>
              {{ render.name }}
              <div class="description" v-if="render.description">
                {{ render.description }}
              </div>
            </td>
            <td>
              <button type="button" class="button-link" @click="showEditRenderDialog(render)">{{ $i18n.t("Edit") }}</button>
              <button type="button" class="button-link" @click="showDeleteRenderDialog(render)">{{ $i18n.t("Delete") }}</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <PanelRenderDialog ref="renderDialog" @created="search(1)" @updated="search(1)"></PanelRenderDialog>
    <DeletePanelRenderDialog ref="deleteRenderDialog" @deleted="search(1)"></DeletePanelRenderDialog>
  </div>
</template>

<style scoped>

</style>
