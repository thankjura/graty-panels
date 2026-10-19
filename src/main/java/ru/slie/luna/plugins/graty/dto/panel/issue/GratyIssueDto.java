package ru.slie.luna.plugins.graty.dto.panel.issue;

import ru.slie.luna.issue.Issue;

public class GratyIssueDto {
    private final Long id;
    private final String key;
    private final String summary;
    private final StatusDto status;
    private final PriorityDto priority;

    public GratyIssueDto(Issue issue) {
        this.id = issue.getId();
        this.key = issue.getKey();
        this.summary = issue.getSummary();
        this.status = StatusDto.from(issue.getStatus());
        this.priority = PriorityDto.from(issue.getPriority());
    }

    public Long getId() {
        return id;
    }

    public String getKey() {
        return key;
    }

    public String getSummary() {
        return summary;
    }

    public StatusDto getStatus() {
        return status;
    }

    public PriorityDto getPriority() {
        return priority;
    }
}
