export interface PanelFieldDto {
  id: string,
  name: string,
  system: boolean,
  viewComponent?: string | null,
  navigatorComponent?: string | null,
}

export interface PanelRenderValueDto {
  id: number,
  name: string,
  description: string | null,
}

export interface PanelRenderDto extends PanelRenderValueDto {
  fields: Array<PanelFieldDto>,
  limit: number,
  showActions: boolean,
}

export interface PanelRenderRequest {
  name: string,
  description: string | null,
  fields: Array<string>,
  limit: number,
  showActions: boolean,
}
