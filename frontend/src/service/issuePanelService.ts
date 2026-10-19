import { client } from "@/utils/client.ts";
import type { GratyIssueDto, IssuePanelDto } from "@/interfaces/issue.ts";
import type { SearchResult } from "luna";

class IssuePanelService {
  getPanelsForIssue(key: string) {
    return client.get<Array<IssuePanelDto>>(`/graty/panels/${key}`);
  }

  searchIssues(panelId: number, key: string, params: {page: number, limit: number}) {
    return client.get<SearchResult<GratyIssueDto>>(`/graty/panels/${key}/${panelId}/issues`, {params: params});
  }
}

export const issuePanelService = new IssuePanelService();