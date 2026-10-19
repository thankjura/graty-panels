import type { SearchQuery, UserValueDto } from "luna";
import type { PanelRenderValueDto } from "@/interfaces/render.ts";

export interface PanelValueDto {
  id: number,
  name: string,
  displayName: string,
  disabled: boolean,
}

export interface PanelDto extends PanelValueDto {
  created: string,
  updated: string,
  author: UserValueDto,
  updater: UserValueDto,
  qs: string,
  projectIds: Array<number>,
  issueTypeIds: Array<number>,
  render: PanelRenderValueDto,
}

export interface PanelSearchRequest extends SearchQuery {
  projectIds?: Array<number>,
  issueTypeIds?: Array<number>,
}