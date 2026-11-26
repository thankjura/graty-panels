<script setup lang="ts">
import {
  $notify,
  BaseDialog,
  type Errors,
  ButtonBusy,
  fieldService,
  type IssueFieldInfoDto,
  SingleSelect,
  StatusComponent
} from "luna";
import Sortable, { type SortableEvent } from "sortablejs";
import { computed, nextTick, onBeforeUnmount, onMounted, ref, useTemplateRef, watch } from "vue";
import type { ComponentExposed } from "vue-component-type-helpers";
import { $i18n } from "@/utils/i18n.ts";
import type {
  ColumnAlign,
  PanelFieldDto,
  PanelRenderColumnDto,
  PanelRenderDto,
  PanelRenderRequest,
  PanelRenderValueDto
} from "@/interfaces/render.ts";
import { adminRenderService } from "@/service/adminRenderService.ts";

interface EditableColumn {
  uid: number,
  fields: Array<string>,
  title: string,
  hideHeader: boolean,
  align: ColumnAlign,
}

const ALIGN_MODES: Array<ColumnAlign> = ['left', 'center', 'right'];
const ICON_ONLY_FIELDS = new Set(['issueType', 'priority']);
const FIT_FIELDS = new Set(['issueType', 'priority', 'key', 'status', 'statusCategory', 'created', 'updated', 'estimate', 'timeSpent', 'timeLeft']);
const QUICK_FIELD_IDS = ['issueType', 'priority', 'key', 'summary', 'assignee', 'status', 'author', 'updated', 'labels'];
const UNSUPPORTED_FIELD_IDS = new Set(['comment', 'watchers']);

const emits = defineEmits({
  created: (_render: PanelRenderDto) => true,
  updated: (_render: PanelRenderDto) => true,
});

let nextColUid = 1;
const makeColumn = (fields: Array<string>, title = '', hideHeader = false, align: ColumnAlign = 'left'): EditableColumn => ({
  uid: nextColUid++,
  fields: [...fields],
  title,
  hideHeader,
  align,
});

const alignLabel = (mode: ColumnAlign) => {
  if (mode === 'center') return $i18n.t("Align center");
  if (mode === 'right') return $i18n.t("Align right");
  return $i18n.t("Align left");
};

const busy = ref(false);
const fieldsLoading = ref(false);
const renderId = ref<number | null>(null);
const name = ref('');
const description = ref('');
const limit = ref(10);
const showActions = ref(false);
const showHeader = ref(true);
const columns = ref<Array<EditableColumn>>([]);
const loadedRenderFields = ref<Array<PanelFieldDto>>([]);
const addingFieldColUid = ref<number | null>(null);

const dialog = useTemplateRef<ComponentExposed<typeof BaseDialog>>('dialog');
const nameInput = useTemplateRef<HTMLInputElement>('nameInput');
const columnsListEl = useTemplateRef<HTMLUListElement>('columnsListEl');
const addColumnSelect = useTemplateRef<ComponentExposed<typeof SingleSelect>>('addColumnSelect');
const errors = ref<Errors>({});
const allFields = ref<Array<IssueFieldInfoDto>>([]);
let sortableInstance: Sortable | null = null;

const selectableFields = computed(() =>
  allFields.value.filter((f) => !UNSUPPORTED_FIELD_IDS.has(f.id) && !!(f.navigatorComponent || f.viewComponent))
);

const fieldsMap = computed(() => {
  const map: Record<string, { id: string, name: string }> = {};
  for (const field of allFields.value) {
    map[field.id] = { id: field.id, name: field.name };
  }
  for (const field of loadedRenderFields.value) {
    if (!map[field.id]) {
      map[field.id] = { id: field.id, name: field.name };
    }
  }
  return map;
});

const usedFieldIds = computed(() => {
  const set = new Set<string>();
  for (const col of columns.value) {
    for (const id of col.fields) {
      set.add(id);
    }
  }
  return set;
});

const unusedSelectableFields = computed(() =>
  selectableFields.value.filter((f) => !usedFieldIds.value.has(f.id as string))
);

