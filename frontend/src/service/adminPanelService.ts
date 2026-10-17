import { client } from "@/utils/client.ts";
import type { PanelDto, PanelSearchRequest } from "@/interfaces/panel.ts";
import type { DeleteResult, SearchResult } from "luna";

class AdminPanelService {
  search(request: PanelSearchRequest) {
    return client.get<SearchResult<PanelDto>>('/graty/admin/panels', {params: request});
  }

  getById(panelId: number) {
    return client.get<PanelDto>(`/graty/admin/panels/${panelId}`);
  }

  create(request: Partial<PanelDto>) {
    return client.post<PanelDto>(`/graty/admin/panels`, request);
  }

  patch(id: number, request: Partial<PanelDto>) {
    return client.patch<PanelDto>(`/graty/admin/panels/${id}`, request);
  }

  update(id: number, request: Partial<PanelDto>) {
    return client.put<PanelDto>(`/graty/admin/panels/${id}`, request);
  }

  delete(id: number) {
    return client.delete<DeleteResult>(`/graty/admin/panels/${id}`);
  }
}

export const adminPanelService = new AdminPanelService();