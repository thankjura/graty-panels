// Luna exposes version: 4.0.0-SNAPSHOT
declare module 'luna' {
  import type { DefineComponent, SlotsType, InjectionKey, Ref } from 'vue';
  import type { LocationQueryRaw, RouteLocationRaw } from 'vue-router';
  import type { SortableEvent } from 'sortablejs';
  import { AxiosResponse } from "axios";
  import { ComputedRef } from "@vue/reactivity";

  type T = any;
  type K = any;

  export const ColorStrategy: {
    readonly Priority: "priority";
    readonly Query: "queryString";
  };
  export const StatusCategoryKey: {
    readonly TODO: "todo";
    readonly IN_PROGRESS: "in_progress";
    readonly DONE: "done";
  };
  export const ISSUE_PERMS: [
    'edit',
    'assign',
    'transit',
    'move',
    'delete',
    'create_attachments',
    'create_comments',
    'create_worklogs',
    'create_issues',
    'view_watchers',
    'manage_watchers',
    'browse_workflow',
    'edit_all_comments',
    'edit_own_comments',
    'delete_all_comments',
    'delete_own_comments',
    'delete_all_attachments',
    'delete_own_attachments',
    'edit_all_worklogs',
    'edit_own_worklogs',
    'delete_all_worklogs',
    'delete_own_worklogs',
  ];
  export const AssignStrategy: {
    readonly LEAD: "lead";
    readonly AUTHOR: "author";
  };
  export const SprintState: {
    readonly NEW: "new";
    readonly ACTIVE: "active";
    readonly COMPLETE: "complete";
  };
  export const IconType: {
    readonly ISSUE_TYPE: "issuetypes";
    readonly PRIORITY: "priorities";
    readonly PROJECT: "projects";
    readonly AVATAR: "users";
  };
  export const IconFormat: {
    readonly SVG: "svg";
    readonly PNG: "png";
  };
  export const IconSize: {
    readonly ORIGINAL: "original";
    readonly SMALL: "32x32";
    readonly MEDIUM: "64x64";
    readonly LARGE: "128x128";
    readonly X_LARGE: "256x256";
  };
  export const BackupPhase: {
    readonly CALC: "calc";
    readonly BACKUP: "backup";
    readonly DONE: "done";
    readonly FAIL: "fail";
    readonly CANCELED: "canceled";
  };
  export const IndexingPhase: {
    readonly CALC: "calc";
    readonly INDEXING: "indexing";
    readonly DONE: "done";
    readonly FAIL: "fail";
  };
  export const LicenceType: {
    readonly DEVELOPMENT: "development";
    readonly COMMERCIAL: "commercial";
    readonly TRIAL: "trial";
  };
  export const RankingPhase: {
    readonly CALC: "calc";
    readonly RANKING: "ranking";
    readonly DONE: "done";
  };
  export const ProjectImportPhase: {
    readonly IDLE: "idle";
    readonly ANALYZING: "analyzing";
    readonly VALIDATING: "validating";
    readonly IMPORTING: "importing";
  };
  export const BusyIconComponent: DefineComponent<
    {},{}, {}, {}, {}, {}, {}, {}, string, {}, {}, {}, SlotsType<{}>>;
  export const ButtonBusy: DefineComponent<
    {},{}, {}, {}, {}, {}, {}, {}, string, {
      busy?: boolean | undefined
      disabled?: boolean | undefined
      type?: "button" | "submit" | "reset" | undefined
    }, {}, {}, SlotsType<{
      default: any,
    }>>;
  export const LoadOverlayComponent: DefineComponent<
    {},{}, {}, {}, {}, {}, {}, {}, string, {
      absolute?: boolean | undefined
      dim?: boolean | undefined
    }, {}, {}, SlotsType<{}>>;
  export const PagePaginator: DefineComponent<
    {},{}, {}, {}, {}, {}, {}, {
      'setPage': [_page: number]
    }, 'setPage', {
      busy?: boolean | undefined
      compact?: boolean | undefined
      page?: number | undefined
      maxItems?: number | undefined
      limit?: number | undefined
      total: number
    }, {}, {}, SlotsType<{}>>;
  export const PopoverComponent: DefineComponent<
    {},{
      toggle: (event: MouseEvent, target?: HTMLElement | undefined) => void
      show: (event: MouseEvent, target?: HTMLElement | undefined) => void
      hide: () => void
      active: boolean
    }, {}, {}, {}, {}, {}, {
      'hide': []
    }, 'hide', {
      closeButton?: boolean | undefined
      prefferedAlign?: ALIGN[] | undefined
      group?: string | undefined
    }, {}, {}, SlotsType<{
      default: any,
    }>>;
  export const StatusComponent: DefineComponent<
    {},{}, {}, {}, {}, {}, {}, {}, string, {
      name?: string | undefined
      category?: string | undefined
    }, {}, {}, SlotsType<{}>>;
  export const TogglePanel: DefineComponent<
    {},{}, {}, {}, {}, {}, {}, {
      'toggle': [_state: boolean]
      'show': []
    }, 'toggle' | 'show', {
      hideActions?: boolean | undefined
      initState?: boolean | undefined
      title?: string | undefined
      storeKey?: string | undefined
    }, {}, {}, SlotsType<{
      actions: any,
      default: any,
    }>>;
  export const UserAvatarComponent: DefineComponent<
    {},{}, {}, {}, {}, {}, {}, {}, string, {
      size?: any
      user: { id?: string | number | undefined; displayName?: string | undefined; login?: string | undefined; iconUrl?: string | undefined; }
    }, {}, {}, SlotsType<{}>>;
  export const UserLinkComponent: DefineComponent<
    {},{}, {}, {}, {}, {}, {}, {}, string, {
      label?: string | undefined
      user?: any
    }, {}, {}, SlotsType<{}>>;
  export const BaseDialog: DefineComponent<
    {},{
      show: () => void
      hide: () => void
    }, {}, {}, {}, {}, {}, {
      'closed': []
    }, 'closed', {
      busy?: boolean | undefined
      showClose?: boolean | undefined
    }, {}, {}, SlotsType<{
      header: any,
      default: any,
      footer: any,
    }>>;
  export const ButtonsGroupComponent: DefineComponent<
    {},{}, {}, {}, {}, {}, {}, {}, string, {
      max?: number | undefined
      moreLabel?: string | undefined
      maxWidth?: number | undefined
      zIndex?: number | undefined
      group?: string | undefined
      options: DropDownGroupOption[]
    }, {}, {}, SlotsType<{}>>;
  export const DropDownButton: DefineComponent<
    {},{
      show: () => Promise<void>
      hide: () => void
      active: boolean
    }, {}, {}, {}, {}, {}, {}, string, {
      maxWidth?: number | undefined
      zIndex?: number | "auto" | undefined
      disabled?: boolean | undefined
      busy?: boolean | undefined
      toggleIcon?: boolean | undefined
      fixed?: boolean | undefined
      align?: "auto" | "left" | "right" | undefined
      cacheOptions?: boolean | undefined
      offset?: { x: number; y: number; } | undefined
      group?: string | undefined
      options: DropDownGroupOption[] | OptionsGetterSync | OptionsGetterAsync
      layerClass?: string | undefined
    }, {}, {}, SlotsType<{
      default: any,
    }>>;
  export const DropDownLayer: DefineComponent<
    {},{}, {}, {}, {}, {}, {}, {
      'clicked': [_option: DropDownOption, _event: MouseEvent]
    }, 'clicked', {
      maxWidth?: number | undefined
      zIndex?: number | undefined
      busy?: boolean | undefined
      fixed?: boolean | undefined
      align?: "auto" | "left" | "right" | undefined
      prefferedAlign?: "left" | "right" | undefined
      options?: DropDownGroupOption[] | undefined
      parentPosition?: any
    }, {}, {}, SlotsType<{}>>;
  export const DropDownLayerItem: DefineComponent<
    {},{}, {}, {}, {}, {}, {}, {}, string, {
      option?: any
    }, {}, {}, SlotsType<{}>>;
  export const CheckBoxesComponent: DefineComponent<
    {},{}, {}, {}, {}, {}, {}, {
      'select': [value: T]
      'remove': [value: string | number]
      'update:modelValue': [value: (string | number)[] | undefined]
    }, 'select' | 'remove' | 'update:modelValue', {
      optionGroups: OptionsGroup<T>[]
      valueKey?: keyof T | undefined
      disabled?: boolean | undefined
      busyValue?: string | number | undefined
      emitsOnly?: boolean | undefined
      maxHeight?: string | undefined
      modelValue?: (string | number)[] | undefined
    }, {}, {}, SlotsType<{}>>;
  export const ColorPickerDialog: DefineComponent<
    {},{
      show: (colorValue: string, elementValue: HTMLElement) => void
    }, {}, {}, {}, {}, {}, {
      'update': [_color: string]
    }, 'update', {}, {}, {}, SlotsType<{}>>;
  export const ColorPickerInput: DefineComponent<
    {},{}, {}, {}, {}, {}, {}, {
      'update:modelValue': [value: string | undefined]
    }, 'update:modelValue', {
      modelValue?: string | undefined
    }, {}, {}, SlotsType<{
      default: any,
    }>>;
  export const ColorPickerLayer: DefineComponent<
    {},{
      layer: HTMLDivElement | null
    }, {}, {}, {}, {}, {}, {
      'update:modelValue': [value: any]
    }, 'update:modelValue', {
      modelValue?: any
      field?: HTMLElement | undefined
      origValue?: any
    }, {}, {}, SlotsType<{
      default: any,
      footer: any,
    }>>;
  export const DateTimePicker: DefineComponent<
    {},{}, {}, {}, {}, {}, {}, {
      'update:modelValue': [value: string | undefined]
    }, 'update:modelValue', {
      withTime?: boolean | undefined
      disabled?: boolean | undefined
      modelValue?: string | undefined
      placeholder?: string | undefined
    }, {}, {}, SlotsType<{}>>;
  export const IconSelector: DefineComponent<
    {},{}, {}, {}, {}, {}, {}, {
      'update:modelValue': [value: string | undefined]
    }, 'update:modelValue', {
      modelValue?: string | undefined
      type: IconType
    }, {}, {}, SlotsType<{}>>;
  export const MultiSelect: DefineComponent<
    {},{}, {}, {}, {}, {}, {}, {
      'select': [value: T]
      'remove': [value: T[K]]
      'create': [value: string]
      'update:modelValue': [value: T[K][] | undefined]
    }, 'select' | 'remove' | 'create' | 'update:modelValue', {
      options: any
      modelData?: T[] | undefined
      showIcons?: boolean | undefined
      minTagLen?: number | undefined
      suggestionKey?: keyof T | undefined
      tagLabel?: ((term: string) => string) | undefined
      labelKey?: keyof T | undefined
      valueKey?: (keyof T & K) | undefined
      id?: string | undefined
      cropSuggestions?: boolean | undefined
      iconRadius?: string | undefined
      missingIconLiteralKey?: keyof T | undefined
      modelValue?: T[K][] | undefined
    }, {}, {}, SlotsType<{}>>;
  export const QsInput: DefineComponent<
    {},{}, {}, {}, {}, {}, {}, {
      'update:modelValue': [value: string | undefined]
      'update:error': [value: string | undefined]
    }, 'update:modelValue' | 'update:error', {
      disabled?: boolean | undefined
      maxResult?: number | undefined
      modelValue?: string | undefined
      error?: string | undefined
      initFunc?: (() => Promise<QsFormData>) | undefined
    }, {}, {}, SlotsType<{}>>;
  export const QsViewer: DefineComponent<
    {},{}, {}, {}, {}, {}, {}, {}, string, {
      qs?: string | undefined
    }, {}, {}, SlotsType<{}>>;
  export const SingleSelect: DefineComponent<
    {},{}, {}, {}, {}, {}, {}, {
      'select': [value: T | null]
      'create': [value: string]
      'update:modelValue': [value: T[K] | undefined]
    }, 'select' | 'create' | 'update:modelValue', {
      options: any
      modelData?: T | undefined
      showIcons?: boolean | undefined
      minTagLen?: number | undefined
      tagLabel?: ((term: string) => string) | undefined
      suggestionKey?: keyof T | undefined
      valueKey?: (keyof T & K) | undefined
      labelKey?: keyof T | ((item: T) => string) | undefined
      name?: string | undefined
      cropSuggestions?: boolean | undefined
      disabled?: boolean | undefined
      iconRadius?: string | undefined
      missingIconLiteralKey?: keyof T | undefined
      placeholder?: string | undefined
      nullValueName?: string | undefined
      enableClear?: boolean | undefined
      suggestionsWidth?: string | undefined
      id?: string | undefined
      modelValue?: T[K] | undefined
    }, {}, {}, SlotsType<{}>>;
  export const SliderTrack: DefineComponent<
    {},{}, {}, {}, {}, {}, {}, {
      'update:modelValue': [value: number | undefined]
    }, 'update:modelValue', {
      min?: number | undefined
      max?: number | undefined
      step?: number | undefined
      modelValue?: number | undefined
    }, {}, {}, SlotsType<{}>>;
  export const SortableList: DefineComponent<
    {},{}, {}, {}, {}, {}, {}, {
      'moved': [optionId: T["id"], prevOptionId: T["id"]]
      'deleted': [option: T]
      'update': [value: T[] | null]
      'update:modelValue': [value: T[] | undefined]
    }, 'moved' | 'deleted' | 'update' | 'update:modelValue', {
      modelValue: T[]
      showIcons?: boolean | undefined
      removable?: boolean | undefined
      emitsOnly?: boolean | undefined
      busy?: boolean | undefined
    }, {}, {}, SlotsType<{}>>;
  export const SortableSelect: DefineComponent<
    {},{}, {}, {}, {}, {}, {}, {
      'update': [_value: T[] | null]
      'update:modelValue': [value: T[] | undefined]
    }, 'update' | 'update:modelValue', {
      showIcons?: boolean | undefined
      options: any
      modelValue?: T[] | undefined
    }, {}, {}, SlotsType<{}>>;
  export const SuggestionIconComponent: DefineComponent<
    {},{}, {}, {}, {}, {}, {}, {}, string, {
      option: T
      missingIconLiteralKey: keyof T
      iconRadius?: string | undefined
    }, {}, {}, SlotsType<{}>>;
  export const SuggestionsComponent: DefineComponent<
    {},{}, {}, {}, {}, {}, {}, {
      'select': [value: T]
      'hide': []
      'create': [value: string]
    }, 'select' | 'hide' | 'create', {
      suggestions: T[]
      showIcons?: boolean | undefined
      term?: string | undefined
      field?: HTMLElement | undefined
      tag?: string | null | undefined
      valueKey: keyof T
      suggestionKey?: keyof T | ((item: T) => string) | undefined
      cropSuggestions: boolean
      iconRadius?: string | undefined
      missingIconLiteralKey?: keyof T | undefined
      width?: string | undefined
      tagLabel?: ((term: string) => string) | undefined
    }, {}, {}, SlotsType<{}>>;
  export const MarkdownEditor: DefineComponent<
    {},{}, {}, {}, {}, {}, {}, {
      'update:modelValue': [value: string | undefined]
    }, 'update:modelValue', {
      modelValue?: string | undefined
    }, {}, {}, SlotsType<{}>>;
  export const MarkdownViewer: DefineComponent<
    {},{}, {}, {}, {}, {}, {}, {}, string, {
      value?: string | undefined
    }, {}, {}, SlotsType<{}>>;
  export const ProgressBar: DefineComponent<
    {},{}, {}, {}, {}, {}, {}, {}, string, {
      busy?: boolean | undefined
      value?: number | undefined
      total?: number | undefined
    }, {}, {}, SlotsType<{}>>;
  export const ProgressComponent: DefineComponent<
    {},{}, {}, {}, {}, {}, {}, {}, string, {
      value: number
      total: number
    }, {}, {}, SlotsType<{}>>;
  export const ImageViewer: DefineComponent<
    {},{
      show: (imagesData: ImageItem[], idx?: number | undefined) => void
    }, {}, {}, {}, {}, {}, {}, string, {}, {}, {}, SlotsType<{}>>;
  export const CopyToBufferButton: DefineComponent<
    {},{}, {}, {}, {}, {}, {}, {}, string, {
      value?: string | undefined
    }, {}, {}, SlotsType<{}>>;
  export const ToggleComponent: DefineComponent<
    {},{}, {}, {}, {}, {}, {}, {
      'update:modelValue': [value: boolean | undefined]
    }, 'update:modelValue', {
      disabled?: boolean | undefined
      busy?: boolean | undefined
      manual?: boolean | undefined
      modelValue?: boolean | undefined
      id?: string | undefined
    }, {}, {}, SlotsType<{}>>;
  export const SortableTable: DefineComponent<
    {},{}, {}, {}, {}, {}, {}, {
      'updated': [_event: SortableEvent]
      'changed': [_event: SortableEvent]
    }, 'updated' | 'changed', {
      group?: string | undefined
    }, {}, {}, SlotsType<{
      header: any,
      default: any,
      footer: any,
    }>>;
  export const $i18n: I18N & {new(supportedLocales: Array<string>, loader: (locale: string) => Promise<Record<string, string>>): I18N}
  export const $notify: NotifyComponentInterface
  export const projectService: ProjectService
  export const issueTypeService: IssueTypeService
  export const queryService: QueryService
  export const RouteNames: {
        home: "home",
        dashboard: "dashboard",
        navigator: "navigator",
        bulkAction: "bulkAction",
        bulkActionProgress: "bulkActionProgress",
        projects: "projects",
        project: {
            project: "project",
            summary: "projectSummary",
            issues: "projectIssues",
            activity: "projectActivity",
            reports: "projectReports",
            settings: {
                settings: "projectSettings",
                summary: "projectSettingsSummary",
                details: "projectSettingsDetails",
                schemas: "projectSettingsSchemas",
                users: "projectSettingsUsers",
                permissions: "projectSettingsPermissions",
                issueTypes: "projectSettingsIssueTypes",
                notifications: "projectSettingsNotifications",
                priorities: "projectSettingsPriorities",
                indexing: "projectSettingsIndexing",
                delete: "projectSettingsDelete",
            },
        },
        login: "login",
        logout: "logout",
        profile: "profile",
        searchFilters: "searchFilters",
        issue: "issue",
        agile: {
            boards: "agileBoards",
            view: "agileBoardView",
            backlog: "agileBoardBacklog",
            reports: "agileBoardReports",
            settings: "agileBoardSettings",
        },
        admin: {
            admin: "admin",
            users: {
                users: "adminUsers",
                list: "adminUserList",
                user: "adminUser",
                roles: "adminUserRoles",
            },
            groups: "adminGroups",
            issueTypes: "adminIssueTypes",
            issueTypeSchemas: "adminIssueTypeSchemas",
            priorities: "adminPriorities",
            prioritySchemas: "adminPrioritySchemas",
            statuses: "adminStatuses",
            workflows: "adminWorkflows",
            workflowSchemas: "adminWorkflowSchemas",
            projects: "adminProjects",
            archivedProjects: "adminArchivedProjects",
            projectRoles: "adminProjectRoles",
            customFields: "adminCustomFields",
            screens: "adminScreens",
            screenSchemas: "adminScreenSchemas",
            issueTypeScreenSchemas: "adminIssueTypeScreenSchemas",
            permissionSchemas: "adminPermissionSchemas",
            outgoingMailServers: "adminOutgoingMailServers",
            outgoingMailQueue: "adminOutgoingMailQueue",
            incomingMailServers: "adminIncomingMailServers",
            messageHandlers: "adminMessageHandlers",
            notificationSchemas: "adminNotificationSchemas",
            fieldLayouts: "adminFieldLayouts",
            fieldLayoutSchemas: "adminFieldLayoutSchemas",
            globalPermissions: "adminGlobalPermissions",
            properties: "adminProperties",
            systemInfo: "adminSystemInfo",
            plugins: "adminPlugins",
            scheduler: "adminScheduler",
            issueLinkTypes: "adminIssueLinkTypes",
            directories: "adminDirectories",
            indexing: "adminIndexing",
            backup: "adminBackup",
            projectImport: "adminProjectImport",
            logging: "adminLogging",
            ranking: "adminRanking",
            docs: "adminDocs",
            license: "adminLicense",
            icons: "adminIcons",
            eventTypes: "adminEventTypes",
            mailTester: "adminMailTester",
            cluster: "adminCluster",
        },
        maintenance: "maintenance",
    }
  export const Injections: {
    issueKey: InjectionKey<Ref<string>>,
    project: InjectionKey<Ref<ProjectDto>>,
    board: InjectionKey<Ref<BoardDto>>,
    selectedIssues: InjectionKey<ComputedRef<Array<IssueDto>>>,
    workflowAction: InjectionKey<Ref<WorkflowActionDto>>,
    workflowFuncType: InjectionKey<Ref<WorkflowFunctionType>>,
    workflowActionFunc: InjectionKey<Ref<WorkflowFunctionDescriptorDto>>,  
  }
  export const $bus: Emitter<BusEvents>
  export interface NotifyComponentInterface {
      info: (title: string, body?: string, closable?: boolean) => void;
      warn: (title: string, body?: string, closable?: boolean) => void;
      ok: (title: string, body?: string, closable?: boolean) => void;
      error: (title: string, body?: string, closable?: boolean) => void;
  }
  export interface I18N {
      loadMessages(locale: string): Promise<void>,
      supportedLocales: Array<string>,
      t(text: string, ...args: Array<string|number>): string,
      p(text: string, params: Record<string, any>, ...args: Array<string|number>): string,
  }
  export interface Errors {
      [key: string]: string
  }
  export interface Emitter<Events extends Record<EventType, unknown>> {
      all: EventHandlerMap<Events>;
      on<Key extends keyof Events>(type: Key, handler: Handler<Events[Key]>): void;
      on(type: '*', handler: WildcardHandler<Events>): void;
      off<Key extends keyof Events>(type: Key, handler?: Handler<Events[Key]>): void;
      off(type: '*', handler: WildcardHandler<Events>): void;
      emit<Key extends keyof Events>(type: Key, event: Events[Key]): void;
      emit<Key extends keyof Events>(type: undefined extends Events[Key] ? Key : never): void;
  }
  export type EventType = string | symbol;
  export type EventHandlerMap<Events extends Record<EventType, unknown>> = Map<keyof Events | '*', EventHandlerList<Events[keyof Events]> | WildCardEventHandlerList<Events>>;
  export type EventHandlerList<T = unknown> = Array<Handler<T>>;
  export type Handler<T = unknown> = (event: T) => void;
  export type WildCardEventHandlerList<T = Record<string, unknown>> = Array<WildcardHandler<T>>;
  export type WildcardHandler<T = Record<string, unknown>> = (type: keyof T, event: T[keyof T]) => void;
  export type BusEvents = {
      'issue.updated': {issue: IssueDto, fields?: Array<string>, initiator?: string},
      'issue.created': {issue: IssueDto, initiator?: string},
      'issue.deleted': {issue: IssueDto},
      'issue.worklog.created': {worklog: WorklogDto},
      'issue.worklog.updated': {worklog: WorklogDto},
      'issue.attachment.added': {issueKey: string, attachments: Array<AttachmentDto>, initiator: string},
      'action.create_comment': {issue: IssueDto},
      'action.create_link': {issueKey: string, initiator?: string},
      'action.move_issue': {issue: IssueDto, initiator?: string},
      'action.edit_issue': {issueKey: string, fields?: Array<string>, initiator?: string},
      'action.assign_issue': {issue: IssueDto, fields?: Array<string>, initiator?: string},
      'action.delete_issue': {issue: IssueDto, initiator?: string},
      'action.transit_issue': {issueKey: string, actionId: number, showDialog: boolean, fields?: Array<string>, initiator?: string, cb?: (issue: IssueDto) => void},
      'action.edit_worklog': {issueKey: string, worklog: WorklogDto},
      'action.create_worklog': {issueKey: string},
      'action.edit_field': {issueKey: string, fieldId: string},
      'action.custom': {name: string, issue?: IssueDto, initiator?: string, params: Record<string, string>},
      // workflow
      'workflow.edit_condition': {condGroup: WorkflowActionConditionGroup},
      'workflow.remove_condition': {condGroup: WorkflowActionConditionGroup},
      'workflow.add_condition': {condGroup: WorkflowActionConditionGroup},
      // user
      'user.login.required': {resolve?: (user: UserProfileDto) => void, reject?: () => void},
      'user.self.updated': {user: UserProfileDto},
      // size
      'site.theme.switched': {theme: 'dark'|'light'}
  
      // project
      'project.updated': {project: ProjectWithSchemas},
  
      // filters
      'search_filter.deleted': {searchFilterId: number}
  
      // popovers
      'popover.show': {event: MouseEvent, group: string, target?: Element, id: string},
  
      // panels
      'panel.event': {event: MouseEvent, name: string, panelId: string, actionId: string}
  }
  export interface IssueDto {
      id: number,
      key: string,
      summary?: string,
      issueType?: IssueTypeValueDto,
      project?: ProjectValueDto,
      parent?: IssueValueDto,
      assignee?: UserValueDto,
      author?: UserValueDto,
      priority?: PriorityValueDto,
      estimate?: string,
      timeLeft?: string,
      timeSpent?: string,
      estimateValue?: number,
      timeLeftValue?: number,
      timeSpentValue?: number,
      status?: StatusValueDto,
      statusCategory?: StatusCategory,
      description?: string,
      created?: string,
      updated?: string,
      sprint?: SprintValueDto,
      rank?: string,
      labels?: Array<string>,
      watchers?: WatchersInfo,
      perms?: Array<IssuePermKey>,
      color?: string,
      [customField: `f${number}`]: FieldValue
  }
  export interface IssueTypeValueDto {
      id: number,
      name: string,
      subtask?: boolean,
      iconUrl: string,
  }
  export interface ProjectValueDto {
      id: number,
      key: string,
      name: string,
      iconUrl: string,
  }
  export interface IssueValueDto {
      id: number,
      key: string,
      summary: string,
      iconTypeUrl: string,
  }
  export interface UserValueDto {
      id: number,
      login: string,
      displayName: string,
      email: string,
      iconUrl: string,
      active: boolean,
      name: string,
  }
  export interface PriorityValueDto {
      id: number,
      name: string,
      description: string,
      iconUrl: string,
  }
  export interface StatusValueDto {
      id: number,
      name: string,
      categoryKey: StatusCategoryKey,
  }
  export type StatusCategoryKey = typeof StatusCategoryKey[keyof typeof StatusCategoryKey];
  export interface StatusCategory {
      key: StatusCategoryKey,
      name: string,
  }
  export interface SprintValueDto {
      id: number,
      name: string,
      state: SprintState,
  }
  export type SprintState = typeof SprintState[keyof typeof SprintState];
  export interface WatchersInfo {
      count: number,
  }
  export type IssuePermKey = typeof ISSUE_PERMS[number];
  export type FieldValue = string | null | undefined | object | Array<string>;
  export interface WorklogDto {
      id: number,
      author: UserValueDto,
      created: string
      updater: UserValueDto|null,
      updated: string|null,
      startDate: string,
      timeSpent: string,
      comment: string|null,
  }
  export interface AttachmentDto {
      id: number,
      author: UserInfoDto,
      created: string,
      fileName: string,
      fileUrl: string,
      fileSize: number,
      mimeType: string,
      thumbName: string,
      thumbUrl: string,
  }
  export interface UserInfoDto extends UserValueDto {
      gender: Gender,
  }
  export type Gender = 'male' | 'female';
  export interface WorkflowActionConditionGroup {
      id: string,
      condition: WorkflowActionFunction | null,
      items: Array<WorkflowActionConditionGroup> | null,
      operator: ConditionOperator,
  }
  export interface WorkflowActionFunction extends WorkflowFunctionDescriptorDto {
      id: string,
      params: null | WorkflowFunctionParams,
      viewParams: null | WorkflowFunctionViewParams,
  }
  export interface WorkflowFunctionParams {
      [key: string]: string
  }
  export interface WorkflowFunctionViewParams {
      [key: string]: any
  }
  export type ConditionOperator = "and" | "or";
  export interface UserProfileDto extends UserInfoDto {
      activity: UserActivityDto,
      favorites: UserFavorites,
      admin: boolean,
      name: string,
      lastName: string,
      locale: string,
      dashboardId: number,
      external: boolean,
  }
  export interface UserActivityDto {
      navigatorQs: string | null,
      navigatorFilterId: number | null,
      lastVisitedIssuesIds: Array<number>,
      lastVisitedProjectIds: Array<number>,
      navigatorColumns: Array<IssueFieldInfoDto>,
      lastVisitedFilters: Array<SearchFilterValueDto>,
      lastVisitedBoardIds: Array<number>,
  }
  export interface IssueFieldInfoDto {
      id: keyof IssueDto,
      name: string,
      description: string,
      systemField: boolean,
      viewComponent?: string,
      navigatorComponent?: string,
      formComponent?: string,
  }
  export interface SearchFilterValueDto {
      id: number,
      name: string,
      queryString: string,
  }
  export interface UserFavorites {
      userId: number,
      filterIds: Array<number>,
      boardIds: Array<number>,
  }
  export interface ProjectWithSchemas extends ProjectDto {
      fieldLayoutSchema: FieldLayoutSchemaDto | null,
      issueTypeSchema: IssueTypeSchemaDto | null,
      issueTypeScreenSchema: IssueTypeScreenSchemaDto | null,
      prioritySchema: PrioritySchemaDto | null,
      workflowSchema: WorkflowSchemaDto | null,
      permissionSchema: PermissionSchemaValueDto | null,
      notificationSchema: NotificationSchemaValueDto | null,
  }
  export interface FieldLayoutSchemaDto extends FieldLayoutValueDto {
      description: string,
      defaultLayout: FieldLayoutValueDto,
      entries: Array<FieldLayoutSchemaEntryDto>
  }
  export interface FieldLayoutValueDto {
      id: number,
      name: string,
      description: string,
  }
  export interface FieldLayoutSchemaEntryDto {
      issueType: IssueTypeValueDto,
      fieldLayout: FieldLayoutValueDto,
  }
  export interface IssueTypeSchemaDto extends IssueTypeScreenSchemaValueDto {
      description?: string,
      issueTypes: Array<IssueTypeDto>,
  }
  export interface IssueTypeDto extends IssueTypeValueDto {
      iconPath: string,
      description?: string
  }
  export interface IssueTypeScreenSchemaDto extends IssueTypeScreenSchemaValueDto {
      description?: string | null,
      defaultScreenSchema: ScreenSchemaValueDto,
      entries: {
          [key: number]: ScreenSchemaValueDto
      },
      assignedIssueTypesMap: {[key: number]: IssueTypeValueDto}
  }
  export interface ScreenSchemaValueDto {
      id: number,
      name: string,
  }
  export interface PrioritySchemaDto extends PrioritySchemaValueDto {
      description?: string,
      priorities: Array<PriorityDto>,
  }
  export interface PriorityDto extends PriorityValueDto {
      iconPath?: string,
      position: number
  }
  export interface WorkflowSchemaDto extends WorkflowSchemaValueDto {
      defaultWorkflow: WorkflowValueDto,
      entries: Array<WorkflowSchemaEntryDto>,
      active: boolean,
  }
  export interface WorkflowValueDto {
      id: number,
      name: string,
      description: string|null,
      originalId: number|null,
  }
  export interface WorkflowSchemaEntryDto {
      issueType: IssueTypeValueDto,
      workflow: WorkflowValueDto,
  }
  export interface PermissionSchemaValueDto {
      id: number,
      name: string,
      description: string|null,
  }
  export interface NotificationSchemaValueDto {
      id: number,
      name: string,
      description: string|null,
  }
  export type ALIGN = 'top' | 'left' | 'bottom' | 'right';
  export interface DropDownGroupOption {
      id: ID,
      label?: string;
      options: Array<DropDownOption>;
      className?: string,
  }
  export type ID = string|number;
  export interface DropDownOption {
      id: ID,
      label: string;
      iconUrl?: string;
      iconTitle?: string;
      iconName?: string;
      route?: RouteLocationRaw,
      href?: string;
      cb?: (option: DropDownOption, event: MouseEvent) => boolean|undefined|void;
      children?: Array<DropDownGroupOption>;
      childrenLayerClass?: string;
      className?: string,
      title?: string,
      selected?: boolean,
  }
  export type OptionsGetterSync = () => DropDownGroupOption[]
  export type OptionsGetterAsync = () => Promise<DropDownGroupOption[]>
  export interface OptionsGroup<T extends BaseOption> {
      id: ID,
      label?: string,
      options: Array<T>
  }
  export interface BaseOption {
      //name: string;
      [key: string]: any;
  }
  export type IconType = typeof IconType[keyof typeof IconType];
  export interface QsFormData {
      searchers: Array<QuerySearcherDto>,
      sorters: Array<QuerySorterDto>,
      functions: Array<QueryFunctionDto>,
  }
  export interface QuerySearcherDto {
      id: string,
      name: string,
      nameQs: string,
      valueType: string,
      allowedOperators: Array<QueryOperator>,
      supportBasicSearch: boolean,
  }
  export type QueryOperator = '=' | '!=' | '~' | '!~' | 'is' | 'not is' | '>' | '>=' | '<' | '<=';
  export interface QuerySorterDto {
      id: string,
      name: string,
      label: string,
  }
  export interface QueryFunctionDto {
      functionName: string,
      value: string,
      list: boolean,
      valueType: string
  }
  export interface ImageItem {
    id: number,
    name: string,
    description?: string,
    imageUrl: string,
    thumbUrl?: string,
  }
  export interface ActivityIssueDto {
      id: number,
      key: string,
      summary: string,
      projectKey: string,
      issueType: IssueTypeValueDto
  }
  export interface ActivityCommentDto {
      id: number,
      text: string,
  }
  export interface ActivityAttachmentDto {
      id: number,
      thumbUrl: string,
      fileName: string,
      fileUrl: string,
      fileSize: number,
      mimeType: number,
  }
  export interface ActivityWorklogDto {
      startDate: string,
      timeSpent: string,
      comment: string,
  }
  export interface ActivityStreamSearchQuery extends SearchQuery {
      projectKey?: string,
      user?: string,
  }
  export interface ActivityStreamItemDto {
      issue: ActivityIssueDto,
      comment?: ActivityCommentDto,
      worklog?: ActivityWorklogDto,
      changeItems?: Array<ChangeItemDto>
      attachments?: Array<ActivityAttachmentDto>
      date: string
  }
  export interface ChangeItemDto {
      id: number,
      fieldId: string,
      fieldName: string,
      oldStringValue: string,
      newStringValue: string,
  }
  export interface ActivityStreamItemGrouped {
      groupedUser: UserInfoDto
      items: Array<ActivityStreamItemDto>
  }
  export type ColorStrategy = typeof ColorStrategy[keyof typeof ColorStrategy];
  export type BoardType = 'kanban'|'scrum';
  export interface BoardValueDto {
      id: number,
      name: string,
      type: BoardType,
  }
  export interface BoardInfoDto extends BoardValueDto, SharedObjectDto {
      description: string,
      author: UserInfoDto,
  }
  export interface BoardColumnDto {
      id: number,
      name: string,
      statusIds: Array<number>,
  }
  export interface BoardFilterDto {
      id: number,
      name: string,
      description: string,
      queryString: string,
  }
  export interface CardColorDto {
      id: number,
      value: string,
      color: string,
  }
  export interface CardColorPresetDto {
      strategy: string,
      colors: Array<CardColorDto>,
  }
  export interface BoardDto extends BoardInfoDto {
      filterId: number,
      columns: Array<BoardColumnDto>,
      filters: Array<BoardFilterDto>,
      rankField: IssueFieldInfoDto,
      cardColorStrategy?: ColorStrategy,
      cardColorPreset?: CardColorPresetDto,
      cardExtraFieldShowNames: boolean,
      cardExtraFieldIds: Array<string>,
      cardExtraFields: Array<IssueFieldInfoDto>,
      detailViewFieldIds: Array<string>,
      detailViewFields: Array<IssueFieldInfoDto>,
      doneLimitDays: number,
      activeSprints: Array<SprintInfoDto>,
  }
  export interface SprintInfoDto extends SprintValueDto {
      goal: string,
      startDate: string,
      displayName: string,
      finishDate: string,
      boardId: number,
  }
  export interface EditableBoard extends Partial<BoardDto> {
      rankFieldId?: string,
  }
  export interface BacklogDto {
      backlogIssues: SearchResult<IssueDto>,
      sprintIssues: Array<IssueDto>,
  }
  export interface SearchResult<T> {
      total: number,
      results: Array<T>,
      page: number,
      limit: number,
  }
  export interface BoardSearchQuery extends SearchQuery {
      type?: BoardType;
      author?: Array<string>;
      favorite?: 1|0,
  }
  export interface SprintReportData {
      sprint: SprintValueDto,
      board: BoardValueDto,
      issuesMap: Record<string, IssueDto>,
      chartData: Array<ChartData>,
      statusCategoryMap: Record<string, StatusCategory>,
  }
  export interface ChartData {
      value: string,
      chartValues: Array<ChartValue>,
  }
  export interface ChartValue {
      dateValue: number,
      date: string,
      count: number,
  }
  export interface SprintDto extends SprintInfoDto {
      author: UserInfoDto,
      createdDate: string,
      activatedDate: string,
      completedDate: string,
      active: boolean,
      completed: boolean,
  }
  export interface LoginResponse {
      user?: UserProfileDto,
      error?: string
  }
  export type RequireFields<T, K extends keyof T> = T & Required<Pick<T, K>>;
  export interface DashboardValueDto {
      id: number,
      name: string,
      author: UserInfoDto,
      widgetsCount: number,
  }
  export interface DashboardDto extends DashboardValueDto {
      description: string,
      sharedForGroups: Array<string>,
      sharedForUserIds: Array<number>,
      widgets: Array<DashboardWidget>,
  }
  export interface DashboardWidget extends WidgetPosition {
      id: number,
      widgetDescriptor: DashboardWidgetDescriptor,
      params: WidgetParams,
  }
  export interface DashboardWidgetDescriptor {
      key: string,
      name: string,
      description: string,
      previewImageUrl: string,
      viewComponent: string,
      formComponent: string,
  }
  export type WidgetParams = {[key: string]: string}
  export interface WidgetPosition {
      columns: [number, number],
      rows: [number, number],
  }
  export interface DeleteResult {
      count: number
  }
  export interface ResponseError {
      status: number,
      reason: string,
      errors: Errors
  }
  export interface MessageSet {
      errorMessages: Array<string>
      warningMessages: Array<string>
  }
  export interface IssueEventType {
      id: number,
      name: string,
      description: string | null,
      baseEventTypeId: number,
      systemEvent: boolean,
  }
  export type IconFormat = typeof IconFormat[keyof typeof IconFormat];
  export type IconSize = typeof IconSize[keyof typeof IconSize];
  export interface IconKey {
      key: string,
      type: IconType,
      format: IconFormat,
      path: string,
  }
  export interface Icon {
      id: number,
      key: IconKey,
      fileName: string,
      path: string,
      url: string,
  }
  export interface BulkActionDescriptor {
    key: string,
    name: string,
    description: string,
    paramsComponent: string,
    confirmComponent: string,
  }
  export interface BulkActionProgressDto {
    taskId: string,
    actionKey: string,
    actionName: string,
    actionDescription: string,
    initiator: UserInfoDto,
    startDate: string,
    finishDate?: string,
    progress: number,
    progressMessage: string,
    active: boolean,
    duration: number,
  }
  export interface BulkActionRequest {
    actionKey: string,
    actionParams: any,
    issueKeys: Array<string>,
  }
  export interface BulkActionValidationResult {
    errors: Errors,
    data: any,
    valid: boolean,
  }
  export interface CommentDto {
      id: number,
      author: UserValueDto,
      created: string,
      updated?: string,
      updater?: UserValueDto,
      text: string
  }
  export interface ExportModuleDto {
      key: string,
      name: string,
      webComponent: string,
  }
  export interface ExportParams {
      moduleKey: string,
      limit: number,
      fields: Array<string>,
      qs: string,
      extraParams?: {[key: string]: string},
  }
  export interface IssueFieldContextDto {
      id: number,
      name: string,
      description: string,
      projectIds: Array<number>,
      issueTypeIds: Array<number>,
      options: Array<Option<number>>
  }
  export interface Option<T extends ID> {
      id: T,
      name: string,
      iconUrl?: string,
      altName?: string,
  }
  export interface IssueFieldContextResolvedDto extends IssueFieldContextDto {
      projects: Array<ProjectInfoDto>,
      issueTypes: Array<IssueTypeDto>,
      defaultViewValue?: FieldValue,
      defaultFormValue?: FieldValue,
      formParams: any,
  }
  export interface ProjectInfoDto extends ProjectValueDto {
      description?: string,
      archived?: boolean,
      archivedDate?: string,
  }
  export interface IssueFieldInfoTypeDto extends IssueFieldInfoDto {
      fieldTypeKey: string,
      fieldTypeName: string,
      searcherName: string,
      valid: boolean,
  }
  export interface IssueFieldDto extends IssueFieldInfoTypeDto {
      formComponents?: Array<string>,
      viewComponents?: Array<string>,
      navigatorComponents?: Array<string>,
      optionsComponent?: string,
      searcherKey: string
      searcherName: string,
      valid: boolean,
  }
  export interface IssueFieldResolvedDto extends IssueFieldDto {
      contexts: Array<IssueFieldContextResolvedDto>,
  }
  export interface FieldTypeDescriptorDto {
      key: string,
      name: string,
      description?: string,
      iconUrl?: string,
      navigatorComponents?: Array<string>,
      viewComponents?: Array<string>,
      formComponents?: Array<string>,
  }
  export interface FieldSearcherDescriptorDto {
      key: string,
      name: string,
      description?: string,
  }
  export interface SearchFilterDto extends SharedObjectDto, SearchFilterValueDto {
      description: string,
      author: UserInfoDto,
      created: string,
      updated?: string,
  }
  export interface SearchFilterQueryParams extends SearchQuery {
      author?: Array<string>,
      favorite?: 0|1,
  }
  export interface IssueFormFieldDto extends IssueFieldInfoDto {
    value: FieldRawValue,
    params: FieldParams,
    required: boolean,
  }
  export type FieldRawValue = string | null | undefined | number | object | Array<string> | Array<number>;
  export type FieldParams = Array<any> | Object;
  export interface IssueFormTabDto {
    id: number,
    name: string,
    fields: Array<IssueFormFieldDto>,
  }
  export interface IssueFormDto {
    id: number,
    key: string,
    project: ProjectValueDto,
    tabs: Array<IssueFormTabDto>,
    commentRequired: boolean,
    showComment: boolean,
  }
  export interface IssueTransitionFormDto extends IssueFormDto {
    actionName: string,
  }
  export interface IssueMoveFormDto {
    fields: Array<IssueFormFieldDto>
  }
  export interface ChangeGroupDto {
      id: number,
      author: UserInfoDto,
      created: string,
      changeItems: {[key: string]: ChangeItemDto},
  }
  export interface ChangeItem {
      id: number,
      fieldId: string,
      fieldName: string,
      oldStringValue: string,
      newStringValue: string,
  }
  export interface EditableIssueDto extends Record<string, any>{
      issueType?: Partial<IssueTypeDto> | number,
      project?: Partial<ProjectInfoDto> | number | string,
      assignee?: Partial<UserValueDto> | number | string,
      author?: Partial<UserValueDto> | number | string,
      priority?: Partial<PriorityValueDto> | number,
      sprint?: Partial<SprintValueDto> | number,
      comment?: string,
      [customField: `f${number}`]: FieldRawValue,
  }
  export interface IssueTypeSchemaValueDto {
      id: number,
      name: string,
  }
  export interface IssueTypeSchemaWithIdsDto extends IssueTypeSchemaValueDto {
      id: number,
      name: string,
      issueTypeIds: Array<number>
  }
  export interface Label {
      id: number,
      name: string,
  }
  export interface PrioritySchemaValueDto {
      id: number,
      name: string,
  }
  export interface StatusDto extends StatusValueDto {
      description: string,
      category: StatusCategory
  }
  export interface IssueSearchResultDto extends SearchResult<IssueDto> {
      fields: Array<IssueFieldInfoDto>
  }
  export interface FieldLayoutItemDto {
      fieldId: string,
      field: IssueFieldInfoTypeDto,
      required: boolean,
  }
  export interface FieldLayoutDto extends FieldLayoutValueDto {
      description: string,
      fieldLayoutItems: Array<FieldLayoutItemDto>
  }
  export interface FieldLayoutSchemaValueDto {
      id: number,
      name: string,
  }
  export interface FieldLayoutSchemaRequest {
      id: number,
      name: string,
      description: string,
      defaultLayoutId: number,
  }
  export interface IssueLinkTypeDto {
      id: number,
      name: string,
      outgoingName: string,
      incomingName: string,
  }
  export interface IssueLinkDto {
      id: number,
      issueLinkType: IssueLinkTypeDto,
      sourceIssue: IssueDto,
      targetIssue: IssueDto,
      created: string,
  }
  export interface IssueLinkRequest {
      issueLinkTypeId: string,
      sourceIssueKeys?: Array<String>
      targetIssueKeys?: Array<String>
  }
  export interface QueryParseError {
      line: number,
      position: number,
      message: string
  }
  export interface ParseResult {
      valid: boolean,
      errorMessage: string | null,
      errors: Array<QueryParseError>
  }
  export interface ClauseValue {
      value: string,
      displayValue: string,
  }
  export interface SortRepresentationDto {
      id: string,
      name: string,
      asc: boolean,
      label: string,
  }
  export interface SearcherRepresentation {
      id: string,
      name: string,
      qsFragment: string,
      viewLabel: string,
      formComponent: string,
      formValue: any,
  }
  export interface QueryRepresentation {
      valid: boolean,
      searchers: Array<SearcherRepresentation>,
      sorters: Array<SortRepresentationDto>,
  }
  export interface TimeSheerReportRequest {
      projectOrFilter: string,
      startDate?: string,
      finishDate?: string,
  }
  export interface TimeSheetIssueRow {
      issue: IssueDto,
      values: Record<string, string>
      total: string,
  }
  export interface TimeSheetCol {
      id: string,
      name: string,
      title: string,
      iconUrl: string,
  }
  export interface TimeSheetIssueReportDate {
      rows: Array<TimeSheetIssueRow>;
      columns: Array<TimeSheetCol>;
      totalTimeSpent: string;
      totals: Record<string, string>;
  }
  export interface StatisticsGroup {
      id: string,
      label: string,
      iconUrl?: string,
      count: number,
      qs: string
  }
  export interface StatisticsGroupResultDto {
      keys: Array<string>,
      groups: Array<StatisticsGroup>,
      issueField: IssueFieldInfoDto,
      totalCount: number,
  }
  export interface AffectedSchemasDto {
    issueTypeSchemas: Array<IssueTypeSchemaValueDto>
    workflowSchemas: Array<WorkflowSchemaValueDto>
    issueTypeScreenSchemas: Array<IssueTypeScreenSchemaValueDto>
    fieldLayoutSchemas: Array<FieldLayoutSchemaValueDto>
  }
  export interface WorkflowSchemaValueDto {
      id: number,
      name: string,
      description: string|null,
      workflowIds: Array<number>,
  }
  export interface IssueTypeScreenSchemaValueDto {
      id: number,
      name: string,
  }
  export interface ScreenTabDto {
      id: number,
      name: string,
      fieldIds: Array<string>,
      fields: Array<IssueFieldInfoTypeDto>,
  }
  export interface ScreenValueDto {
      id: number,
      name: string,
      description: string,
  }
  export interface ScreenDto extends ScreenValueDto {
      tabs: Array<ScreenTabDto>
  }
  export interface ScreenSchemaDto extends ScreenSchemaValueDto{
      description: string|null,
      defaultScreen: ScreenValueDto,
      createScreen?: ScreenValueDto,
      editScreen?: ScreenValueDto,
      viewScreen?: ScreenValueDto,
  }
  export interface EditableScreenSchemaDto {
      name: string,
      description: string|null,
      defaultScreenId: number,
      createScreenId: number,
      editScreenId: number,
      viewScreenId: number,
  }
  export interface EditableIssueTypeScreenSchemaDto {
      name: string,
      description: string,
      defaultScreenSchemaId: number,
  }
  export interface IssueViewWorkflowAction {
    name: string,
    actionId: number,
    screenId: number|null,
    targetStatusId: number,
  }
  export interface IssueViewSubtaskType {
    id: number,
    name: string,
    iconUrl: string,
  }
  export interface IssueViewTabDto {
    id: number,
    name: string,
    fields: Array<IssueFieldInfoDto>
  }
  export interface IssueViewDto {
    issue: RequireFields<IssueDto, "priority" | "summary" | "project" | "issueType" | "watchers" | "perms">,
    workflowActions: Array<IssueViewWorkflowAction>,
    customActions: Array<WebNode>,
    editableFields: Array<string>,
    subtaskTypes: Array<IssueViewSubtaskType>,
    tabs: Array<IssueViewTabDto>
    userFields: Array<IssueFieldInfoDto>,
    dateFields: Array<IssueFieldInfoDto>,
    systemFields: Array<IssueFieldInfoDto>,
    watching: boolean,
    mainPanels: Array<WebPanelDto>,
    advancedPanels: Array<WebPanelDto>,
  }
  export interface WebNode {
      id: number,
      name: string,
      routeName: string,
      eventName: string,
      routeParams: {[key: string]: string},
      routeQuery: {[key: string]: string},
      iconUrl: string,
      iconTitle: string,
      hint: string,
      children: Array<WebNode>,
      section: boolean,
  }
  export interface WebPanelDto {
      id: string,
      name: string,
      actions: Array<WebPanelAction>,
      webComponent: string,
  }
  export interface WebPanelAction {
      id: string,
      name: string,
      eventName: string,
      hint: string,
      className: string,
  }
  export interface IssueViewWatchersDto {
    count: number,
    watching: boolean,
  }
  export interface WorkflowFunctionDescriptorDto {
      name: string,
      description: string,
      className: string,
      allowMultiple: boolean,
      viewComponent: string | null,
      formComponent: string | null,
  }
  export type WorkflowFunctionType = 'postfunction' | 'validator' | 'condition';
  export interface WorkflowActionValueDto {
      id: number,
      name: string,
      screenId: number|null,
  }
  export interface WorkflowActionDto extends WorkflowActionValueDto {
      sourceStatusIds: Array<number>,
      targetStatusId: number,
      screen: ScreenValueDto|null,
      validators: Array<WorkflowActionFunction>,
      conditions: WorkflowActionConditionGroup,
      postfunctions: Array<WorkflowActionFunction>,
  }
  export interface WorkflowValueActionsDto extends WorkflowValueDto {
      actions: Array<WorkflowActionValueDto>
  }
  export interface WorkflowValueStatusesDto extends WorkflowValueDto {
      statuses: Array<StatusValueDto>
  }
  export interface WorkflowDto extends WorkflowValueDto {
      statuses: Array<StatusValueDto>,
      display: {
          statusPositionMap: Record<number, Position>,
          canvasPosition: Position,
          actionPorts: Record<string, {left: NodePort, right: NodePort}>
      },
      createAction: WorkflowActionDto,
      actions: Array<WorkflowActionDto>,
      author: UserInfoDto | null,
      updater: UserInfoDto | null,
      created: string | null,
      updated: string | null,
  }
  export interface Position {
      x: number,
      y: number,
  }
  export interface NodePort {
      nodeId: number,
      direction: Direction|null,
      index: number,
  }
  export type Direction = 'left' | 'right' | 'top' | 'bottom';
  export interface EditableWorkflowSchemaDto {
      name: string,
      description: string|null,
      defaultWorkflowId: number,
  }
  export interface WorkflowSearchResultDto extends SearchResult<WorkflowValueDto> {
      drafts: Array<WorkflowValueDto>,
      schemas: Array<WorkflowSchemaValueDto>,
  }
  export interface WorkflowViewResponse {
      nodes: Array<NodeModel>,
      links: Array<NodeLink>,
  }
  export interface NodeModel {
      id: number,
      position: Position,
      name: string,
      className?: string,
      startNode: boolean,
      leftPorts: number,
      topPorts: number,
      rightPorts: number,
      bottomPorts: number,
  }
  export interface NodeLink {
      id: string,
      name: string,
      source: NodePort,
      target: NodePort,
  }
  export interface DirectoryValueDto {
      id: number,
      name: string,
      description: string,
      position: number,
      active: boolean,
      syncable: boolean,
      directoryTypeKey: string,
  }
  export interface DirectoryDto extends DirectoryValueDto {
      attributes: DirectoryAttributes,
      directoryType: DirectoryTypeDto,
  }
  export type DirectoryAttributes = {[key: string]: string};
  export interface DirectoryTypeDto {
      key: string,
      name: string,
      description: string,
      webComponent: string,
      syncable: boolean,
  }
  export type DirectorySyncStatus = 'PENDING' | 'ERROR' | 'RUNNING';
  export interface SyncableDirectory extends DirectoryValueDto {
      intervalMinutes: number,
      nextSyncTime: string,
      lastSyncTime: string,
      syncStatus: DirectorySyncStatus,
      syncProgressPercent: number,
      enabled: boolean,
      lastSyncDurationMillis: number,
      syncErrorMessage: string,
      syncStartTime: string,
  }
  export interface DirectorySyncCheckResult {
      success: boolean,
      message: string,
  }
  export interface Group {
      id: number,
      name: string,
  }
  export interface IncludedBy {
      user: boolean,
      groups: Array<string>,
  }
  export interface ProjectRoleMap {
      projectId: number;
      projectName: string;
      projectKey: string;
      rolesMap: Record<number, IncludedBy>
  }
  export interface UserProjectRoles {
      user: UserInfoDto,
      projectRoles: Array<ProjectRole>,
      projectRoleMaps: Array<ProjectRoleMap>,
  }
  export interface ProjectRole {
      id: number,
      name: string,
      description?: string
  }
  export interface UserDto extends UserInfoDto {
      directoryId: number,
      name: string
      lastName?: string | null
      groups?: Array<string>,
      locale: string,
      external: boolean,
  }
  export interface UserActivityRequest {
      navigatorColumnIds: Array<string>,
  }
  export interface UserFavoritesRequest {
      addFilterIds: Array<number>,
      removeFilterIds: Array<number>,
      addBoardIds: Array<number>,
      removeBoardIds: Array<number>,
  }
  export interface PortPosition extends Position {
      direction?: Direction|null,
      index: number,
  }
  export interface Curve {
      id: string,
      sx: number,
      sy: number,
      sd?: Direction|null,
      ex: number,
      ey: number,
      ed?: Direction|null,
  }
  export interface WorkflowLink extends NodeLink {
      actionId: number,
  }
  export interface ProgressStep {
      id: string,
      name: string,
      description?: string,
  }
  export type OptionsFunc<T extends BaseOption, K extends keyof T = 'id'> = (term: string | null, selected: Array<T[K]>) => Promise<Array<T>>;
  export type AssignStrategy = typeof AssignStrategy[keyof typeof AssignStrategy];
  export interface ProjectValueWithIssueTypesDto extends ProjectValueDto {
      issueTypes: Array<IssueTypeValueDto>
  }
  export interface ProjectDto extends ProjectInfoDto {
      lead: UserValueDto,
      created: string,
      iconPath?: string,
      assignStrategy: AssignStrategy,
      canAdmin: boolean,
  }
  export interface ProjectSchemasIds {
      issueTypeSchemaId: number | null,
      resolutionSchemaId: number | null,
      issueTypeScreenSchemaId: number | null,
      fieldLayoutSchemaId: number | null,
      prioritySchemaId: number | null,
      workflowSchemaId: number|null,
      permissionSchemaId: number|null,
      notificationSchemaId: number|null,
  }
  export interface ProjectWithSchemaIdsDto extends ProjectDto, ProjectSchemasIds {}
  export interface ProjectMembersDto {
      projectRoles: Array<ProjectRole>,
      usersMap: {
          [key: number]: Array<UserInfoDto>
      },
      groupsMap: {
          [key: number]: Array<string>
      },
  }
  export interface ReportCategory {
      id: string,
      name: string,
  }
  export interface ReportModuleDto {
      key: string;
      name: string;
      category: ReportCategory;
      description: string;
      thumbnailUrl: string;
      webComponent: string;
      weight: number;
  }
  export interface SearchResultWithSchemas<T, S> extends SearchResult<T> {
    schemas: Array<S>
  }
  export interface ScreenSearchResult extends SearchResultWithSchemas<ScreenValueDto, ScreenSchemaDto> {
      workflows: Array<WorkflowValueActionsDto>
  }
  export interface SearchQuery extends LocationQueryRaw {
      term?: string,
      page?: number,
      limit?: number,
  }
  export interface ProjectSearchQuery extends SearchQuery {
      state?: 'active' | 'archived' | 'any'
  }
  export interface ScreenSearchQuery extends SearchQuery {
      fieldIds?: Array<keyof IssueDto>
  }
  export interface UserSearchQuery extends SearchQuery {
      directoryIds?: Array<number>,
      groups?: Array<string>
  }
  export interface IssueSearchQuery extends SearchQuery {
      filter?: number,
      qs?: string,
      fields?: Array<string>,
      updateActivity?: 1|0,
  }
  export interface SearchQueryExt {
      term?: string,
      page?: number,
      limit?: number,
      excludes?: Array<string|number>,
      [key: string]: number|string|boolean|Array<string|number>|undefined,
  }
  export interface ProjectOrFilter {
      filterId?: string,
      projectKey?: string
  }
  export interface ProjectService {
    create: (key: string | null, name: string | null, description?: string | null, iconPath?: string | null) => Promise<AxiosResponse<ProjectWithSchemas>>
    patch: (key: string, data: Partial<ProjectWithSchemaIdsDto> & {leadId?: number}) => Promise<AxiosResponse<ProjectWithSchemas>>
    get: (keyOrId: string|number) => Promise<AxiosResponse<ProjectDto>>
    getWithSchemas: (key: string) => Promise<AxiosResponse<ProjectWithSchemas>>
    getAll: () => Promise<AxiosResponse<Array<ProjectValueDto>>>
    search: (params: ProjectSearchQuery, excludes?: Array<string|number>, isAdmin?: boolean) => Promise<AxiosResponse<SearchResult<ProjectInfoDto>>>
    delete: (key: string) => Promise<AxiosResponse<DeleteResult>>
    getMembers: (key: string) => Promise<AxiosResponse<ProjectMembersDto>>
    addMembers: (key: string, roles: Array<number>, groups: Array<string>, users: Array<string>) => Promise<AxiosResponse<ProjectMembersDto>>
    removeUser: (key: string, role: number, userName: string) => Promise<AxiosResponse<ProjectMembersDto>>
    removeGroup: (key: string, role: number, groupName: string) => Promise<AxiosResponse<ProjectMembersDto>>
    removeRole: (key: string, role: number) => Promise<AxiosResponse<ProjectMembersDto>>
  }
  export interface IssueTypeService {
    create: (name: string | null, description: string | null, subtask: boolean, iconPath?: string | null) => Promise<AxiosResponse<IssueTypeDto>>
    update: (id: number, name: string | null, description?: string | null, iconPath?: string | null) => Promise<AxiosResponse<IssueTypeDto>>
    getById: (id: number) => Promise<AxiosResponse<IssueTypeDto>>
    search: (params: SearchQuery, excludes?: Array<number>) => Promise<AxiosResponse<SearchResult<IssueTypeDto>>>
    getAll: () => Promise<AxiosResponse<Array<IssueTypeDto>>>
    delete: (id: number) => Promise<AxiosResponse<DeleteResult>>
    getSchema: (id: number) => Promise<AxiosResponse<IssueTypeSchemaDto>>
    searchSchemas: (params: SearchQuery, excludes?: Array<number>) => Promise<AxiosResponse<SearchResult<IssueTypeSchemaDto>>>
    createSchema: (name?: string | null, description?: string | null, issueTypes?: Array<number>) => Promise<AxiosResponse<IssueTypeSchemaDto>>
    updateSchema: (id: number, name?: string | null, description?: string | null, issueTypes?: Array<number>) => Promise<AxiosResponse<IssueTypeSchemaDto>>
    deleteSchema: (id: number) => Promise<AxiosResponse<DeleteResult>>
  }
  export interface QueryService {
    parseQuery: (qs: string) => Promise<AxiosResponse<ParseResult>>
    getQsData: () => Promise<AxiosResponse<QsFormData>>
    getSuggestions: (fieldName: string, term: string | null, excludes: Array<string>) => Promise<AxiosResponse<Array<ClauseValue>>>
    getSuggestionsForType: (valueType: string, term: string | null, excludes: Array<string>) => Promise<AxiosResponse<Array<ClauseValue>>>
    transformQuery: (qs: string) => Promise<AxiosResponse<QueryRepresentation>>
    transformSearcher: (fieldId: string, params: any) => Promise<AxiosResponse<SearcherRepresentation>>
  }
  export interface ShareEntryDto  {
      users: Array<UserValueDto>;
      userIds: Array<number>;
      groupNames: Array<string>;
  }
  export interface ApplicationProperties {
      baseUrl: string,
      applicationTitle: string,
      defaultLocale: string,
      indexingLocale: string,
  }
  export interface BackupTableStatus {
    tableName: string,
    totalRows: number,
    completedRows: number,
  }
  export type BackupPhase = typeof BackupPhase[keyof typeof BackupPhase];
  export interface BackupStatus extends SystemTaskStatus {
    planningCompleteDate: string,
    phase: BackupPhase,
    totalRows: number,
    completedRows: number,
    filePath: string,
    tableStatus: BackupTableStatus,
    cancelled: boolean,
  }
  export interface BackupStatusResponse {
    status: BackupStatus,
    backupDir: string
  }
  export type ClusterNodeState = "OFFLINE" | "ONLINE";
  export interface ClusterNode {
    id: string,
    state: ClusterNodeState,
    ip: string,
    name: string,
    lastHeartbeatTime: string,
  }
  export interface ClusterState {
    currentNodeId: string,
    nodes: Array<ClusterNode>,
  }
  export interface OpenApiDoc {
    url: string,
    name: string,
  }
  export type IndexingPhase = typeof IndexingPhase[keyof typeof IndexingPhase];
  export interface IndexingStatus {
      id: string,
      started: string,
      finished: string,
      duration: number,
      total: number,
      completed: number,
      failures: number,
      phase: IndexingPhase,
      running: boolean,
      progress: number,
      planningCompleteDate: string,
  }
  export interface IndexingStats {
      indexedIssuesCount: number,
      storedIssuesCount: number,
  }
  export interface SystemInfo {
    groups: Array<SystemInfoGroup>;
  }
  export interface SystemInfoGroup {
    key: string,
    label: string,
    entries: Array<SystemInfoEntry>,
  }
  export interface SystemInfoEntry {
    key: string,
    label: string,
    value: string,
  }
  export type LicenceType = typeof LicenceType[keyof typeof LicenceType];
  export interface LicenseDetails {
    id: string,
    serverId: string,
    app: string,
    type: LicenceType,
    version: number,
    startAt: string,
    expiresAt: string,
    userLimit: number,
    current: boolean,
    expired: boolean,
  
