<script setup lang="ts">
import {
  $notify,
  BaseDialog,
  type Errors,
  ButtonBusy,
  fieldService,
  type IssueFieldInfoDto,
  SortableSelect,
  StatusComponent
} from "luna";
import { computed, nextTick, onMounted, ref, useTemplateRef } from "vue";
import type { ComponentExposed } from "vue-component-type-helpers";
import { $i18n } from "@/utils/i18n.ts";
import type { PanelFieldDto, PanelRenderDto, PanelRenderRequest, PanelRenderValueDto } from "@/interfaces/render.ts";
import { adminRenderService } from "@/service/adminRenderService.ts";

const DEFAULT_FIELD_IDS = ['issueType', 'key', 'summary', 'priority', 'status'];
const QUICK_FIELD_IDS = ['issueType', 'key', 'summary', 'assignee', 'priority', 'status', 'author', 'updated', 'labels'];
const UNSUPPORTED_FIELD_IDS = new Set(['comment', 'watchers']);

const emits = defineEmits({
  created: (_render: PanelRenderDto) => true,
  updated: (_render: PanelRenderDto) => true,
});

const busy = ref(false);
const fieldsLoading = ref(false);
const renderId = ref<number | null>(null);
const request = ref<PanelRenderRequest>({
  name: '',
  description: '',
  fields: [],
  limit: 10,
  showActions: false,
});
const loadedRenderFields = ref<Array<PanelFieldDto>>([]);
const dialog = useTemplateRef<ComponentExposed<typeof BaseDialog>>('dialog');
const nameInput = useTemplateRef<HTMLInputElement>('nameInput');
const errors = ref<Errors>({});
const allFields = ref<Array<IssueFieldInfoDto>>([]);

const selectableFields = computed(() =>
  allFields.value.filter((f) => !UNSUPPORTED_FIELD_IDS.has(f.id) && !!(f.navigatorComponent || f.viewComponent))
);

const fieldsMap = computed(() => {
  const map: Record<string, { id: string, name: string, systemField?: boolean }> = {};
  for (const field of allFields.value) {
    map[field.id] = field;
  }
  for (const field of loadedRenderFields.value) {
    if (!map[field.id]) {
      map[field.id] = { id: field.id, name: field.name, systemField: field.system };
    }
  }
  return map;
});

const modelData = computed<Array<IssueFieldInfoDto>>(() =>
  loadedRenderFields.value.map((f) => ({
    id: f.id as IssueFieldInfoDto['id'],
    name: f.name,
    description: '',
    systemField: f.system,
    viewComponent: f.viewComponent ?? undefined,
    navigatorComponent: f.navigatorComponent ?? undefined,
  }))
);

const quickAddFields = computed(() => {
  const selected = new Set(request.value.fields);
  return QUICK_FIELD_IDS
    .filter((id) => !selected.has(id) && !!fieldsMap.value[id])
    .map((id) => fieldsMap.value[id]);
});

const previewFields = computed(() =>
  request.value.fields
    .map((id) => fieldsMap.value[id])
    .filter((f): f is { id: string, name: string, systemField?: boolean } => !!f)
);

const defaultFieldIds = () => {
  if (selectableFields.value.length === 0) {
    return [...DEFAULT_FIELD_IDS];
  }
  const available = new Set(selectableFields.value.map((f) => f.id as string));
  const ids = DEFAULT_FIELD_IDS.filter((id) => available.has(id));
  return ids.length > 0 ? ids : [selectableFields.value[0].id as string];
};

const loadAllFields = () => {
  if (fieldsLoading.value) {
    return;
  }
  fieldsLoading.value = true;
  fieldService.getAllFields(false).then((data) => {
    allFields.value = data.data;
  }).catch(() => {
    $notify.error($i18n.t("Failed load fields"));
  }).finally(() => {
    fieldsLoading.value = false;
  });
};

const addQuickField = (fieldId: string) => {
  if (!request.value.fields.includes(fieldId)) {
    request.value.fields = [...request.value.fields, fieldId];
  }
};

const resetDefaultFields = () => {
  request.value.fields = defaultFieldIds();
};

const getColumnLabel = (field: { id: string, name: string }) => {
  if (field.id === 'issueType') {
    return $i18n.t("Type");
  }
  return field.name;
};

const isFitField = (fieldId: string) => {
  return ['issueType', 'priority', 'key', 'status', 'statusCategory'].includes(fieldId);
};

const focusName = () => {
  nextTick(() => {
    nameInput.value?.focus();
  });
};

const hide = () => {
  dialog.value.hide();
};

