<script setup lang="ts">
import {
  $notify,
  BaseDialog,
  ButtonBusy
} from "luna";
import type { PanelDto, PanelValueDto } from "@/interfaces/panel.ts";
import { ref, useTemplateRef } from "vue";
import type { ComponentExposed } from "vue-component-type-helpers";
import { adminPanelService } from "@/service/adminPanelService.ts";
import { $i18n } from "@/utils/i18n.ts";

const emits = defineEmits({
  deleted: (_panelId: number) => true,
});

const busy = ref(false);
const panel = ref<Partial<PanelDto>>({});
const dialog = useTemplateRef<ComponentExposed<typeof BaseDialog>>('dialog');

const hide = () => {
  dialog.value.hide();
}

const show = (panelValue: PanelValueDto) => {
  panel.value = panelValue;
  dialog.value.show();
}

const submit = () => {
  if (busy.value) {
    return;
  }
  busy.value = true;
  adminPanelService.delete(panel.value.id).then(() => {
    emits('deleted', panel.value.id);
    hide();
  }).catch(() => {
    $notify.error($i18n.t("Failed to delete panel"));
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
      {{ $i18n.t("Delete panel confirmation") }}
    </template>
    <template v-slot:default>
      <div class="message warning">
        <div class="title no-border">{{ $i18n.t("Are you sure to delete graty panel?") }}</div>
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