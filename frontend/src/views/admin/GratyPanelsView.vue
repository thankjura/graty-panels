<script setup lang="ts">
import { $i18n } from "@/utils/i18n.ts";
import { computed, onMounted, ref, useTemplateRef } from "vue";
import type { PanelDto, PanelSearchRequest, PanelValueDto } from "@/interfaces/panel.ts";
import { adminPanelService } from "@/service/adminPanelService.ts";
import PanelDialog from "@/components/dialog/PanelDialog.vue";
import type { ComponentExposed } from "vue-component-type-helpers";
import '@/assets/general.scss';
import DeletePanelDialog from "@/components/dialog/DeletePanelDialog.vue";
import { issueTypeService,
  type IssueTypeValueDto,
  projectService,
  type ProjectValueDto,
  MultiSelect,
  ButtonBusy
} from "luna";

const busy = ref(false);
const query = ref<PanelSearchRequest>({page: 1, limit: 20});
const panels = ref<Array<PanelDto>>([]);
const total = ref(0);
const panelDialog = useTemplateRef<ComponentExposed<typeof PanelDialog>>('panelDialog');
const deletePanelDialog = useTemplateRef<ComponentExposed<typeof DeletePanelDialog>>('deletePanelDialog');
const projects = ref<Array<ProjectValueDto>>(null);
const issueTypes = ref<Array<IssueTypeValueDto>>(null);

const projectsMap = computed(() => {
  const out: Record<number, ProjectValueDto> = {};
  if (projects.value) {
    projects.value.forEach((item) => {
      out[item.id] = item;
    });
  }

  return out;
});


const issueTypesMap = computed(() => {
  const out: Record<number, IssueTypeValueDto> = {};
  if (issueTypes.value) {
    issueTypes.value.forEach((item) => {
      out[item.id] = item;
    });
  }

  return out;
});

const search = (page?: number) => {
  const nextPage = page?? query.value.page;
  busy.value = true;
  adminPanelService.search({...query.value, page: nextPage}).then((data) => {
    panels.value = data.data.results;
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

const showCreatePanelDialog = () => {
  panelDialog.value.create();
}

const showEditPanelDialog = (panel: PanelValueDto) => {
  panelDialog.value.edit(panel);
}

const showDeletePanelDialog = (panel: PanelValueDto) => {
  deletePanelDialog.value.show(panel);
}

onMounted(() => {
  projectService.getAll().then((data) => {
    projects.value = data.data;
  });
  issueTypeService.getAll().then((data) => {
    issueTypes.value = data.data;
  });

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
          {{ $i18n.t("Panels") }}
        </li>
      </ol>
      <div class="actions">
        <button type="button" @click="showCreatePanelDialog">{{ $i18n.t("Create panel") }}</button>
      </div>
    </nav>

    <div class="pad">
      <form class="panel pad horizontal" @submit.prevent="search(1)">
        <div class="field-group">
          <label for="term">{{ $i18n.t("Filter") }}</label>
          <input id="term" type="text" v-model="query.term">
        </div>

        <div class="field-group">
          <label for="project">{{ $i18n.t("Project") }}</label>
          <MultiSelect id="project" v-model="query.projectIds" :options="projects" :show-icons="true"></MultiSelect>
        </div>

        <div class="field-group">
          <label for="issue-type">{{ $i18n.t("Issue types") }}</label>
          <MultiSelect id="issue-type" v-model="query.issueTypeIds" :options="issueTypes" :show-icons="true"></MultiSelect>
        </div>

        <div class="form-footer">
          <ButtonBusy :busy="busy" :disabled="busy" @click="search(1)">{{ $i18n.t("Search") }}</ButtonBusy>
          <button type="button" @click="resetSearch()" :disabled="busy" class="button-link">{{ $i18n.t("Reset") }}</button>
        </div>
      </form>
    </div>

    <div class="pad">
      <table class="hover" v-if="projects && issueTypes">
        <thead>
          <tr>
            <th>{{ $i18n.t("Name/Display name") }}</th>
            <th>{{ $i18n.t("Projects") }}</th>
            <th>{{ $i18n.t("Issue types") }}</th>
            <th>{{ $i18n.t("Actions") }}</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="panel in panels" :key="panel.id" :class="{disabled: panel.disabled}">
            <td>
              {{ panel.name }}
              <div class="description">
                <template v-if="panel.displayName">{{ panel.displayName }}</template>
                <template v-else>{{ panel.name }}</template>
              </div>
            </td>
            <td>
              <ul class="item-list">
                <template v-for="pid in panel.projectIds">
                  <li class="iconed" v-if="projectsMap[pid]">
                    <img alt="" :src="projectsMap[pid].iconUrl" :title="projectsMap[pid].name" />
                    {{ projectsMap[pid].key }}
                  </li>
                </template>
              </ul>
            </td>
            <td>
              <ul class="item-list">
                <template v-for="tid in panel.issueTypeIds">
                  <li class="iconed" v-if="issueTypesMap[tid]">
                    <img alt="" :src="issueTypesMap[tid].iconUrl" />
                    {{ issueTypesMap[tid].name }}
                    <template v-if="issueTypesMap[tid].subtask">{{ $i18n.t("(Subtask)") }}</template>
                  </li>
                </template>
              </ul>
            </td>
            <td>
              <button type="button" class="button-link" @click="showEditPanelDialog(panel)">{{ $i18n.t("Edit") }}</button>
              <button type="button" class="button-link" @click="showDeletePanelDialog(panel)">{{ $i18n.t("Delete") }}</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <PanelDialog ref="panelDialog" @created="search(1)" @updated="search(1)" :projects="projects" :issue-types="issueTypes"></PanelDialog>
    <DeletePanelDialog ref="deletePanelDialog" @deleted="search(1)"></DeletePanelDialog>
  </div>
</template>

<style scoped>

</style>