const create = () => {
  renderId.value = null;
  errors.value = {};
  loadedRenderFields.value = [];
  request.value = {
    name: '',
    description: '',
    fields: defaultFieldIds(),
    limit: 10,
    showActions: false,
  };
  if (allFields.value.length === 0) {
    loadAllFields();
  }
  dialog.value.show();
  focusName();
};

const edit = (renderValue: PanelRenderValueDto) => {
  busy.value = true;
  renderId.value = renderValue.id;
  errors.value = {};
  loadedRenderFields.value = [];
  request.value = {
    name: renderValue.name ?? '',
    description: renderValue.description ?? '',
    fields: [],
    limit: 10,
    showActions: false,
  };
  if (allFields.value.length === 0) {
    loadAllFields();
  }
  dialog.value.show();
  adminRenderService.getById(renderValue.id).then((data) => {
    loadedRenderFields.value = data.data.fields ?? [];
    request.value = {
      name: data.data.name ?? '',
      description: data.data.description ?? '',
      fields: (data.data.fields ?? []).map((f) => f.id),
      limit: data.data.limit ?? 10,
      showActions: !!data.data.showActions,
    };
    focusName();
  }).catch(() => {
    $notify.error($i18n.t("Failed load render"));
  }).finally(() => {
    busy.value = false;
  });
};

const submit = () => {
  if (busy.value) {
    return;
  }
  busy.value = true;
  errors.value = {};
  const payload: PanelRenderRequest = {
    name: request.value.name,
    description: request.value.description,
    fields: request.value.fields,
    limit: Number(request.value.limit),
    showActions: !!request.value.showActions,
  };

  if (renderId.value) {
    adminRenderService.update(renderId.value, payload).then((data) => {
      emits('updated', data.data);
      $notify.ok($i18n.t("Render saved"));
      hide();
    }).catch((e) => {
      errors.value = e.data?.errors ?? {};
      $notify.error($i18n.t("Failed to update render"), e.data?.reason);
    }).finally(() => {
      busy.value = false;
    });
  } else {
    adminRenderService.create(payload).then((data) => {
      emits('created', data.data);
      $notify.ok($i18n.t("Render created"));
      hide();
    }).catch((e) => {
      errors.value = e.data?.errors ?? {};
      $notify.error($i18n.t("Failed to create render"), e.data?.reason);
    }).finally(() => {
      busy.value = false;
    });
  }
};

onMounted(() => {
  loadAllFields();
});

defineExpose({
  create,
  edit
});
</script>

