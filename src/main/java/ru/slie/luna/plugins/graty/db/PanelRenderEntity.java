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
    private Integer limit;

    public PanelRenderEntity() {}
    public PanelRenderEntity(String name, String description, List<String> fields, Integer limit) {
        this.name = name;
        this.description = description;
        if (fields != null) {
            this.fields = fields.toArray(new String[0]);
        } else {
            this.fields = new String[0];
        }
        this.limit = limit;
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

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }
}
