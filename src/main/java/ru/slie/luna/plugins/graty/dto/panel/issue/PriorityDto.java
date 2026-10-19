package ru.slie.luna.plugins.graty.dto.panel.issue;

import ru.slie.luna.issue.priority.Priority;

public class PriorityDto {
    private final Long id;
    private final String name;
    private final String iconUrl;

    public PriorityDto(Priority priority) {
        this.id = priority.getId();
        this.name = priority.getName();
        this.iconUrl = priority.getIconUrl();
    }

    public static PriorityDto from(Priority priority) {
        if (priority == null) {
            return null;
        }

        return new PriorityDto(priority);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getIconUrl() {
        return iconUrl;
    }
}
