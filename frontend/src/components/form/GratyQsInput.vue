<script setup lang="ts">
import { QsInput, type QueryFunctionDto, queryService } from "luna";
import { gratyService } from "@/service/gratyService.ts";

defineProps({
  error: String,
  disabled: Boolean,
});

const value = defineModel<string>();

const getQsFormDate = async () => {
  const results = await Promise.all([queryService.getQsData(), gratyService.getFunctions()]);
  const data = results[0].data;
  const gratyFunctions: Array<QueryFunctionDto> = results[1].data.map(f => {
    return {
      functionName: f.functionName,
      value: f.value,
      list: f.list,
      valueType: f.valueType,
    }
  });
  data.functions.unshift(...gratyFunctions);

  return data;
}

</script>

<template>
  <div class="graty-qs-input-component">
    <QsInput :init-func="getQsFormDate" :error="error" :disabled="disabled" v-model="value"></QsInput>
  </div>

</template>

<style>
  .graty-qs-input-component {
    max-width: 600px!important;

    .qs-input {
      textarea {
        max-width: 600px;
      }
    }
  }
</style>