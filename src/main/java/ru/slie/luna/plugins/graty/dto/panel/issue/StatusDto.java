package ru.slie.luna.plugins.graty.dto.panel.issue;

import ru.slie.luna.issue.status.Status;

public class StatusDto {
    private final Long id;
    private final String name;
    private final String categoryKey;

    public StatusDto(Status status) {
        this.id = status.getId();
        this.name = status.getName();
        this.categoryKey = status.getCategoryKey();
    }

    public static StatusDto from(Status status) {
        if (status == null) {
            return null;
        }

        return new StatusDto(status);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategoryKey() {
        return categoryKey;
    }
}
