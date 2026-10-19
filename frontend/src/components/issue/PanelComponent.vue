<script setup lang="ts">

import { onMounted, type PropType, ref } from "vue";
import type { IssuePanelDto, GratyIssueDto } from "@/interfaces/issue.ts";
import { issuePanelService } from "@/service/issuePanelService.ts";
import { $i18n } from "@/utils/i18n.ts";
import { RouteNames, StatusComponent, PagePaginator } from "luna";

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

const issues = ref<Array<GratyIssueDto>>([]);
const total = ref(0);
const limit = ref(10);
const page = ref(1);

const search = (nextPage: number) => {
  nextPage = nextPage ?? page.value;
  issuePanelService.searchIssues(props.panel.id, props.issueKey, {page: nextPage, limit: limit.value}).then((data) => {
    issues.value = data.data.results;
    total.value = data.data.total;
    limit.value = data.data.limit;
    page.value = data.data.page;
  });
}

onMounted(() => {
  issues.value = props.panel.issues;
  if (issues.value.length == 0) {
    search(1);
  }
});
</script>

<template>
  <div class="panel-component">
    <table>
      <thead>
        <tr>
          <th>{{ $i18n.t("Key") }}</th>
          <th>{{ $i18n.t("Summary") }}</th>
          <th>{{ $i18n.t("Status") }}</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="issue in issues" :key="issue.id">
          <td>
            <div class="iconed">
              <router-link :to="{name: RouteNames.issue, params: {issue: issue.key}}">{{ issue.key }}</router-link>
              <img alt="" :src="issue.priority.iconUrl" v-if="issue.priority" :title="issue.priority.name" />
            </div>
          </td>
          <td>
            {{ issue.summary }}
          </td>
          <td>
            <StatusComponent :name="issue.status.name" :category="issue.status.categoryKey" />
          </td>
        </tr>
      </tbody>
    </table>
    <PagePaginator :page="page" :total="total" :limit="limit" @set-page="search" />
  </div>
</template>

<style>

</style>