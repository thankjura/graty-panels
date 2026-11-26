<script setup lang="ts">
import { computed, getCurrentInstance, onBeforeUnmount, onMounted, type PropType, ref, watch } from "vue";
import type { IssuePanelDto, GratyIssueDto } from "@/interfaces/issue.ts";
import type { PanelFieldDto, PanelRenderColumnDto } from "@/interfaces/render.ts";
import { issuePanelService } from "@/service/issuePanelService.ts";
import { issueDropDownOptions } from "@/utils/issueActions.ts";
import { $i18n } from "@/utils/i18n.ts";
import {
  $bus,
  BusyIconComponent,
  DropDownButton,
  type IssueDto,
  PagePaginator
} from "luna";

const INITIATOR = 'gratyIssuePanel';
const ICON_ONLY_FIELDS = new Set(['issueType', 'priority']);
const FIT_FIELDS = new Set(['issueType', 'priority', 'key', 'status', 'statusCategory', 'created', 'updated', 'estimate', 'timeSpent', 'timeLeft']);
const NOWRAP_FIELDS = new Set(['issueType', 'priority', 'key', 'status', 'statusCategory', 'created', 'updated', 'estimate', 'timeSpent', 'timeLeft']);

const props = defineProps({
  issueKey: {
    required: true,
    type: String
  },
  panel: {
    required: true,
    type: Object as PropType<IssuePanelDto>
  }
});

const instance = getCurrentInstance();
const issues = ref<Array<GratyIssueDto>>([]);
const total = ref(0);
const limit = ref(10);
const page = ref(1);
const busy = ref(false);
let requestSeq = 0;

const columns = computed<Array<PanelRenderColumnDto>>(() => {
  if (props.panel.columns && props.panel.columns.length > 0) {
    return props.panel.columns;
  }
  return (props.panel.fields ?? []).map((f): PanelRenderColumnDto => ({
    fields: [f],
    title: null,
    hideHeader: false,
    align: 'left',
  }));
});

const fieldIds = computed(() => {
  const ids = new Set<string>();
  for (const col of columns.value) {
    for (const f of col.fields) {
      ids.add(f.id);
    }
  }
  return Array.from(ids);
});

const hasTableHeader = computed(() =>
  props.panel.showHeader !== false && columns.value.some((col) => !col.hideHeader)
);

const getShortFieldName = (field: PanelFieldDto) => {
  if (field.id === 'issueType') {
    return $i18n.t("Type");
  }
  return field.name;
};

const getColumnTitle = (col: PanelRenderColumnDto) => {
  if (col.hideHeader) {
    return '';
  }
  if (col.title) {
    return col.title;
  }
  if (col.fields.length === 0) {
    return '';
  }
  if (col.fields.length === 1) {
    return getShortFieldName(col.fields[0]);
  }
  const nonIcon = col.fields.find((f) => !ICON_ONLY_FIELDS.has(f.id));
  return getShortFieldName(nonIcon ?? col.fields[0]);
};

const isFitColumn = (col: PanelRenderColumnDto) =>
  col.fields.length > 0 && col.fields.every((f) => FIT_FIELDS.has(f.id));

const isNowrapColumn = (col: PanelRenderColumnDto) =>
  col.fields.length > 0 && col.fields.every((f) => NOWRAP_FIELDS.has(f.id));

const isIconOnlyColumn = (col: PanelRenderColumnDto) =>
  col.fields.length > 0 && col.fields.every((f) => ICON_ONLY_FIELDS.has(f.id));

const isSummaryColumn = (col: PanelRenderColumnDto) =>
  col.fields.length === 1 && col.fields[0].id === 'summary';

const cellTitle = (col: PanelRenderColumnDto, issue: GratyIssueDto) => {
  if (isSummaryColumn(col) && typeof issue.summary === 'string') {
    return issue.summary;
  }
  return undefined;
};

