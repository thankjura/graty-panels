import { client } from "@/utils/client.ts";
import type { PanelDto, PanelSearchRequest, PanelValueDto } from "@/interfaces/panel.ts";
import type { DeleteResult, SearchResult } from "luna";

class PanelService {
  search(request: PanelSearchRequest) {
    return client.get<SearchResult<PanelValueDto>>('/graty/panels', {params: request});
  }

  getById(panelId: number) {
    return client.get<PanelDto>(`/graty/panels/${panelId}`);
  }

  create(request: Partial<PanelDto>) {
    return client.post<PanelDto>(`/graty/panels`, request);
  }

  patch(id: number, request: Partial<PanelDto>) {
    return client.patch<PanelDto>(`/graty/panels/${id}`, request);
  }

  update(id: number, request: Partial<PanelDto>) {
    return client.put<PanelDto>(`/graty/panels/${id}`, request);
  }

  delete(id: number) {
    return client.delete<DeleteResult>(`/graty/panels/${id}`);
  }
}

export const panelService = new PanelService();