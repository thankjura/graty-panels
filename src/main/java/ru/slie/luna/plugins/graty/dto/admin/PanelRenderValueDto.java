package ru.slie.luna.plugins.graty.dto.admin;

import ru.slie.luna.plugins.graty.panel.PanelRender;

public class PanelRenderValueDto {
    private final Long id;
    private final String name;
    private final String description;

    public PanelRenderValueDto(PanelRender render) {
        this.id = render.getId();
        this.name = render.getName();
        this.description = render.getDescription();
    }

    public static PanelRenderValueDto from(PanelRender render) {
        if (render == null) {
            return null;
        }

        return new PanelRenderValueDto(render);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
