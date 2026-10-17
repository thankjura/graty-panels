package ru.slie.luna.plugins.graty.dto;

import ru.slie.luna.plugins.graty.panel.Panel;

public class PanelValueDto {
    private final Long id;
    private final String name;
    private final String displayName;
    private final Boolean disabled;

    public PanelValueDto(Panel panel) {
        this.id = panel.getId();
        this.name = panel.getName();
        this.displayName = panel.getDisplayName();
        this.disabled = panel.isDisabled();
    }

    public static PanelValueDto from(Panel panel) {
        if (panel == null) {
            return null;
        }
        return new PanelValueDto(panel);
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

    public Boolean getDisabled() {
        return disabled;
    }
}
