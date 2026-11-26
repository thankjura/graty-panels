import { client } from "@/utils/client.ts";
import type { DeleteResult, SearchQuery, SearchResult } from "luna";
import type { PanelRenderDto, PanelRenderRequest } from "@/interfaces/render.ts";

class AdminRenderService {
  search(request: SearchQuery) {
    return client.get<SearchResult<PanelRenderDto>>('/graty/admin/renders/', {params: request});
  }

  getById(renderId: number) {
    return client.get<PanelRenderDto>(`/graty/admin/renders/${renderId}`);
  }

  create(request: Partial<PanelRenderRequest>) {
    return client.post<PanelRenderDto>(`/graty/admin/renders`, request);
  }

  patch(id: number, request: Partial<PanelRenderRequest>) {
    return client.patch<PanelRenderDto>(`/graty/admin/renders/${id}`, request);
  }

  update(id: number, request: Partial<PanelRenderRequest>) {
    return client.put<PanelRenderDto>(`/graty/admin/renders/${id}`, request);
  }

  delete(id: number) {
    return client.delete<DeleteResult>(`/graty/admin/renders/${id}`);
  }
}

export const adminRenderService = new AdminRenderService();