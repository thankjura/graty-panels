package ru.slie.luna.plugins.graty.rest.admin.request;

import java.util.List;

public class PanelRenderRequest {
    private String name;
    private String description;
    private List<String> fields;
    private Integer limit;
    private Boolean showActions;

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
}
