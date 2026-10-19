package ru.slie.luna.plugins.graty.rest.admin.request;

import java.util.Set;

public class PanelRequest {
    private String name;
    private String description;
    private String displayName;
    private String qs;
    private Set<Long> projectIds;
    private Set<Long> issueTypeIds;
    private Integer priority;

    public String getName() {
        return name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getQs() {
        return qs;
    }

    public Set<Long> getProjectIds() {
        return projectIds;
    }

    public Set<Long> getIssueTypeIds() {
        return issueTypeIds;
    }

    public Integer getPriority() {
        return priority;
    }

    public String getDescription() {
        return description;
    }
}
