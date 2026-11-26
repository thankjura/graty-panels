import axios from "axios";
import { $bus, type DropDownGroupOption, type DropDownOption, type IssueDto } from "luna";
import { $i18n } from "@/utils/i18n.ts";

interface IssueViewWorkflowAction {
  name: string,
  actionId: number,
  screenId: number | null,
  targetStatusId: number,
}

interface WebNode {
  id: number,
  name: string,
  routeName?: string,
  eventName?: string,
  routeParams?: Record<string, string>,
  routeQuery?: Record<string, string>,
  iconUrl?: string,
  iconTitle?: string,
  hint?: string,
  children?: Array<WebNode>,
  section?: boolean,
}

interface IssueMenuDto {
  issue: IssueDto,
  workflowActions: Array<IssueViewWorkflowAction>,
  actions: Array<WebNode>,
}

const CREATE_ISSUE_EVENT = "create_issue";
const CREATE_ISSUE_FIELD_PREFIX = "field.";

const createIssueParams = (params?: Record<string, string>) => {
  const values: Record<string, unknown> = {};
  for (const [key, value] of Object.entries(params ?? {})) {
    if (key.startsWith(CREATE_ISSUE_FIELD_PREFIX)) {
      values[key.substring(CREATE_ISSUE_FIELD_PREFIX.length)] = value;
    }
  }
  const issueType = params?.issueType ? Number(params.issueType) : undefined;
  return {
    project: params?.project || undefined,
    issueType: Number.isFinite(issueType) ? issueType : undefined,
    values,
  };
};

const issueActionEvents: Record<string, (issue: IssueDto, webNode: WebNode, initiator: string, fields?: Array<string>) => void> = {
  assign_issue: (issue, _node, initiator, fields) => $bus.emit('action.assign_issue', {issue, fields, initiator}),
  create_worklog: (issue) => $bus.emit('action.create_worklog', {issueKey: issue.key}),
  create_link: (issue, _node, initiator) => $bus.emit('action.create_link', {issueKey: issue.key, initiator}),
  create_subtask: (issue, node, initiator) => $bus.emit('action.create_subtask', {
    issueKey: issue.key,
    issueType: createIssueParams(node.routeParams).issueType,
    initiator,
  }),
  move_issue: (issue, _node, initiator) => $bus.emit('action.move_issue', {issue, initiator}),
  delete_issue: (issue, _node, initiator) => $bus.emit('action.delete_issue', {issue, initiator}),
  edit_issue: (issue, _node, initiator, fields) => $bus.emit('action.edit_issue', {issueKey: issue.key, fields, initiator}),
  [CREATE_ISSUE_EVENT]: (_issue, node, initiator) => $bus.emit('action.create_issue', {...createIssueParams(node.routeParams), initiator}),
};

const nodeIcon = (webNode: WebNode) => webNode.iconUrl?.startsWith('icon-')
  ? {iconName: webNode.iconUrl, iconTitle: webNode.iconTitle}
  : {iconUrl: webNode.iconUrl, iconTitle: webNode.iconTitle};

const getDropDownOption = (issue: IssueDto, webNode: WebNode, initiator: string, fields?: Array<string>): DropDownOption | null => {
  if (webNode.routeName) {
    return {
      id: webNode.id,
      label: webNode.name,
      route: {name: webNode.routeName, params: webNode.routeParams, query: webNode.routeQuery},
      ...nodeIcon(webNode),
    };
  }
  if (webNode.eventName && issueActionEvents[webNode.eventName]) {
    const handler = issueActionEvents[webNode.eventName];
    return {
      id: webNode.id,
      label: webNode.name,
      cb() {
        handler(issue, webNode, initiator, fields);
      },
      ...nodeIcon(webNode),
    };
  }
  if (webNode.eventName) {
    return {
      id: webNode.id,
      label: webNode.name,
      cb() {
        $bus.emit("action.custom", {
          name: webNode.eventName,
          issue,
          initiator,
          params: webNode.routeParams ?? {},
        });
      },
      ...nodeIcon(webNode),
    };
  }
  if (webNode.children && webNode.children.length > 0) {
    const options: Array<DropDownOption> = [];
    for (const child of webNode.children) {
      const opt = getDropDownOption(issue, child, initiator, fields);
      if (opt) {
        options.push(opt);
      }
    }
    return {
      id: webNode.id,
      label: webNode.name,
      children: [{id: webNode.id, options}],
    };
  }
  return null;
};

const issueActionGroups = (issue: IssueDto, webNodes: Array<WebNode>, initiator: string, fields?: Array<string>): Array<DropDownGroupOption> => {
  const out: Array<DropDownGroupOption> = [];
  let loose: DropDownGroupOption | null = null;

  for (const webNode of webNodes ?? []) {
    if (webNode.section) {
      loose = null;
      const options = (webNode.children ?? [])
        .map((child) => getDropDownOption(issue, child, initiator, fields))
        .filter((opt): opt is DropDownOption => !!opt);
      if (options.length > 0) {
        out.push({id: webNode.id, label: webNode.name || undefined, options});
      }
      continue;
    }

    const opt = getDropDownOption(issue, webNode, initiator, fields);
    if (!opt) {
      continue;
    }
    if (!loose) {
      loose = {id: `${webNode.id}-group`, options: []};
      out.push(loose);
    }
    loose.options.push(opt);
  }

  return out;
};

/** Builds the issue context actions menu (transitions, edit, and Luna issue/actions items), mirroring Luna's IssueLinksPanel. */
export const issueDropDownOptions = async (
  issueKey: string,
  fields: Array<string>,
  initiator: string,
): Promise<Array<DropDownGroupOption>> => {
  const options: Array<DropDownGroupOption> = [];
  const { data } = await axios.get<IssueMenuDto>(`/rest/issue_meta/view/${encodeURIComponent(issueKey)}/menu`);

  if (data.workflowActions && data.workflowActions.length > 0) {
    const actions: Array<DropDownOption> = data.workflowActions.map((action) => ({
      id: action.actionId,
      label: action.name,
      cb() {
        $bus.emit('action.transit_issue', {
          issueKey,
          actionId: action.actionId,
          showDialog: !!action.screenId,
          fields,
          initiator,
        });
      },
    }));

    options.push({
      id: 'transitions',
      label: $i18n.t("Transitions:"),
      options: actions,
    });
  }

  const groups = issueActionGroups(data.issue, data.actions, initiator, fields);
  if (data.issue?.perms?.includes("edit")) {
    const edit: DropDownOption = {
      id: 'edit',
      label: $i18n.t("Edit"),
      cb() {
        $bus.emit('action.edit_issue', {issueKey, fields, initiator});
      },
      iconName: 'icon-pencil',
    };
    if (groups.length > 0 && !groups[0].label) {
      groups[0] = {...groups[0], options: [edit, ...groups[0].options]};
    } else {
      groups.unshift({id: 'edit', options: [edit]});
    }
  }
  options.push(...groups);

  return options;
};
