package ru.slie.luna.plugins.graty.panel;

import com.fasterxml.jackson.annotation.JsonFormat;
import ru.slie.luna.plugins.graty.Constants;
import ru.slie.luna.plugins.graty.db.PanelEntity;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

public class Panel {
    private Long id;
    private String name;
    private String qs;
    private String displayName;
    private Integer priority;
    private Set<Long> projectIds;
    private Set<Long> issueTypeIds;
    private LocalDateTime created;
    private LocalDateTime updated;
    private Long authorId;
    private Long updaterId;
    private boolean disabled;

    public Panel(PanelEntity entity) {
        update(entity);
    }

    public void update(PanelEntity entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.qs = entity.getQs();
        this.displayName = entity.getDisplayName();
        this.priority = entity.getPriority();
        this.projectIds = entity.getProjectIds();
        this.issueTypeIds = entity.getIssueTypeIds();
        this.created = entity.getCreated();
        this.updated = entity.getUpdated();
        this.authorId = entity.getAuthorId();
        this.updaterId = entity.getUpdaterId();
        this.disabled = Objects.requireNonNullElse(entity.getDisabled(), false);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Integer getPriority() {
        return priority;
    }

    public Set<Long> getProjectIds() {
        return projectIds;
    }

    public Set<Long> getIssueTypeIds() {
        return issueTypeIds;
    }

    public String getQs() {
        return qs;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public Long getUpdaterId() {
        return updaterId;
    }

    @JsonFormat(pattern = Constants.DATE_TIME_FORMAT)
    public LocalDateTime getCreated() {
        return created;
    }

    @JsonFormat(pattern = Constants.DATE_TIME_FORMAT)
    public LocalDateTime getUpdated() {
        return updated;
    }

    public boolean isDisabled() {
        return disabled;
    }
}
