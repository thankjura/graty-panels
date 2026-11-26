package ru.slie.luna.plugins.graty.panel;

import ru.slie.luna.issue.field.IssueField;

import java.util.List;

public class PanelRenderColumn {
    private final List<IssueField> fields;
    private final String title;
    private final boolean hideHeader;
    private final String align;

    public PanelRenderColumn(List<IssueField> fields, String title, boolean hideHeader, String align) {
        this.fields = fields;
        this.title = title;
        this.hideHeader = hideHeader;
        this.align = PanelRenderColumnConfig.normalizeAlign(align);
    }

    public List<IssueField> getFields() {
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
