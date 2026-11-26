package ru.slie.luna.plugins.graty.panel;

import ru.slie.luna.issue.field.FieldManager;
import ru.slie.luna.issue.field.IssueField;
import ru.slie.luna.plugins.graty.db.PanelRenderEntity;

import java.util.List;
import java.util.Objects;

public class PanelRender {
    private final long id;
    private String name;
    private String description;
    private List<IssueField> fields;
    private Integer limit;
    private boolean showActions;

    public PanelRender(PanelRenderEntity entity, FieldManager fieldManager) {
        this.id = entity.getId();
        update(entity, fieldManager);
    }

    public void update(PanelRenderEntity entity, FieldManager fieldManager) {
        this.name = entity.getName();
        this.description = entity.getDescription();
        this.fields = fieldManager.getByIds(entity.getFields()).stream().filter(IssueField::isValid).toList();
        this.limit = entity.getLimit();
        this.showActions = Objects.requireNonNullElse(entity.getShowActions(), false);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<IssueField> getFields() {
        return fields;
    }

    public Integer getLimit() {
        return limit;
    }

    public boolean isShowActions() {
        return showActions;
    }

    public long getId() {
        return id;
    }
}
