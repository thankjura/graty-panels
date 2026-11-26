<script setup lang="ts">
import { computed, getCurrentInstance, onBeforeUnmount, onMounted, type PropType, ref, watch } from "vue";
import type { IssuePanelDto, GratyIssueDto } from "@/interfaces/issue.ts";
import type { PanelFieldDto } from "@/interfaces/render.ts";
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
const FIT_FIELDS = new Set(['issueType', 'priority', 'key', 'status', 'statusCategory', 'created', 'updated', 'estimate', 'timeSpent', 'timeLeft']);
const NOWRAP_FIELDS = new Set(['key', 'status', 'statusCategory', 'created', 'updated', 'estimate', 'timeSpent', 'timeLeft']);

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

const fieldIds = computed(() => (props.panel.fields ?? []).map((f) => f.id));

const getColumnName = (field: PanelFieldDto) => {
  if (field.id === 'issueType') {
    return $i18n.t("Type");
  }
  return field.name;
};

const isFitField = (fieldId: string) => FIT_FIELDS.has(fieldId);
const isNowrapField = (fieldId: string) => NOWRAP_FIELDS.has(fieldId);

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
          <thead>
            <tr>
              <th
                v-for="field in panel.fields"
                :key="field.id"
                :class="{
                  fit: isFitField(field.id),
                  nowrap: isNowrapField(field.id),
                  'col-summary': field.id === 'summary',
                }"
              >
                {{ getColumnName(field) }}
              </th>
              <th class="actions fit" v-if="panel.showActions"></th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="issue in issues" :key="issue.id">
              <td
                v-for="field in panel.fields"
                :key="field.id"
                :class="{
                  fit: isFitField(field.id),
                  nowrap: isNowrapField(field.id),
                  'col-summary': field.id === 'summary',
                }"
                :title="field.id === 'summary' && typeof issue[field.id] === 'string' ? issue[field.id] as string : undefined"
              >
                <component
                  v-if="resolveFieldComponent(field)"
                  :is="resolveFieldComponent(field)"
                  :value="issue[field.id]"
                  :field="field"
                />
                <span v-else-if="issue[field.id] != null">{{ issue[field.id] }}</span>
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
    }

    tbody tr td {
      background: transparent;
      vertical-align: middle;
      padding: 4px 6px;
    }

    tbody tr:hover td {
      background: var(--table-highlight-background);
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
