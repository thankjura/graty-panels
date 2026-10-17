package ru.slie.luna.plugins.graty.rest.request;

import ru.slie.luna.plugins.graty.panel.PanelSearchParams;

import java.util.Set;

public class SearchTermRequest {
    private String term;
    private Set<Long> projectIds;
    private Set<Long> issueTypeIds;
    private Integer page;
    private Integer limit;

    public String getTerm() {
        return term;
    }

    public Set<Long> getProjectIds() {
        return projectIds;
    }

    public Set<Long> getIssueTypeIds() {
        return issueTypeIds;
    }

    public Integer getPage() {
        if (page == null || page < 1) {
            return 1;
        }
        return page;
    }

    public Integer getLimit() {
        if (limit == null || limit < 1) {
            return 20;
        }
        return limit;
    }

    public PanelSearchParams getSearchParams() {
        return PanelSearchParams
                       .forPage(getPage(), getLimit())
                       .term(getTerm())
                       .projectIds(getProjectIds())
                       .issueTypeIds(getIssueTypeIds());
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public void setProjectIds(Set<Long> projectIds) {
        this.projectIds = projectIds;
    }

    public void setIssueTypeIds(Set<Long> issueTypeIds) {
        this.issueTypeIds = issueTypeIds;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }
}
