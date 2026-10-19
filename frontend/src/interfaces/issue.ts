import type { PriorityValueDto, StatusValueDto } from "luna";

export interface GratyIssueDto {
  id: number,
  key: string,
  summary: string,
  priority: PriorityValueDto,
  status: StatusValueDto,
}

export interface IssuePanelDto {
  id: number,
  name: string,
  issues: Array<GratyIssueDto>,
  limit: number,
  total: number,
}