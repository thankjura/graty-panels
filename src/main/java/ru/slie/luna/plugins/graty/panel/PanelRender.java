package ru.slie.luna.plugins.graty.panel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.slie.luna.issue.field.FieldManager;
import ru.slie.luna.issue.field.IssueField;
import ru.slie.luna.plugins.graty.db.PanelRenderEntity;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.json.JsonMapper;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class PanelRender {
    private static final Logger log = LoggerFactory.getLogger(PanelRender.class);
    private static final JsonMapper JSON_MAPPER = new JsonMapper();

    private final long id;
    private String name;
    private String description;
    private List<PanelRenderColumn> columns;
    private List<IssueField> fields;
    private Integer limit;
    private boolean showActions;
    private boolean showHeader;

    public PanelRender(PanelRenderEntity entity, FieldManager fieldManager) {
        this.id = entity.getId();
        update(entity, fieldManager);
    }

    public void update(PanelRenderEntity entity, FieldManager fieldManager) {
        this.name = entity.getName();
        this.description = entity.getDescription();
        this.limit = entity.getLimit();
        this.showActions = Objects.requireNonNullElse(entity.getShowActions(), false);
        this.showHeader = Objects.requireNonNullElse(entity.getShowHeader(), true);
        this.columns = resolveColumns(entity, fieldManager);

        Map<String, IssueField> uniqueFields = new LinkedHashMap<>();
        for (PanelRenderColumn column : this.columns) {
            for (IssueField field : column.getFields()) {
                uniqueFields.putIfAbsent(field.getId(), field);
            }
        }
        this.fields = List.copyOf(uniqueFields.values());
    }

    private List<PanelRenderColumn> resolveColumns(PanelRenderEntity entity, FieldManager fieldManager) {
        String rawConfig = entity.getColumnsConfig();
        if (rawConfig != null && !rawConfig.isBlank()) {
            try {
                List<PanelRenderColumnConfig> configs = JSON_MAPPER.readValue(rawConfig, new TypeReference<>() {});
                if (configs != null && !configs.isEmpty()) {
                    List<PanelRenderColumn> resolved = new ArrayList<>();
                    for (PanelRenderColumnConfig config : configs) {
                        if (config == null || config.getFields() == null || config.getFields().isEmpty()) {
                            continue;
                        }
                        List<IssueField> colFields = fieldManager.getByIds(config.getFields()).stream()
                                .filter(IssueField::isValid)
                                .toList();
                        if (!colFields.isEmpty()) {
                            String title = config.getTitle() != null && !config.getTitle().isBlank()
                                    ? config.getTitle().trim()
                                    : null;
                            resolved.add(new PanelRenderColumn(colFields, title, Boolean.TRUE.equals(config.getHideHeader()), config.getAlign()));
                        }
                    }
                    if (!resolved.isEmpty()) {
                        return List.copyOf(resolved);
                    }
                }
            } catch (Exception e) {
                log.warn("Failed to parse columnsConfig for panel render {}, falling back to fieldIds", entity.getId(), e);
            }
        }

        List<IssueField> legacyFields = fieldManager.getByIds(entity.getFields()).stream()
                .filter(IssueField::isValid)
                .toList();
        return legacyFields.stream()
                .map(field -> new PanelRenderColumn(List.of(field), null, false, "left"))
                .toList();
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<PanelRenderColumn> getColumns() {
        return columns;
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

    public boolean isShowHeader() {
        return showHeader;
    }

    public long getId() {
        return id;
    }
}
