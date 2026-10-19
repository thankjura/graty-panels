import { client } from "@/utils/client.ts";
import type { IssuePanelDto } from "@/interfaces/issue.ts";

class IssuePanelService {
  getPanelsForIssue(key: string) {
    return client.get<Array<IssuePanelDto>>(`/graty/panels/${key}`);
  }
}

export const issuePanelService = new IssuePanelService();