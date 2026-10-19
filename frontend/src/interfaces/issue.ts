import type { PriorityValueDto, StatusValueDto } from "luna";

export interface IssueDto {
  id: number,
  key: string,
  summary: string,
  priority: PriorityValueDto,
  status: StatusValueDto,
}

export interface IssuePanelDto {
  id: number,
  name: string,
  issues: Array<IssueDto>,
  limit: number,
  total: number,
}