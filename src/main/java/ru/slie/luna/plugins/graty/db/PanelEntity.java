package ru.slie.luna.plugins.graty.db;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import ru.slie.luna.regolith.ActiveDocEntity;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "panels")
public class PanelEntity extends ActiveDocEntity {
    private String name;
    private String displayName;
    @Column(columnDefinition = "TEXT")
    private String qs;
    private Long authorId;
    private Long updaterId;
    private LocalDateTime created;
    private LocalDateTime updated;
    private Boolean disabled;
    private Integer priority;

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "project_ids", columnDefinition = "bigint[]")
    private Long[] projectIds;

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "issue_type_ids", columnDefinition = "bigint[]")
    private Long[] issueTypeIds;

    public PanelEntity() {}

    public PanelEntity(Long authorId, String name, String displayName, String qs, Integer priority) {
        this.name = name;
        this.displayName = displayName;
        this.qs = qs;
        this.authorId = authorId;
        this.created = LocalDateTime.now();
        this.updated = LocalDateTime.now();
        this.priority = priority;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getQs() {
        return qs;
    }

    public void setQs(String qs) {
        this.qs = qs;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public Long getUpdaterId() {
        return updaterId;
    }

    public void setUpdaterId(Long updaterId) {
        this.updaterId = updaterId;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public LocalDateTime getUpdated() {
        return updated;
    }

    public void setUpdated(LocalDateTime updated) {
        this.updated = updated;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public Integer getPriority() {
        return priority;
    }
    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Set<Long> getProjectIds() {
        if (projectIds == null) {
            return Set.of();
        }
        return Set.of(projectIds);
    }

    public void setProjectIds(Set<Long> projectIds) {
        if (projectIds == null) {
            this.projectIds = null;
        } else {
            this.projectIds = projectIds.toArray(new Long[0]);
        }
    }

    public Set<Long> getIssueTypeIds() {
        if (issueTypeIds == null) {
            return Set.of();
        }

        return Set.of(issueTypeIds);
    }

    public void setIssueTypeIds(Set<Long> issue_typeIds) {
        if (issue_typeIds == null) {
            this.issueTypeIds = null;
        } else {
            this.issueTypeIds = issue_typeIds.toArray(new Long[0]);
        }
    }
}