const resolveFieldComponent = (field: PanelFieldDto): string | null => {
  const registered = instance?.appContext?.components ?? {};
  if (field.navigatorComponent && registered[field.navigatorComponent]) {
    return field.navigatorComponent;
  }
  if (field.viewComponent && registered[field.viewComponent]) {
    return field.viewComponent;
  }
  return null;
};

const search = (nextPage?: number) => {
  const targetPage = nextPage ?? page.value;
  const seq = ++requestSeq;
  busy.value = true;
  issuePanelService.searchIssues(props.panel.id, props.issueKey, {page: targetPage, limit: limit.value}).then((data) => {
    if (seq !== requestSeq) {
      return;
    }
    issues.value = data.data.results;
    total.value = data.data.total;
    limit.value = data.data.limit;
    page.value = data.data.page;
  }).catch(() => {
    if (seq !== requestSeq) {
      return;
    }
  }).finally(() => {
    if (seq === requestSeq) {
      busy.value = false;
    }
  });
};

const syncFromPanel = () => {
  requestSeq++;
  limit.value = props.panel.limit || 10;
  page.value = 1;
  if (props.panel.total !== null && props.panel.total !== undefined) {
    issues.value = props.panel.issues ?? [];
    total.value = props.panel.total;
    busy.value = false;
  } else {
    issues.value = [];
    total.value = 0;
    search(1);
  }
};

const dropDownOptions = (issue: GratyIssueDto) => {
  return issueDropDownOptions(issue.key, fieldIds.value, INITIATOR);
};

const onIssueUpdated = (data: { issue: IssueDto }) => {
  const key = data.issue?.key;
  if (!key) {
    return;
  }
  if (key === props.issueKey || issues.value.some((i) => i.key === key)) {
    search(page.value);
  }
};

const onIssueDeleted = (data: { issue: IssueDto }) => {
  const deletedId = data.issue?.id;
  const deletedKey = data.issue?.key;
  if (issues.value.some((i) => i.id === deletedId || i.key === deletedKey)) {
    const nextPage = issues.value.length <= 1 && page.value > 1 ? page.value - 1 : page.value;
    search(nextPage);
  }
};

watch(() => [props.panel.id, props.issueKey], () => {
  syncFromPanel();
});

onMounted(() => {
  syncFromPanel();
  $bus.on('issue.updated', onIssueUpdated);
  $bus.on('issue.deleted', onIssueDeleted);
});

onBeforeUnmount(() => {
  $bus.off('issue.updated', onIssueUpdated);
  $bus.off('issue.deleted', onIssueDeleted);
});
</script>

