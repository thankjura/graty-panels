package ru.slie.luna.plugins.graty.dto.panel;

import ru.slie.luna.plugins.graty.dto.panel.issue.GratyIssueDto;
import ru.slie.luna.plugins.graty.panel.Panel;
import ru.slie.luna.search.SearchResult;

import java.util.ArrayList;
import java.util.List;

public class IssuePanelDto {
    private final Long id;
    private final String name;
    private final List<GratyIssueDto> issues = new ArrayList<>();
    private final int limit;
    private Long total;

    public IssuePanelDto(Panel panel) {
        this.id = panel.getId();
        if (panel.getDisplayName() != null && !panel.getDisplayName().isEmpty()) {
            this.name = panel.getDisplayName();
        } else {
            this.name = panel.getName();
        }
        this.limit = 10;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getLimit() {
        return limit;
    }

    public Long getTotal() {
        return total;
    }

    public List<GratyIssueDto> getIssues() {
        return issues;
    }

    public void setIssues(SearchResult<GratyIssueDto> result) {
        this.issues.clear();
        this.issues.addAll(result.getResults());
        this.total = result.getTotal();
    }
}
