import type { FieldValue } from "luna";
import type { PanelFieldDto, PanelRenderColumnDto } from "@/interfaces/render.ts";

export interface GratyIssueDto {
  id: number,
  key: string,
  [fieldId: string]: FieldValue | number | string,
}

export interface IssuePanelDto {
  id: number,
  name: string,
  fields: Array<PanelFieldDto>,
  columns: Array<PanelRenderColumnDto>,
  showActions: boolean,
  showHeader: boolean,
  issues: Array<GratyIssueDto>,
  limit: number,
  total: number | null,
}
