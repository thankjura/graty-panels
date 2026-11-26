package ru.slie.luna.plugins.graty.dto.admin;

import ru.slie.luna.issue.field.IssueField;

public class PanelFieldDto {
    private final String id;
    private final String name;
    private final boolean system;
    private final String viewComponent;
    private final String navigatorComponent;

    public PanelFieldDto(String id, String name, boolean system, String viewComponent, String navigatorComponent) {
        this.id = id;
        this.name = name;
        this.system = system;
        this.viewComponent = viewComponent;
        this.navigatorComponent = navigatorComponent;
    }

    public static PanelFieldDto from(IssueField field) {
        if (field == null) {
            return null;
        }

        return new PanelFieldDto(field.getId(), field.getName(), field.isSystemField(), field.getViewComponent(), field.getNavigatorComponent());
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

    public String getViewComponent() {
        return viewComponent;
    }

    public String getNavigatorComponent() {
        return navigatorComponent;
    }
}
