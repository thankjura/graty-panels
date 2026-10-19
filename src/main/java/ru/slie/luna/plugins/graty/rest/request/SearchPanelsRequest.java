package ru.slie.luna.plugins.graty.rest.request;

import ru.slie.luna.plugins.graty.panel.PanelSearchParams;

import java.util.Set;

public class SearchPanelsRequest extends SearchTermRequest {
    private Set<Long> projectIds;
    private Set<Long> issueTypeIds;

    public Set<Long> getProjectIds() {
        return projectIds;
    }

    public Set<Long> getIssueTypeIds() {
        return issueTypeIds;
    }

    public PanelSearchParams getSearchParams() {
        return PanelSearchParams
                       .forPage(getPage(), getLimit())
                       .term(getTerm())
                       .projectIds(getProjectIds())
                       .issueTypeIds(getIssueTypeIds());
    }

    public void setProjectIds(Set<Long> projectIds) {
        this.projectIds = projectIds;
    }

    public void setIssueTypeIds(Set<Long> issueTypeIds) {
        this.issueTypeIds = issueTypeIds;
    }
}
