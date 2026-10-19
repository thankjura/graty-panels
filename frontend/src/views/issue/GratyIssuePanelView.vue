<script setup lang="ts">
import { inject, onMounted } from "vue";
import { Injections } from "luna";
import { issuePanelService } from "@/service/issuePanelService.ts";

const issueKey = inject(Injections.issueKey);

onMounted(() => {
  console.log(issueKey.value);
  issuePanelService.getPanelsForIssue(issueKey.value).then((panels) => {
    console.log(panels);
  })
})

</script>

<template>
  <div>
    Hello world!
  </div>
</template>

<style>

</style>