package ru.slie.luna.plugins.graty.rest.admin.request;

import ru.slie.luna.plugins.graty.panel.PanelRenderColumnConfig;

import java.util.List;

public class PanelRenderRequest {
    private String name;
    private String description;
    private List<String> fields;
    private List<PanelRenderColumnConfig> columns;
    private Integer limit;
    private Boolean showActions;
    private Boolean showHeader;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getFields() {
        return fields;
    }

    public void setFields(List<String> fields) {
        this.fields = fields;
    }

    public List<PanelRenderColumnConfig> getColumns() {
        return columns;
    }

    public void setColumns(List<PanelRenderColumnConfig> columns) {
        this.columns = columns;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Boolean getShowActions() {
        return showActions;
    }

    public void setShowActions(Boolean showActions) {
        this.showActions = showActions;
    }

    public Boolean getShowHeader() {
        return showHeader;
    }

    public void setShowHeader(Boolean showHeader) {
        this.showHeader = showHeader;
    }
}
