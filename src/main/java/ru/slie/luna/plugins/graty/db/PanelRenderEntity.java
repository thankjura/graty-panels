package ru.slie.luna.plugins.graty.db;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import ru.slie.luna.regolith.ActiveDocEntity;

import java.util.Arrays;
import java.util.List;

@Entity
@Table(name = "panel_renders")
public class PanelRenderEntity extends ActiveDocEntity {
    private String name;
    private String description;

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "field_ids", columnDefinition = "text[]")
    private String[] fields;

    @Column(name = "columns_config", columnDefinition = "TEXT")
    private String columnsConfig;

    private Integer limit;
    private Boolean showActions;
    private Boolean showHeader;

    public PanelRenderEntity() {}

    public PanelRenderEntity(String name,
                             String description,
                             List<String> fields,
                             String columnsConfig,
                             Integer limit,
                             Boolean showActions,
                             Boolean showHeader) {
        this.name = name;
        this.description = description;
        setFields(fields);
        this.columnsConfig = columnsConfig;
        this.limit = limit;
        this.showActions = showActions;
        this.showHeader = showHeader;
    }

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
        if (fields != null) {
            return Arrays.asList(fields);
        }
        return List.of();
    }

    public void setFields(List<String> fields) {
        if (fields != null) {
            this.fields = fields.toArray(new String[0]);
        } else {
            this.fields = new String[0];
        }
    }

    public String getColumnsConfig() {
        return columnsConfig;
    }

    public void setColumnsConfig(String columnsConfig) {
        this.columnsConfig = columnsConfig;
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
