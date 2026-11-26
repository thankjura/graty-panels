package ru.slie.luna.plugins.graty.panel;

import java.util.List;

public class PanelRenderColumnConfig {
    private List<String> fields;
    private String title;
    private Boolean hideHeader;
    private String align;

    public PanelRenderColumnConfig() {}

    public PanelRenderColumnConfig(List<String> fields, String title, Boolean hideHeader, String align) {
        this.fields = fields;
        this.title = title;
        this.hideHeader = hideHeader;
        this.align = align;
    }

    public static String normalizeAlign(String align) {
        if ("center".equalsIgnoreCase(align)) {
            return "center";
        }
        if ("right".equalsIgnoreCase(align)) {
            return "right";
        }
        return "left";
    }

    public List<String> getFields() {
        return fields;
    }

    public void setFields(List<String> fields) {
        this.fields = fields;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getHideHeader() {
        return hideHeader;
    }

    public void setHideHeader(Boolean hideHeader) {
        this.hideHeader = hideHeader;
    }

    public String getAlign() {
        return align;
    }

    public void setAlign(String align) {
        this.align = align;
    }
}
