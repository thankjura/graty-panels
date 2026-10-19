package ru.slie.luna.plugins.graty.dto;

import ru.slie.luna.plugins.graty.panel.Panel;

import java.util.ArrayList;
import java.util.List;

public class IssuePanelDto {
    private final Long id;
    private final String name;
    private final List<IssueDto> issues = new ArrayList<>();
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

    public List<IssueDto> getIssues() {
        return issues;
    }

    public void setIssues(List<IssueDto> issues, long total) {
        this.issues.clear();
        this.issues.addAll(issues);
        this.total = total;
    }
}
