<script setup lang="ts">
import {
  $notify,
  BaseDialog,
  type Errors,
  ButtonBusy,
  type IssueFieldInfoDto,
  SortableSelect
} from "luna";
import { ref, useTemplateRef } from "vue";
import type { ComponentExposed } from "vue-component-type-helpers";
import { $i18n } from "@/utils/i18n.ts";
import type { PanelRenderDto, PanelRenderRequest, PanelRenderValueDto } from "@/interfaces/render.ts";
import { adminRenderService } from "@/service/adminRenderService.ts";

const emits = defineEmits({
  created: (_panel: PanelRenderDto) => true,
  updated: (_panel: PanelRenderDto) => true,
});

const busy = ref(false);
const renderId = ref<number>(null);
const request = ref<Partial<PanelRenderRequest>>({});
const dialog = useTemplateRef<ComponentExposed<typeof BaseDialog>>('dialog');
const errors = ref<Errors>({});
const fields = ref<Array<IssueFieldInfoDto>>([]);

const hide = () => {
  dialog.value.hide();
}

const create = () => {
  request.value = {};
  dialog.value.show();
}

const edit = (renderValue: PanelRenderValueDto) => {
  busy.value = true;
  renderId.value = renderValue.id;
  request.value = {name: renderValue.name};
  dialog.value.show();
  adminRenderService.getById(renderValue.id).then((data) => {
    request.value = {
      name: data.data.name,
      description: data.data.description,
      fields: data.data.fields.map((f) => f.id),
      limit: data.data.limit,
    };
  }).catch(() => {
    $notify.error($i18n.t("Failed load render"));
  }).finally(() => {
    busy.value = false;
  })
}

const submit = () => {
  if (busy.value) {
    return;
  }
  busy.value = true;
  if (renderId.value) {
    adminRenderService.update(renderId.value, request.value).then((data) => {
      emits('updated', data.data);
      hide();
    }).catch((e) => {
      if (e.data?.errors) {
        errors.value = e.data?.errors;
      }
      $notify.error($i18n.t("Failed to update render"));
    }).finally(() => {
      busy.value = false;
    });
  } else {
    adminRenderService.create(request.value).then((data) => {
      emits('created', data.data);
      hide();
    }).catch((e) => {
      if (e.data?.errors) {
        errors.value = e.data?.errors;
      }
      $notify.error($i18n.t("Failed to create render"));
    }).finally(() => {
      busy.value = false;
    });
  }
}


defineExpose({
  create,
  edit
});
</script>

<template>
  <BaseDialog ref="dialog" :busy="busy" class="large">
    <template v-slot:header>
      <template v-if="renderId">{{ $i18n.t("Edit panel render") }}</template>
      <template v-else>{{ $i18n.t("Create panel render") }}</template>
    </template>
    <template v-slot:default>
      <form class="ui pad" @submit.prevent="submit()">
        <div class="field-group">
          <label for="render-name">{{ $i18n.t("Name") }}</label>
          <input id="render-name" class="full-width" type="text" v-model="request.name">
          <div class="error" v-if="errors['name']">{{ errors['name'] }}</div>
        </div>
        <div class="field-group">
          <label for="render-description">{{ $i18n.t("Description") }}</label>
          <textarea id="render-description" class="full-width" v-model="request.description"></textarea>
          <div class="error" v-if="errors['description']">{{ errors['description'] }}</div>
        </div>
        <div class="field-group">
          <label for="render-fields">{{ $i18n.t("Fields") }}</label>
          <SortableSelect id="render-fields" :options="fields" v-model="request.fields"></SortableSelect>
          <div class="error" v-if="errors['fields']">{{ errors['fields'] }}</div>
        </div>
        <div class="field-group">
          <label for="render-limit">{{ $i18n.t("Limit") }}</label>
          <input id="render-limit" type="number" step="1" v-model="request.limit">
          <div class="error" v-if="errors['limit']">{{ errors['limit'] }}</div>
        </div>
      </form>
    </template>
    <template v-slot:footer>
      <div>
        <button type="button" @click="hide()" :disabled="busy">{{ $i18n.t("Cancel") }}</button>
        <ButtonBusy type="button" @click="submit()" :busy="busy" :disabled="busy">
          <template v-if="renderId">{{ $i18n.t("Save") }}</template>
          <template v-else>{{ $i18n.t("Create") }}</template>
        </ButtonBusy>
      </div>
    </template>
  </BaseDialog>
</template>

<style>

</style>