<template>
  <div class="graty-panel-component">
    <div class="graty-panel-empty" v-if="issues.length === 0">
      <BusyIconComponent v-if="busy" />
      <span class="muted" v-else>{{ $i18n.t("No issues") }}</span>
    </div>
    <template v-else>
      <div class="graty-panel-table-wrap">
        <table class="graty-panel-table clean-table hover">
          <thead v-if="hasTableHeader">
            <tr>
              <th
                v-for="(col, colIdx) in columns"
                :key="colIdx"
                :class="[
                  'align-' + (col.align || 'left'),
                  {
                    fit: isFitColumn(col),
                    nowrap: isNowrapColumn(col),
                    'col-icon-only': col.hideHeader && isIconOnlyColumn(col),
                    'col-summary': isSummaryColumn(col),
                  }
                ]"
              >
                {{ getColumnTitle(col) }}
              </th>
              <th class="actions fit" v-if="panel.showActions"></th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="issue in issues" :key="issue.id">
              <td
                v-for="(col, colIdx) in columns"
                :key="colIdx"
                :class="[
                  'align-' + (col.align || 'left'),
                  {
                    fit: isFitColumn(col),
                    nowrap: isNowrapColumn(col),
                    'col-icon-only': (col.hideHeader || !hasTableHeader) && isIconOnlyColumn(col),
                    'col-summary': isSummaryColumn(col),
                  }
                ]"
                :title="cellTitle(col, issue)"
              >
                <template v-if="col.fields.length === 1">
                  <component
                    v-if="resolveFieldComponent(col.fields[0])"
                    :is="resolveFieldComponent(col.fields[0])"
                    :value="issue[col.fields[0].id]"
                    :field="col.fields[0]"
                  />
                  <span v-else-if="issue[col.fields[0].id] != null">{{ issue[col.fields[0].id] }}</span>
                </template>
                <div class="graty-cell-group" v-else>
                  <template v-for="field in col.fields" :key="field.id">
                    <template v-if="issue[field.id] != null">
                      <span
                        class="graty-cell-item"
                        :class="{ 'graty-cell-item-summary': field.id === 'summary' }"
                        :title="field.id === 'summary' && typeof issue[field.id] === 'string' ? issue[field.id] as string : undefined"
                      >
                        <component
                          v-if="resolveFieldComponent(field)"
                          :is="resolveFieldComponent(field)"
                          :value="issue[field.id]"
                          :field="field"
                        />
                        <span v-else>{{ issue[field.id] }}</span>
                      </span>
                    </template>
                  </template>
                </div>
              </td>
              <td class="actions fit" v-if="panel.showActions">
                <DropDownButton
                  :options="() => dropDownOptions(issue)"
                  :cache-options="false"
                  :toggle-icon="false"
                  class="button button-transparent button-icon icon-ellipsis"
                  :title="$i18n.t('Actions')"
                ></DropDownButton>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
      <div class="graty-panel-paginator" v-if="total > limit">
        <PagePaginator :compact="true" :page="page" :total="total" :limit="limit" :busy="busy" @set-page="search" />
      </div>
    </template>
  </div>
</template>

<style lang="scss">
.graty-panel-component {
  padding: 6px 10px 10px;

  .graty-panel-empty {
    display: flex;
    justify-content: center;
    padding: 8px;
  }

  .graty-panel-table-wrap {
    overflow-x: auto;
  }

  .graty-panel-table {
    overflow: visible;

    thead tr:last-child th {
      background: transparent;
      font-size: 85%;
      color: var(--muted-text-color);
      border-bottom-width: 1px;
      padding: 0 6px;
      line-height: 26px;
      white-space: nowrap;

      &.align-center {
        text-align: center;
      }

      &.align-right {
        text-align: right;
      }
    }

    tbody tr td {
      background: transparent;
      vertical-align: middle;
      padding: 4px 6px;

      &.align-center {
        text-align: center;

        > .iconed {
          justify-content: center;
        }
      }

      &.align-right {
        text-align: right;

        > .iconed {
          justify-content: flex-end;
        }
      }
    }

    tbody tr:hover td {
      background: var(--table-highlight-background);
    }

    th.col-icon-only,
    td.col-icon-only {
      padding-left: 3px;
      padding-right: 3px;
    }

    th.col-summary,
    td.col-summary {
      width: 100%;
      min-width: 140px;
      max-width: 0;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
    }

    .graty-cell-group {
      display: inline-flex;
      align-items: center;
      gap: 6px;
      min-width: 0;
      max-width: 100%;
    }

    .graty-cell-item {
      display: inline-flex;
      align-items: center;
      min-width: 0;

      &.graty-cell-item-summary {
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
      }
    }

    .user-link-component {
      max-width: 145px;
      min-width: 0;

      a.link {
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
      }
    }

    th.actions,
    td.actions {
      position: sticky;
      right: 0;
      padding: 0 2px;
      width: 28px;
    }

    td.actions > * {
      opacity: 0;
      transition: opacity 0.15s ease;
    }

    tr:hover td.actions > *,
    td.actions > *:focus-within,
    td.actions > .active {
      opacity: 1;
    }
  }

  .graty-panel-paginator {
    display: flex;
    justify-content: flex-end;
    margin-top: 8px;
  }

  @media (hover: none) {
    .graty-panel-table td.actions > * {
      opacity: 1;
    }
  }
}
</style>
