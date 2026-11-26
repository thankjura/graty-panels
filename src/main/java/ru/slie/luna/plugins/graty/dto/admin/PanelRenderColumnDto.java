package ru.slie.luna.plugins.graty.dto.admin;

import ru.slie.luna.plugins.graty.panel.PanelRenderColumn;

import java.util.List;
import java.util.Objects;

public class PanelRenderColumnDto {
    private final List<PanelFieldDto> fields;
    private final String title;
    private final boolean hideHeader;
    private final String align;

    public PanelRenderColumnDto(List<PanelFieldDto> fields, String title, boolean hideHeader, String align) {
        this.fields = fields;
        this.title = title;
        this.hideHeader = hideHeader;
        this.align = align;
    }

    public static PanelRenderColumnDto from(PanelRenderColumn column) {
        if (column == null) {
            return null;
        }
        List<PanelFieldDto> fieldDtos = column.getFields().stream()
                .map(PanelFieldDto::from)
                .filter(Objects::nonNull)
                .toList();
        return new PanelRenderColumnDto(fieldDtos, column.getTitle(), column.isHideHeader(), column.getAlign());
    }

    public List<PanelFieldDto> getFields() {
        return fields;
    }

    public String getTitle() {
        return title;
    }

    public boolean isHideHeader() {
        return hideHeader;
    }

    public String getAlign() {
        return align;
    }
}
