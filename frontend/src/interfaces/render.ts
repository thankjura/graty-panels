export type ColumnAlign = 'left' | 'center' | 'right';

export interface PanelFieldDto {
  id: string,
  name: string,
  system: boolean,
  viewComponent?: string | null,
  navigatorComponent?: string | null,
}

export interface PanelRenderColumnDto {
  fields: Array<PanelFieldDto>,
  title: string | null,
  hideHeader: boolean,
  align?: ColumnAlign,
}

export interface PanelRenderColumnRequest {
  fields: Array<string>,
  title?: string | null,
  hideHeader?: boolean,
  align?: ColumnAlign,
}

export interface PanelRenderValueDto {
  id: number,
  name: string,
  description: string | null,
}

export interface PanelRenderDto extends PanelRenderValueDto {
  fields: Array<PanelFieldDto>,
  columns: Array<PanelRenderColumnDto>,
  limit: number,
  showActions: boolean,
  showHeader: boolean,
}

export interface PanelRenderRequest {
  name: string,
  description: string | null,
  fields?: Array<string>,
  columns: Array<PanelRenderColumnRequest>,
  limit: number,
  showActions: boolean,
  showHeader: boolean,
}
