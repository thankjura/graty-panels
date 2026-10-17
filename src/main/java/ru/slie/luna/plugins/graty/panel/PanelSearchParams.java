package ru.slie.luna.plugins.graty.panel;

import ru.slie.luna.search.SearchParams;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PanelSearchParams extends SearchParams {
    private String term;
    private Set<Long> projectIds;
    private Set<Long> issueTypeIds;

    protected PanelSearchParams(int page, int limit, Collection<String> excludes) {
        super(page, limit, excludes);
    }

    public static PanelSearchParams forPage(int page, int limit) {
        return new PanelSearchParams(page, limit, List.of());
    }

    public PanelSearchParams term(String term) {
        this.term = term;
        return this;
    }

    public String term() {
        return term;
    }

    public PanelSearchParams projectIds(Collection<Long> projectIds) {
        if (projectIds == null) {
            this.projectIds = new HashSet<>();
        } else {
            this.projectIds = new HashSet<>(projectIds);
        }
        return this;
    }

    public Set<Long> projectIds() {
        return projectIds;
    }

    public PanelSearchParams issueTypeIds(Collection<Long> issueTypeIds) {
        if (issueTypeIds == null) {
            this.issueTypeIds = new HashSet<>();
        } else {
            this.issueTypeIds = new HashSet<>(issueTypeIds);
        }

        return this;
    }

    public Set<Long> issueTypeIds() {
        return issueTypeIds;
    }
}
