<script setup lang="ts">
import {
  $notify,
  BaseDialog,
  type Errors,
  MultiSelect,
  ButtonBusy,
  type ProjectValueDto,
  type IssueTypeValueDto
} from "luna";
import type { PanelDto, PanelValueDto } from "@/interfaces/panel.ts";
import { type PropType, ref, useTemplateRef } from "vue";
import type { ComponentExposed } from "vue-component-type-helpers";
import { adminPanelService } from "@/service/adminPanelService.ts";
import { $i18n } from "@/utils/i18n.ts";

defineProps({
  projects: {
    required: true,
    type: Array as PropType<Array<ProjectValueDto>>,
  },
  issueTypes: {
    required: true,
    type: Array as PropType<Array<IssueTypeValueDto>>
  },
})

const emits = defineEmits({
  created: (_panel: PanelDto) => true,
  updated: (_panel: PanelDto) => true,
});

const busy = ref(false);
const panel = ref<Partial<PanelDto>>({});
const dialog = useTemplateRef<ComponentExposed<typeof BaseDialog>>('dialog');
const errors = ref<Errors>({});


const hide = () => {
  dialog.value.hide();
}

const create = () => {
  panel.value = {};
  dialog.value.show();
}

const edit = (panelValue: PanelValueDto) => {
  busy.value = true;
  panel.value = {id: panelValue.id, name: panelValue.name, displayName: panelValue.displayName};
  dialog.value.show();
  adminPanelService.getById(panelValue.id).then((data) => {
    panel.value = data.data;
  }).catch(() => {
    $notify.error($i18n.t("Failed load panel"));
  }).finally(() => {
    busy.value = false;
  })
}

const submit = () => {
  if (busy.value) {
    return;
  }
  busy.value = true;
  if (panel.value?.id) {
    adminPanelService.update(panel.value.id, {
      name: panel.value.name,
      displayName: panel.value.displayName,
      projectIds: panel.value.projectIds,
      issueTypeIds: panel.value.issueTypeIds,
      disabled: panel.value.disabled,
      qs: panel.value.qs,
    }).then((data) => {
      emits('updated', data.data);
      hide();
    }).catch((e) => {
      if (e.data?.errors) {
        errors.value = e.data?.errors;
      }
      $notify.error($i18n.t("Failed to update panel"));
    }).finally(() => {
      busy.value = false;
    });
  } else {
    adminPanelService.create({
      name: panel.value.name,
      displayName: panel.value.displayName,
      projectIds: panel.value.projectIds,
      issueTypeIds: panel.value.issueTypeIds,
      disabled: panel.value.disabled,
      qs: panel.value.qs,
    }).then((data) => {
      emits('created', data.data);
      hide();
    }).catch((e) => {
      if (e.data?.errors) {
        errors.value = e.data?.errors;
      }
      $notify.error($i18n.t("Failed to create panel"));
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
  <BaseDialog ref="dialog" :busy="busy" class="medium">
    <template v-slot:header>
      <template v-if="panel.id">{{ $i18n.t("Edit panel") }}</template>
      <template v-else>{{ $i18n.t("Create panel") }}</template>
    </template>
    <template v-slot:default>
      <form class="ui pad" @submit.prevent="submit()">
        <div class="field-group">
          <label for="panel-name">{{ $i18n.t("Name") }}</label>
          <input id="panel-name" type="text" v-model="panel.name">
          <div class="error" v-if="errors['name']">{{ errors['name'] }}</div>
        </div>
        <div class="field-group">
          <label for="panel-display-name">{{ $i18n.t("Display name") }}</label>
          <input id="panel-display-name" type="text" v-model="panel.displayName">
          <div class="error" v-if="errors['displayName']">{{ errors['displayName'] }}</div>
        </div>
        <div class="field-group">
          <label for="panel-qs">{{ $i18n.t("Query string") }}</label>
          <textarea id="panel-qs" v-model="panel.qs" rows="3"></textarea>
          <div class="error" v-if="errors['qs']">{{ errors['qs'] }}</div>
        </div>
        <div class="field-group">
          <label for="panel-projects">{{ $i18n.t("Projects") }}</label>
          <MultiSelect id="panel-projects" v-model="panel.projectIds" :options="projects" :show-icon="true"></MultiSelect>
          <div class="error" v-if="errors['projects']">{{ errors['projects'] }}</div>
        </div>
        <div class="field-group">
          <label for="panel-issue-types">{{ $i18n.t("Issue types") }}</label>
          <MultiSelect id="panel-issue-types" v-model="panel.issueTypeIds" :options="issueTypes" :show-icon="true"></MultiSelect>
          <div class="error" v-if="errors['issueTypes']">{{ errors['issueTypes'] }}</div>
        </div>
      </form>
    </template>
    <template v-slot:footer>
      <div>
        <button type="button" @click="hide()" :disabled="busy">{{ $i18n.t("Cancel") }}</button>
        <ButtonBusy type="button" @click="submit()" :busy="busy" :disabled="busy">
          <template v-if="panel.id">{{ $i18n.t("Save") }}</template>
          <template v-else>{{ $i18n.t("Create") }}</template>
        </ButtonBusy>
      </div>
    </template>
  </BaseDialog>
</template>

<style>

</style>