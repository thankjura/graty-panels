import type { FieldValue } from "luna";
import type { PanelFieldDto } from "@/interfaces/render.ts";

export interface GratyIssueDto {
  id: number,
  key: string,
  [fieldId: string]: FieldValue | number | string,
}

export interface IssuePanelDto {
  id: number,
  name: string,
  fields: Array<PanelFieldDto>,
  showActions: boolean,
  issues: Array<GratyIssueDto>,
  limit: number,
  total: number | null,
}