const quickAddFields = computed(() =>
  QUICK_FIELD_IDS
    .filter((id) => !usedFieldIds.value.has(id) && !!fieldsMap.value[id])
    .map((id) => fieldsMap.value[id])
);

const availableFieldsForColumn = (col: EditableColumn) => {
  const inCol = new Set(col.fields);
  return selectableFields.value.filter((f) => !inCol.has(f.id as string));
};

const getFieldName = (fieldId: string) => fieldsMap.value[fieldId]?.name ?? fieldId;

const getShortFieldLabel = (fieldId: string) => {
  if (fieldId === 'issueType') {
    return $i18n.t("Type");
  }
  return getFieldName(fieldId);
};

const autoColumnTitle = (col: EditableColumn) => {
  if (col.fields.length === 0) {
    return '';
  }
  if (col.fields.length === 1) {
    return getShortFieldLabel(col.fields[0]);
  }
  const nonIcon = col.fields.find((id) => !ICON_ONLY_FIELDS.has(id));
  return getShortFieldLabel(nonIcon ?? col.fields[0]);
};

const effectiveColumnTitle = (col: EditableColumn) => {
  if (col.hideHeader) {
    return '';
  }
  return col.title.trim() || autoColumnTitle(col);
};

const previewHasHeader = computed(() =>
  showHeader.value && columns.value.some((col) => !col.hideHeader)
);

const isFitColumn = (col: EditableColumn) =>
  col.fields.length > 0 && col.fields.every((id) => FIT_FIELDS.has(id));

const isIconOnlyColumn = (col: EditableColumn) =>
  col.fields.length > 0 && col.fields.every((id) => ICON_ONLY_FIELDS.has(id));

const isSummaryColumn = (col: EditableColumn) =>
  col.fields.length === 1 && col.fields[0] === 'summary';

const filterAvailable = (ids: Array<string>) => {
  if (selectableFields.value.length === 0) {
    return ids;
  }
  const available = new Set(selectableFields.value.map((f) => f.id as string));
  return ids.filter((id) => available.has(id));
};

const applyDefaultPreset = () => {
  const result: Array<EditableColumn> = [];
  for (const id of filterAvailable(['issueType', 'priority', 'key', 'summary', 'status'])) {
    result.push(makeColumn([id], '', ICON_ONLY_FIELDS.has(id)));
  }
  columns.value = result.length > 0
    ? result
    : (selectableFields.value[0] ? [makeColumn([selectableFields.value[0].id as string])] : []);
  showHeader.value = true;
  addingFieldColUid.value = null;
};