    customerName: string,
    issuedAt: string,
  }
  export interface AppLicenseResponse {
    licenses: Array<LicenseDetails>,
    activeUserCount: number,
  }
  export interface LoggingEntry {
    id: string,
    packageName: string,
    level: string,
  }
  export type MailEncryption = 'start_tls' | 'ssl_tls';
  export interface IncomingMailServer {
      id: number,
      name: string,
      description: string,
      protocol: 'pop3' | 'imap',
      encryption: MailEncryption | null,
      disableCertVerification: boolean,
      timeout: number,
      host: string,
      port: number,
      login: string,
      password: string,
  }
  export interface OutgoingMailServer {
      id: number,
      name: string,
      description: string | null,
      fromEmail: string,
      subjectPrefix: string | null,
      host: string,
      port: number,
      timeout: number,
      encryption: MailEncryption | null,
      disableCertVerification: boolean,
      username: string,
      password?: string,
  }
  export interface OutgoingMailServerRequest extends OutgoingMailServer {
      subject: string,
      to: string,
      body: string,
  }
  export interface Mail {
      subject: string,
      from: string,
      to: Array<string>,
      body: string,
      contentType: string,
  }
  export interface QueueMail extends Mail {
      id: number,
      mailServerId: number,
      created: string,
      state: string,
      errorMessage: string,
  }
  export interface MessageHandlerFactory {
      key: string,
      name: string,
      description: string,
      webComponent: string,
  }
  export interface MessageHandlerModel {
      id: number,
      name: string,
      description: string,
      factory: MessageHandlerFactory,
      factoryKey: string,
      mailServer: IncomingMailServer,
      mailServerId: number,
      handlerParams: Record<string, string>,
      delayMinutes: number,
      folderName: string,
      enabled: boolean,
  }
  export interface NotificationSchemaDto extends NotificationSchemaValueDto {
      notificationsMap: {
          [key: number]: NotificationSchemaEntryDto
      }
  }
  export interface NotificationSchemaEntryDto {
      users: Array<UserInfoDto>,
      projectRoles: Array<ProjectRole>,
      userFields: Array<IssueFieldInfoDto>,
      groupNames: Array<string>,
  }
  export interface MailPreviewDto {
      duration: number,
      eventTypeId: number,
      subject: string,
      html: string,
  }
  export interface Permission {
      id: string,
      name: string,
      description: string|null,
  }
  export interface PermissionSchemaDto extends PermissionSchemaValueDto {
      permissionsMap: Record<string, PermissionSchemaEntry>
  }
  export interface PermissionSchemaEntry {
      userIds: Array<number>,
      groupNames: Array<string>,
      projectRoleIds: Array<number>,
      userFieldIds: Array<string>,
  }
  export interface PermissionSchemaResolvedDto extends PermissionSchemaDto {
      permissionsMap: Record<string, PermissionSchemaEntryResolvedDto>
  }
  export interface PermissionSchemaEntryResolvedDto extends PermissionSchemaEntry {
      users: Array<UserInfoDto>,
      projectRoles: Array<ProjectRole>,
      userFields: Array<IssueFieldInfoDto>,
  }
  export interface GlobalPermissionsDto {
      adminGroups: Array<string>,
      allowedLoginGroups: Array<string>,
      allowedViewUsersGroups: Array<string>,
  }
  export interface SharedObjectDto {
      editAccess: boolean,
      viewAccess: boolean,
      viewPerms: ShareEntryDto,
      editPerms: ShareEntryDto,
  }
  export interface PluginModel {
      key: string,
      name: string,
      description: string,
      version: string,
      vendor: PluginVendor,
      enabled: boolean,
      components: Array<PluginComponent>,
      resources: Array<PluginResource>,
      activeDocs: Array<string>
  }
  export interface PluginVendor {
      name?: string,
      url?: string,
  }
  export interface PluginComponent {
      key: string,
      name: string,
      className: string,
      resources: Array<PluginResource>,
      enabled: boolean,
  }
  export interface PluginResource {
      pluginKey: string,
      key: string,
      type: 'comp'|'css'|'i18n'|'any',
      path: string,
      enabled: boolean,
      resourceUrl: string,
      mount?: 'admin' | 'general',
  }
  export type RankingPhase = typeof RankingPhase[keyof typeof RankingPhase];
  export interface RankingStatusDto extends SystemTaskStatus {
    total: number,
    completed: number,
    phase: RankingPhase,
    planningCompleteDate: string,
    rankField: IssueFieldInfoDto,
  }
  export interface RankingStatsDto {
    issueField: IssueFieldInfoDto,
    missingCount: number,
  }
  export interface BackupMetaProject {
    id: number,
    key: string,
    name: string,
    description: string,
    issueCount: number,
    parseResult?: ProjectParseResult,
  }
  export interface ProjectParseResult {
    startDate: string,
    finishDate: string,
    errors: Record<string, MessageSet>,
    valid: boolean,
    issueCount: number,
    attachmentCount: number,
    commentCount: number,
    watcherCount: number,
    worklogCount: number,
    changeGroupCount: number,
    changeItemCount: number,
  }
  export interface BackupMeta {
    backupName: string,
    backupCRC32: number,
    backupSize: number,
    backupDate: string,
    parseStartDate: string,
    parseFinishDate: string,
    projects: Array<BackupMetaProject>,
  }
  export interface ProjectImportResult {
    startDate: string,
    finishDate: string,
    errors: Record<string, MessageSet>,
    issueCount: number,
    attachmentCount: number,
    commentCount: number,
    watcherCount: number,
    worklogCount: number,
    changeGroupCount: number,
    changeItemCount: number,
  }
  export type ProjectImportPhase = typeof ProjectImportPhase[keyof typeof ProjectImportPhase];
  export interface ProjectImportProgress  {
    percent: number,
    message: string,
  }
  export interface ProjectImportSession {
    backupFile: string,
    backupName: string,
    phaseStartDate: string,
    phaseFinishDate: string,
    phaseProgress: ProjectImportProgress,
    phase: ProjectImportPhase,
    errors: Record<string, string>,
    backupMeta: BackupMeta,
    selectedProjectKey: string,
    projectParseResult?: ProjectParseResult,
    projectImportResult?: ProjectImportResult,
  }
  export interface RestoreInfoResponse {
    importDirectory: string,
  }
  export interface Scheduler {
    cronExpression?: string,
    runOnce: boolean,
    interval?: string,
  }
  export interface JobDetails {
    jobId: string,
    jobRunnerKey: string,
    schedule: Scheduler,
    prevRun: string,
    nextRun: string,
    running: boolean,
  }
  export interface JobRunnerResponse {
    name: string,
    description: string,
    jobRunnerKey: string,
    jobs: Array<JobDetails>,
  }
  export interface JobResult {
    jobId: string,
    jobRunnerKey: string,
    startDate: string,
    executionTime: number,
    result: 'successful' | 'failed' | 'aborted',
    message: string,
    stackTrace: string,
  }
  export interface SystemState {
      ok: boolean,
      state: string,
      progress?: number,
      plannedCompleteDate?: string,
  }
  export interface SystemTaskStatus {
    id: string,
    started: string,
    finished: string,
    duration: number,
    progress: number,
    running: boolean,
  }
  export interface ElemRect {
    x: number,
    y: number,
    width: number,
    height: number
  }
  export interface NamedResponse {
    id: number,
    name: string,
  }
  export interface WebRoute {
      routeName: string,
      routePath: string,
      componentUrl: string,
      children: Array<WebRoute>,
      parent: string,
  }
}