<template>
  <BaseDialog ref="dialog" :busy="busy" class="large graty-render-dialog">
    <template v-slot:header>
      <template v-if="renderId">{{ $i18n.t("Edit panel render") }}</template>
      <template v-else>{{ $i18n.t("Create panel render") }}</template>
    </template>
    <template v-slot:default>
      <form class="ui pad" @submit.prevent="submit()">
        <div class="field-group">
          <label for="render-name">{{ $i18n.t("Name") }}</label>
          <input id="render-name" ref="nameInput" class="full-width" type="text" v-model="request.name">
          <div class="error" v-if="errors['name']">{{ errors['name'] }}</div>
        </div>

        <div class="field-group">
          <label for="render-description">{{ $i18n.t("Description") }}</label>
          <textarea id="render-description" class="full-width" rows="2" v-model="request.description"></textarea>
          <div class="error" v-if="errors['description']">{{ errors['description'] }}</div>
        </div>

        <div class="field-group">
          <div class="render-fields-header">
            <label for="render-fields">{{ $i18n.t("Fields") }}</label>
            <button type="button" class="button-link button-small" @click="resetDefaultFields">
              {{ $i18n.t("Reset to defaults") }}
            </button>
          </div>
          <SortableSelect
            id="render-fields"
            :options="selectableFields"
            :model-data="modelData"
            :placeholder="$i18n.t('Add field...')"
            v-model="request.fields"
          ></SortableSelect>
          <div class="render-quick-fields" v-if="quickAddFields.length > 0">
            <span class="muted quick-label">{{ $i18n.t("Quick add:") }}</span>
            <button
              v-for="field in quickAddFields"
              :key="field.id"
              type="button"
              class="button-small render-quick-chip"
              @click="addQuickField(field.id)"
            >
              + {{ field.name }}
            </button>
          </div>
          <div class="description">{{ $i18n.t("Drag fields to change the column order in the panel") }}</div>
          <div class="error" v-if="errors['fields']">{{ errors['fields'] }}</div>
        </div>

        <div class="render-options-row">
          <div class="field-group render-limit-group">
            <label for="render-limit">{{ $i18n.t("Limit") }}</label>
            <input id="render-limit" type="number" min="1" step="1" v-model.number="request.limit">
            <div class="description">{{ $i18n.t("Issues per page") }}</div>
            <div class="error" v-if="errors['limit']">{{ errors['limit'] }}</div>
          </div>

          <div class="field-group render-actions-group">
            <div class="label">{{ $i18n.t("Actions menu") }}</div>
            <fieldset class="checkbox-group">
              <div class="checkbox-item">
                <label for="render-show-actions">
                  <input id="render-show-actions" type="checkbox" v-model="request.showActions">
                  {{ $i18n.t("Show actions") }}
                </label>
              </div>
            </fieldset>
            <div class="description">{{ $i18n.t("Add a dropdown menu with issue actions to each row") }}</div>
            <div class="error" v-if="errors['showActions']">{{ errors['showActions'] }}</div>
          </div>
        </div>

        <div class="field-group render-preview-group" v-if="previewFields.length > 0">
          <div class="label">{{ $i18n.t("Preview") }}</div>
          <div class="render-preview-box">
            <table class="clean-table render-preview-table">
              <thead>
                <tr>
                  <th v-for="field in previewFields" :key="field.id" :class="{ fit: isFitField(field.id) }">
                    {{ getColumnLabel(field) }}
                  </th>
                  <th v-if="request.showActions" class="actions fit" aria-label="Actions"></th>
                </tr>
              </thead>
              <tbody>
                <tr>
                  <td v-for="field in previewFields" :key="field.id" :class="{ fit: isFitField(field.id), nowrap: field.id === 'key' }">
                    <span v-if="field.id === 'issueType'" class="preview-icon-badge" :title="field.name"></span>
                    <span v-else-if="field.id === 'priority'" class="preview-priority-badge" :title="field.name"></span>
                    <span v-else-if="field.id === 'key'" class="preview-link">PRJ-42</span>
                    <span v-else-if="field.id === 'summary'">{{ $i18n.t("Example issue summary") }}</span>
                    <StatusComponent v-else-if="field.id === 'status'" :name="$i18n.t('In progress')" category="in_progress" />
                    <span v-else-if="field.id === 'assignee' || field.id === 'author' || field.id === 'creator'" class="muted">
                      {{ $i18n.t("John Doe") }}
                    </span>
                    <span v-else-if="field.id === 'created' || field.id === 'updated'" class="muted nowrap">12.05.2026</span>
                    <span v-else class="muted">—</span>
                  </td>
                  <td v-if="request.showActions" class="actions fit">
                    <span class="button button-transparent button-icon icon-ellipsis preview-action-btn" aria-hidden="true"></span>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </form>
    </template>
    <template v-slot:footer>
      <div>
        <button type="button" @click="hide()" :disabled="busy">{{ $i18n.t("Cancel") }}</button>
        <ButtonBusy type="button" class="primary" @click="submit()" :busy="busy" :disabled="busy">
          <template v-if="renderId">{{ $i18n.t("Save") }}</template>
          <template v-else>{{ $i18n.t("Create") }}</template>
        </ButtonBusy>
      </div>
    </template>
  </BaseDialog>
</template>

<style lang="scss">
.graty-render-dialog {
  .render-fields-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    gap: 8px;
  }

  .render-quick-fields {
    display: flex;
    flex-wrap: wrap;
    align-items: center;
    gap: 6px;
    margin-top: 8px;

    .quick-label {
      font-size: 90%;
    }

    .render-quick-chip {
      padding: 2px 8px;
      height: auto;
      line-height: 1.5;
      font-size: 90%;
    }
  }

  .render-options-row {
    display: flex;
    flex-wrap: wrap;
    gap: 28px;
    align-items: flex-start;

    .render-limit-group {
      width: 160px;
      flex: 0 0 160px;

      input[type="number"] {
        min-width: 0;
        width: 100%;
        box-sizing: border-box;
      }
    }

    .render-actions-group {
      flex: 1 1 240px;
      min-width: 0;
    }
  }

  .render-preview-box {
    border: 1px solid var(--border-color);
    border-radius: var(--panel-border-radius, 5px);
    background: var(--surface-alt-background);
    padding: 6px 8px;
    overflow-x: auto;
  }

  .render-preview-table {
    margin: 0;

    thead tr:last-child th {
      background: transparent;
      font-size: 85%;
      color: var(--muted-text-color);
    }

    tbody td {
      vertical-align: middle;
    }

    .preview-link {
      color: var(--link-color);
      font-weight: 500;
    }

    .preview-icon-badge {
      display: inline-block;
      width: 14px;
      height: 14px;
      border-radius: 3px;
      background: var(--accent-background);
      vertical-align: middle;
    }

    .preview-priority-badge {
      display: inline-block;
      width: 12px;
      height: 12px;
      border-radius: 50%;
      background: var(--warning-color);
      vertical-align: middle;
    }

    .preview-action-btn {
      pointer-events: none;
    }
  }
}
</style>
