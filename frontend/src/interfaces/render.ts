export interface PanelFieldDto {
  id: string,
  name: string,
  system: boolean,
}

export interface PanelRenderValueDto {
  id: number,
  name: string,
  description: string,
}

export interface PanelRenderDto extends PanelRenderValueDto {
  fields: Array<PanelFieldDto>,
  limit: number,
}

export interface PanelRenderRequest {
  name: string,
  description: string,
  fields: Array<string>,
  limit: number,
}