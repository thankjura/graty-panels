<script setup lang="ts">
import {
  $notify,
  BaseDialog,
  ButtonBusy
} from "luna";
import { ref, useTemplateRef } from "vue";
import type { ComponentExposed } from "vue-component-type-helpers";
import { $i18n } from "@/utils/i18n.ts";
import type { PanelRenderValueDto } from "@/interfaces/render.ts";
import { adminRenderService } from "@/service/adminRenderService.ts";

const emits = defineEmits({
  deleted: (_panelId: number) => true,
});

const busy = ref(false);
const render = ref<Partial<PanelRenderValueDto>>({});
const dialog = useTemplateRef<ComponentExposed<typeof BaseDialog>>('dialog');

const hide = () => {
  dialog.value.hide();
}

const show = (renderValue: PanelRenderValueDto) => {
  render.value = renderValue;
  dialog.value.show();
}

const submit = () => {
  if (busy.value) {
    return;
  }
  busy.value = true;
  adminRenderService.delete(render.value.id).then(() => {
    emits('deleted', render.value.id);
    hide();
  }).catch(() => {
    $notify.error($i18n.t("Failed to delete panel render"));
  }).finally(() => {
    busy.value = false;
  });
}

defineExpose({
  show
});
</script>

<template>
  <BaseDialog ref="dialog" :busy="busy" class="medium">
    <template v-slot:header>
      {{ $i18n.t("Delete panel render confirmation") }}
    </template>
    <template v-slot:default>
      <div class="message warning">
        <div class="title no-border">{{ $i18n.t("Are you sure to delete panel render?") }}</div>
      </div>
    </template>
    <template v-slot:footer>
      <div>
        <button type="button" @click="hide()" :disabled="busy">{{ $i18n.t("Cancel") }}</button>
        <ButtonBusy type="button" @click="submit()" :busy="busy" :disabled="busy" class="dangerous">
          {{ $i18n.t("Delete") }}
        </ButtonBusy>
      </div>
    </template>
  </BaseDialog>
</template>

<style>

</style>