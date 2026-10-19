package ru.slie.luna.plugins.graty.dto.admin;

import ru.slie.luna.issue.field.IssueField;

public class PanelFieldDto {
    private final String id;
    private final String name;
    private final boolean system;

    public PanelFieldDto(String id, String name, boolean system) {
        this.id = id;
        this.name = name;
        this.system = system;
    }

    public static PanelFieldDto from(IssueField field) {
        if (field == null) {
            return null;
        }

        return new PanelFieldDto(field.getId(), field.getName(), field.isSystemField());
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isSystem() {
        return system;
    }
}