const applyLinksStylePreset = () => {
  const result: Array<EditableColumn> = [];
  const keyGroup = filterAvailable(['issueType', 'key']);
  if (keyGroup.length) result.push(makeColumn(keyGroup, '', false));
  const summaryGroup = filterAvailable(['summary']);
  if (summaryGroup.length) result.push(makeColumn(summaryGroup, '', false));
  const assigneeGroup = filterAvailable(['assignee']);
  if (assigneeGroup.length) result.push(makeColumn(assigneeGroup, '', false));
  const statusGroup = filterAvailable(['priority', 'status']);
  if (statusGroup.length) result.push(makeColumn(statusGroup, '', false, 'right'));
  columns.value = result;
  showHeader.value = false;
  showActions.value = true;
  addingFieldColUid.value = null;
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

const addNewColumn = (fieldId: string) => {
  if (!fieldId) {
    return;
  }
  columns.value.push(makeColumn([fieldId], '', ICON_ONLY_FIELDS.has(fieldId)));
};

const onAddColumnSelect = (option: IssueFieldInfoDto | null) => {
  if (option) {
    addNewColumn(option.id as string);
    addColumnSelect.value?.setValue(null);
  }
};

const toggleAddFieldToColumn = (colUid: number) => {
  addingFieldColUid.value = addingFieldColUid.value === colUid ? null : colUid;
};

const onAddFieldToColumn = (col: EditableColumn, option: IssueFieldInfoDto | null) => {
  if (option && !col.fields.includes(option.id as string)) {
    col.fields.push(option.id as string);
    if (col.hideHeader && !isIconOnlyColumn(col)) {
      col.hideHeader = false;
    }
  }
  addingFieldColUid.value = null;
};

const removeFieldFromColumn = (colIdx: number, fieldIdx: number) => {
  const col = columns.value[colIdx];
  if (!col) return;
  col.fields.splice(fieldIdx, 1);
  if (col.fields.length === 0) {
    columns.value.splice(colIdx, 1);
  }
};

const removeColumn = (colIdx: number) => {
  columns.value.splice(colIdx, 1);
};

const mergeWithPrevious = (colIdx: number) => {
  if (colIdx <= 0 || colIdx >= columns.value.length) {
    return;
  }
  const prev = columns.value[colIdx - 1];
  const curr = columns.value[colIdx];
  for (const fieldId of curr.fields) {
    if (!prev.fields.includes(fieldId)) {
      prev.fields.push(fieldId);
    }
  }
  if (!prev.title && curr.title) {
    prev.title = curr.title;
  }
  if (prev.hideHeader && !curr.hideHeader && !isIconOnlyColumn(prev)) {
    prev.hideHeader = false;
  }
  columns.value.splice(colIdx, 1);
};

const splitColumn = (colIdx: number) => {
  const col = columns.value[colIdx];
  if (!col || col.fields.length <= 1) {
    return;
  }
  const splitCols = col.fields.map((fieldId) =>
    makeColumn([fieldId], '', col.hideHeader || ICON_ONLY_FIELDS.has(fieldId), col.align)
  );
  columns.value.splice(colIdx, 1, ...splitCols);
};

const toggleColumnHeader = (col: EditableColumn) => {
  col.hideHeader = !col.hideHeader;
};

const initSortable = () => {
  if (sortableInstance) {
    sortableInstance.destroy();
    sortableInstance = null;
  }
  if (!columnsListEl.value) {
    return;
  }
  sortableInstance = new Sortable(columnsListEl.value, {
    handle: '.col-drag-handle',
    animation: 150,
    ghostClass: 'ghost',
    draggable: 'li.render-column-row',
    forceFallback: true,
    onUpdate: (event: SortableEvent) => {
      const { oldIndex, newIndex } = event;
      if (oldIndex === undefined || newIndex === undefined || oldIndex === newIndex) {
        return;
      }
      const next = [...columns.value];
      const [moved] = next.splice(oldIndex, 1);
      next.splice(newIndex, 0, moved);
      columns.value = next;
    },
  });
};

watch(columnsListEl, () => {
  initSortable();
});

const focusName = () => {
  nextTick(() => {
    nameInput.value?.focus();
  });
};

const hide = () => {
  addingFieldColUid.value = null;
  dialog.value.hide();
};

const create = () => {
  renderId.value = null;
  errors.value = {};
  loadedRenderFields.value = [];
  addingFieldColUid.value = null;
  name.value = '';
  description.value = '';
  limit.value = 10;
  showActions.value = false;
  applyDefaultPreset();
  if (allFields.value.length === 0) {
    loadAllFields();
  }
  dialog.value.show();
  nextTick(() => {
    initSortable();
    focusName();
  });
};

const columnsFromDto = (dto: PanelRenderDto): Array<EditableColumn> => {
  if (dto.columns && dto.columns.length > 0) {
    return dto.columns.map((c: PanelRenderColumnDto) =>
      makeColumn(c.fields.map((f) => f.id), c.title ?? '', !!c.hideHeader, c.align ?? 'left')
    );
  }
  return (dto.fields ?? []).map((f) => makeColumn([f.id], '', false, 'left'));
};

const edit = (renderValue: PanelRenderValueDto) => {
  busy.value = true;
  renderId.value = renderValue.id;
  errors.value = {};
  loadedRenderFields.value = [];
  addingFieldColUid.value = null;
  name.value = renderValue.name ?? '';
  description.value = renderValue.description ?? '';
  limit.value = 10;
  showActions.value = false;
  showHeader.value = true;
  columns.value = [];
  if (allFields.value.length === 0) {
    loadAllFields();
  }
  dialog.value.show();
  adminRenderService.getById(renderValue.id).then((data) => {
    loadedRenderFields.value = data.data.fields ?? [];
    name.value = data.data.name ?? '';
    description.value = data.data.description ?? '';
    limit.value = data.data.limit ?? 10;
    showActions.value = !!data.data.showActions;
    showHeader.value = data.data.showHeader !== false;
    columns.value = columnsFromDto(data.data);
    nextTick(() => {
      initSortable();
      focusName();
    });
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

  const colRequests = columns.value
    .filter((c) => c.fields.length > 0)
    .map((c) => ({
      fields: [...c.fields],
      title: c.title.trim() || null,
      hideHeader: !!c.hideHeader,
      align: c.align,
    }));

  const payload: PanelRenderRequest = {
    name: name.value,
    description: description.value,
    columns: colRequests,
    limit: Number(limit.value),
    showActions: !!showActions.value,
    showHeader: !!showHeader.value,
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

onBeforeUnmount(() => {
  if (sortableInstance) {
    sortableInstance.destroy();
    sortableInstance = null;
  }
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
          <input id="render-name" ref="nameInput" class="full-width" type="text" v-model="name">
          <div class="error" v-if="errors['name']">{{ errors['name'] }}</div>
        </div>

        <div class="field-group">
          <label for="render-description">{{ $i18n.t("Description") }}</label>
          <textarea id="render-description" class="full-width" rows="2" v-model="description"></textarea>
          <div class="error" v-if="errors['description']">{{ errors['description'] }}</div>
        </div>

        <div class="field-group">
          <div class="render-fields-header">
            <label>{{ $i18n.t("Columns") }}</label>
            <div class="render-presets">
              <button type="button" class="button-link button-small" @click="applyDefaultPreset">
                {{ $i18n.t("Default preset") }}
              </button>
              <span class="muted">·</span>
              <button type="button" class="button-link button-small" @click="applyLinksStylePreset">
                {{ $i18n.t("Links panel style") }}
              </button>
            </div>
          </div>

          <div class="render-columns-box">
            <div class="render-columns-empty muted" v-if="columns.length === 0">
              {{ $i18n.t("Add at least one column below") }}
            </div>
            <ul ref="columnsListEl" class="render-columns-list" v-show="columns.length > 0">
              <li
                v-for="(col, colIdx) in columns"
                :key="col.uid"
                class="render-column-row"
              >
                <span class="col-drag-handle icon-grip-vertical" :title="$i18n.t('Drag to reorder')"></span>

                <div class="col-fields-cell">
                  <div class="col-fields-pills">
                    <span class="col-field-pill" v-for="(fieldId, fieldIdx) in col.fields" :key="fieldId">
                      <span>{{ getFieldName(fieldId) }}</span>
                      <button
                        type="button"
                        class="col-field-remove icon-cross"
                        :title="$i18n.t('Remove field')"
                        @click="removeFieldFromColumn(colIdx, fieldIdx)"
                      ></button>
                    </span>
                    <button
                      type="button"
                      class="col-add-field-btn icon-plus"
                      :class="{ active: addingFieldColUid === col.uid }"
                      :title="$i18n.t('Add field to this column')"
                      @click="toggleAddFieldToColumn(col.uid)"
                    ></button>
                  </div>

                  <div class="col-inline-picker" v-if="addingFieldColUid === col.uid">
                    <SingleSelect
                      :options="availableFieldsForColumn(col)"
                      :placeholder="$i18n.t('Select field to combine...')"
                      @select="(opt) => onAddFieldToColumn(col, opt)"
                    />
                  </div>
                </div>

                <div class="col-header-cell" v-if="showHeader">
                  <span v-if="col.hideHeader" class="col-no-header-badge muted">
                    {{ $i18n.t("No header") }}
                  </span>
                  <input
                    v-else
                    type="text"
                    class="col-title-input"
                    v-model="col.title"
                    :placeholder="autoColumnTitle(col)"
                    :title="$i18n.t('Column header (leave empty for default)')"
                  >
                </div>

                <div class="col-align-group" role="group" :aria-label="$i18n.t('Alignment')">
                  <button
                    v-for="mode in ALIGN_MODES"
                    :key="mode"
                    type="button"
                    class="col-align-btn"
                    :class="{ active: col.align === mode }"
                    :aria-pressed="col.align === mode"
                    :title="alignLabel(mode)"
                    @click="col.align = mode"
                  >
                    <span class="align-bars" :class="'align-bars-' + mode" aria-hidden="true">
                      <span></span><span></span><span></span>
                    </span>
                  </button>
                </div>

                <div class="col-row-actions">
                  <button
                    v-if="colIdx > 0"
                    type="button"
                    class="button-icon button-transparent button-small icon-table-cells-merge"
                    :title="$i18n.t('Merge with previous column')"
                    @click="mergeWithPrevious(colIdx)"
                  ></button>
                  <button
                    v-if="col.fields.length > 1"
                    type="button"
                    class="button-icon button-transparent button-small icon-table-cells-split"
                    :title="$i18n.t('Split into separate columns')"
                    @click="splitColumn(colIdx)"
                  ></button>
                  <button
                    v-if="showHeader"
                    type="button"
                    class="button-icon button-transparent button-small"
                    :class="col.hideHeader ? 'icon-eye-off col-action-muted' : 'icon-eye'"
                    :title="col.hideHeader ? $i18n.t('Show column header') : $i18n.t('Hide column header')"
                    @click="toggleColumnHeader(col)"
                  ></button>
                  <button
                    type="button"
                    class="button-icon button-transparent button-small icon-cross col-action-danger"
                    :title="$i18n.t('Remove column')"
                    @click="removeColumn(colIdx)"
                  ></button>
                </div>
              </li>
            </ul>
          </div>

          <div class="render-add-column-row">
            <SingleSelect
              ref="addColumnSelect"
              class="full-width"
              :options="unusedSelectableFields"
              :placeholder="$i18n.t('Add column...')"
              @select="onAddColumnSelect"
            />
          </div>

          <div class="render-quick-fields" v-if="quickAddFields.length > 0">
            <span class="muted quick-label">{{ $i18n.t("Quick add:") }}</span>
            <button
              v-for="field in quickAddFields"
              :key="field.id"
              type="button"
              class="button-small render-quick-chip"
              @click="addNewColumn(field.id)"
            >
              + {{ field.name }}
            </button>
          </div>
          <div class="description">{{ $i18n.t("Drag columns to reorder, combine multiple fields into one column, or hide individual headers") }}</div>
          <div class="error" v-if="errors['fields']">{{ errors['fields'] }}</div>
        </div>

        <div class="render-options-row">
          <div class="field-group render-limit-group">
            <label for="render-limit">{{ $i18n.t("Limit") }}</label>
            <input id="render-limit" type="number" min="1" step="1" v-model.number="limit">
            <div class="description">{{ $i18n.t("Issues per page") }}</div>
            <div class="error" v-if="errors['limit']">{{ errors['limit'] }}</div>
          </div>

          <div class="field-group render-toggles-group">
            <div class="label">{{ $i18n.t("Display options") }}</div>
            <fieldset class="checkbox-group">
              <div class="checkbox-item">
                <label for="render-show-header">
                  <input id="render-show-header" type="checkbox" v-model="showHeader">
                  {{ $i18n.t("Show table header") }}
                </label>
              </div>
              <div class="checkbox-item">
                <label for="render-show-actions">
                  <input id="render-show-actions" type="checkbox" v-model="showActions">
                  {{ $i18n.t("Show actions") }}
                </label>
              </div>
            </fieldset>
            <div class="description">{{ $i18n.t("Show actions adds a dropdown menu with issue actions to each row") }}</div>
          </div>
        </div>

        <div class="field-group render-preview-group" v-if="columns.length > 0">
          <div class="label">{{ $i18n.t("Preview") }}</div>
          <div class="render-preview-box">
            <table class="clean-table render-preview-table">
              <thead v-if="previewHasHeader">
                <tr>
                  <th
                    v-for="col in columns"
                    :key="col.uid"
                    :class="[
                      'align-' + col.align,
                      {
                        fit: isFitColumn(col),
                        'col-icon-only': col.hideHeader && isIconOnlyColumn(col),
                        'col-summary': isSummaryColumn(col),
                      }
                    ]"
                  >
                    {{ effectiveColumnTitle(col) }}
                  </th>
                  <th v-if="showActions" class="actions fit" aria-label="Actions"></th>
                </tr>
              </thead>
              <tbody>
                <tr>
                  <td
                    v-for="col in columns"
                    :key="col.uid"
                    :class="[
                      'align-' + col.align,
                      {
                        fit: isFitColumn(col),
                        'col-icon-only': col.hideHeader && isIconOnlyColumn(col),
                        'col-summary': isSummaryColumn(col),
                      }
                    ]"
                  >
                    <div class="preview-cell-group">
                      <template v-for="fieldId in col.fields" :key="fieldId">
                        <span v-if="fieldId === 'issueType'" class="preview-icon-badge" :title="getFieldName(fieldId)"></span>
                        <span v-else-if="fieldId === 'priority'" class="preview-priority-badge" :title="getFieldName(fieldId)"></span>
                        <span v-else-if="fieldId === 'key'" class="preview-link nowrap">PRJ-42</span>
                        <span v-else-if="fieldId === 'summary'" class="preview-summary">{{ $i18n.t("Example issue summary") }}</span>
                        <StatusComponent v-else-if="fieldId === 'status'" :name="$i18n.t('In progress')" category="in_progress" />
                        <span v-else-if="fieldId === 'assignee' || fieldId === 'author' || fieldId === 'creator'" class="muted nowrap">
                          {{ $i18n.t("John Doe") }}
                        </span>
                        <span v-else-if="fieldId === 'created' || fieldId === 'updated'" class="muted nowrap">12.05.2026</span>
                        <span v-else class="muted nowrap">{{ getFieldName(fieldId) }}</span>
                      </template>
                    </div>
                  </td>
                  <td v-if="showActions" class="actions fit">
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
    flex-wrap: wrap;
  }

  .render-presets {
    display: inline-flex;
    align-items: center;
    gap: 6px;

    button.button-link {
      padding: 0 2px;
      margin: 0;
      height: auto;
    }
  }

  .render-columns-box {
    border: 1px solid var(--border-color);
    border-radius: var(--panel-border-radius, 5px);
    background: var(--surface-alt-background);
    padding: 6px;
    margin-bottom: 8px;
  }

  .render-columns-empty {
    text-align: center;
    padding: 12px 8px;
    font-size: 90%;
  }

  .render-columns-list {
    list-style: none;
    margin: 0;
    padding: 0;
    display: flex;
    flex-direction: column;
    gap: 6px;
  }

  .render-column-row {
    display: flex;
    align-items: center;
    gap: 8px;
    padding: 5px 8px;
    border-radius: 4px;
    background: var(--surface-background);
    border: 1px solid var(--border-color);

    &.ghost {
      opacity: 0.35;
    }

    .col-drag-handle {
      cursor: grab;
      color: var(--muted-text-color);
      flex-shrink: 0;
      display: inline-flex;
      align-items: center;

      &:active {
        cursor: grabbing;
      }
    }

    .col-fields-cell {
      flex: 1 1 auto;
      min-width: 0;
      display: flex;
      flex-direction: column;
      gap: 6px;
    }

    .col-fields-pills {
      display: flex;
      flex-wrap: wrap;
      align-items: center;
      gap: 5px;
    }

    .col-field-pill {
      display: inline-flex;
      align-items: center;
      gap: 4px;
      padding: 2px 6px 2px 8px;
      border-radius: 3px;
      background: var(--input-tag-background);
      color: var(--input-tag-color);
      font-size: 90%;
      line-height: 1.4;
      white-space: nowrap;
    }

    .col-field-remove {
      background: transparent;
      border: none;
      padding: 0 2px;
      margin: 0;
      height: auto;
      min-width: 0;
      font-size: 75%;
      color: var(--muted-text-color);
      cursor: pointer;

      &:hover {
        color: var(--danger-color);
      }
    }

    .col-add-field-btn {
      width: 22px;
      height: 22px;
      min-width: 22px;
      padding: 0;
      margin: 0;
      border-radius: 3px;
      border: 1px dashed var(--border-color);
      background: transparent;
      color: var(--muted-text-color);
      font-size: 80%;
      display: inline-flex;
      align-items: center;
      justify-content: center;
      cursor: pointer;

      &:hover,
      &.active {
        border-color: var(--accent-background);
        color: var(--text-color);
      }
    }

    .col-inline-picker {
      max-width: 260px;
    }

    .col-header-cell {
      flex: 0 0 130px;
      min-width: 0;
      display: flex;
      align-items: center;
    }

    .col-title-input {
      width: 100%;
      min-width: 0;
      box-sizing: border-box;
      height: 26px;
      padding: 2px 7px;
      font-size: 88%;
    }

    .col-no-header-badge {
      font-size: 85%;
      font-style: italic;
      padding: 2px 6px;
    }

    .col-align-group {
      display: inline-flex;
      align-items: center;
      flex-shrink: 0;
      border: 1px solid var(--border-color);
      border-radius: 4px;
      overflow: hidden;
      background: var(--surface-alt-background);
    }

    .col-align-btn {
      width: 22px;
      height: 24px;
      min-width: 22px;
      padding: 0;
      margin: 0;
      border: none;
      border-radius: 0;
      background: transparent;
      color: var(--muted-text-color);
      display: inline-flex;
      align-items: center;
      justify-content: center;
      cursor: pointer;

      &:hover {
        color: var(--text-color);
        background: var(--surface-background);
      }

      &.active {
        background: var(--accent-background);
        color: var(--accent-text-color, #fff);
      }
    }

    .align-bars {
      width: 11px;
      display: flex;
      flex-direction: column;
      gap: 2px;

      span {
        display: block;
        height: 1.5px;
        border-radius: 1px;
        background: currentColor;

        &:nth-child(1) { width: 11px; }
        &:nth-child(2) { width: 7px; }
        &:nth-child(3) { width: 9px; }
      }

      &.align-bars-left { align-items: flex-start; }
      &.align-bars-center { align-items: center; }
      &.align-bars-right { align-items: flex-end; }
    }

    .col-row-actions {
      display: inline-flex;
      align-items: center;
      gap: 2px;
      flex-shrink: 0;

      button {
        margin: 0;
        padding: 0;
        width: 26px;
        height: 26px;
        min-width: 26px;
        font-size: 90%;
      }

      .col-action-muted {
        color: var(--muted-text-color);
      }

      .col-action-danger:hover {
        color: var(--danger-color);
      }
    }
  }

  .render-add-column-row {
    margin-bottom: 6px;
  }

  .render-quick-fields {
    display: flex;
    flex-wrap: wrap;
    align-items: center;
    gap: 6px;
    margin-top: 4px;

    .quick-label {
      font-size: 90%;
    }

    .render-quick-chip {
      padding: 2px 8px;
      margin: 0;
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

    .render-toggles-group {
      flex: 1 1 280px;
      min-width: 0;

      .checkbox-group {
        display: flex;
        flex-wrap: wrap;
        gap: 8px 20px;
      }
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
      padding: 0 6px;
      line-height: 24px;
      white-space: nowrap;

      &.align-center { text-align: center; }
      &.align-right { text-align: right; }
    }

    tbody tr td {
      background: transparent;
      vertical-align: middle;
      padding: 4px 6px;

      &.align-center { text-align: center; }
      &.align-right { text-align: right; }
    }

    th.col-icon-only,
    td.col-icon-only {
      padding-left: 3px;
      padding-right: 3px;
    }

    th.col-summary,
    td.col-summary {
      width: 100%;
    }

    .preview-cell-group {
      display: inline-flex;
      align-items: center;
      gap: 6px;
      min-width: 0;
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
      flex-shrink: 0;
    }

    .preview-priority-badge {
      display: inline-block;
      width: 12px;
      height: 12px;
      border-radius: 50%;
      background: var(--warning-color);
      flex-shrink: 0;
    }

    .preview-action-btn {
      pointer-events: none;
    }
  }
}
</style>
