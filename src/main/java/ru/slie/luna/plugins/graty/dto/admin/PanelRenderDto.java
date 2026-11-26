package ru.slie.luna.plugins.graty.dto.admin;

import ru.slie.luna.plugins.graty.panel.PanelRender;

import java.util.List;
import java.util.Objects;

public class PanelRenderDto extends PanelRenderValueDto {
    private final List<PanelFieldDto> fields;
    private final int limit;
    private final boolean showActions;

    public PanelRenderDto(PanelRender render) {
        super(render);
        this.fields = render.getFields().stream().map(PanelFieldDto::from).filter(Objects::nonNull).toList();
        this.limit = Objects.requireNonNullElse(render.getLimit(), 10);
        this.showActions = render.isShowActions();
    }

    public static PanelRenderDto from(PanelRender render) {
        if (render == null) {
            return null;
        }

        return new PanelRenderDto(render);
    }

    public List<PanelFieldDto> getFields() {
        return fields;
    }

    public int getLimit() {
        return limit;
    }

    public boolean isShowActions() {
        return showActions;
    }
}
