<script setup lang="ts">
import { $i18n } from "@/utils/i18n.ts";
import { onMounted, ref, useTemplateRef } from "vue";
import type { ComponentExposed } from "vue-component-type-helpers";
import '@/assets/general.scss';
import {
  ButtonBusy,
  PagePaginator,
  type SearchQuery
} from "luna";
import type { PanelRenderDto } from "@/interfaces/render.ts";
import DeletePanelRenderDialog from "@/components/dialog/DeletePanelRenderDialog.vue";
import PanelRenderDialog from "@/components/dialog/PanelRenderDialog.vue";
import { adminRenderService } from "@/service/adminRenderService.ts";

const busy = ref(false);
const query = ref<SearchQuery>({page: 1, limit: 20});
const renders = ref<Array<PanelRenderDto>>([]);
const total = ref(0);
const renderDialog = useTemplateRef<ComponentExposed<typeof PanelRenderDialog>>('renderDialog');
const deleteRenderDialog = useTemplateRef<ComponentExposed<typeof DeletePanelRenderDialog>>('deleteRenderDialog');

const search = (page?: number) => {
  const nextPage = page ?? query.value.page;
  busy.value = true;
  adminRenderService.search({...query.value, page: nextPage}).then((data) => {
    renders.value = data.data.results;
    total.value = data.data.total;
    query.value.page = data.data.page;
    query.value.limit = data.data.limit;
  }).finally(() => {
    busy.value = false;
  });
};

const resetSearch = () => {
  query.value = {
    page: 1,
    limit: 20,
  };

  search(1);
};

const showCreateRenderDialog = () => {
  renderDialog.value.create();
};

const showEditRenderDialog = (render: PanelRenderDto) => {
  renderDialog.value.edit(render);
};

const showDeleteRenderDialog = (render: PanelRenderDto) => {
  deleteRenderDialog.value.show(render);
};

onMounted(() => {
  search(1);
});

</script>

<template>
  <div class="graty-page graty-renders-page">
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
        <button type="button" class="primary" @click="showCreateRenderDialog()">{{ $i18n.t("Create panel render") }}</button>
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
      <div class="empty-panel" v-if="!busy && renders.length === 0">
        <div>{{ $i18n.t("No panel renders found") }}</div>
        <button type="button" class="primary" v-if="!query.term" @click="showCreateRenderDialog()">
          {{ $i18n.t("Create panel render") }}
        </button>
      </div>

      <template v-else>
        <table class="hover">
          <thead>
            <tr>
              <th>{{ $i18n.t("Name/Description") }}</th>
              <th>{{ $i18n.t("Fields") }}</th>
              <th class="fit nowrap">{{ $i18n.t("Limit") }}</th>
              <th class="fit nowrap">{{ $i18n.t("Actions menu") }}</th>
              <th class="fit">{{ $i18n.t("Actions") }}</th>
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
                <div class="render-fields-tags" v-if="render.fields && render.fields.length">
                  <span class="render-field-tag" v-for="field in render.fields" :key="field.id">
                    {{ field.name }}
                  </span>
                </div>
                <span class="muted" v-else>—</span>
              </td>
              <td class="fit nowrap">{{ render.limit }}</td>
              <td class="fit nowrap">
                <span v-if="render.showActions" class="render-actions-enabled">
                  <span class="icon-check" aria-hidden="true"></span>
                  {{ $i18n.t("Yes") }}
                </span>
                <span v-else class="muted">{{ $i18n.t("No") }}</span>
              </td>
              <td class="fit">
                <div class="render-row-actions">
                  <button type="button" class="button-link" @click="showEditRenderDialog(render)">{{ $i18n.t("Edit") }}</button>
                  <button type="button" class="button-link" @click="showDeleteRenderDialog(render)">{{ $i18n.t("Delete") }}</button>
                </div>
              </td>
            </tr>
          </tbody>
        </table>
        <PagePaginator
          v-if="total > query.limit"
          :page="query.page"
          :limit="query.limit"
          :total="total"
          :busy="busy"
          @set-page="search"
        />
      </template>
    </div>
    <PanelRenderDialog ref="renderDialog" @created="search(1)" @updated="search(1)"></PanelRenderDialog>
    <DeletePanelRenderDialog ref="deleteRenderDialog" @deleted="search(1)"></DeletePanelRenderDialog>
  </div>
</template>

<style lang="scss">
.graty-renders-page {
  .empty-panel {
    flex-direction: column;
    gap: 12px;
  }

  .render-fields-tags {
    display: flex;
    flex-wrap: wrap;
    gap: 4px;
  }

  .render-field-tag {
    display: inline-block;
    padding: 1px 7px;
    border-radius: 3px;
    border: 1px solid var(--border-color);
    background: var(--surface-alt-background);
    font-size: 90%;
    white-space: nowrap;
  }

  .render-actions-enabled {
    display: inline-flex;
    align-items: center;
    gap: 4px;
    color: var(--success-color);
  }

  .render-row-actions {
    display: flex;
    flex-wrap: wrap;
    align-items: center;
    gap: 4px 10px;

    button.button-link {
      padding: 2px 0;
      margin: 0;
      height: auto;
      min-width: 0;
      white-space: nowrap;
    }
  }
}
</style>
