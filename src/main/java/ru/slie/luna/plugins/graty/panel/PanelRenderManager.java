package ru.slie.luna.plugins.graty.panel;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;
import ru.slie.luna.db.SearchResultImpl;
import ru.slie.luna.db.filter.Filters;
import ru.slie.luna.db.query.DeleteResult;
import ru.slie.luna.db.query.FindOptions;
import ru.slie.luna.db.query.Query;
import ru.slie.luna.exception.ValidateException;
import ru.slie.luna.issue.field.FieldManager;
import ru.slie.luna.issue.field.IssueField;
import ru.slie.luna.locale.I18nResolver;
import ru.slie.luna.plugins.graty.db.PanelRenderEntity;
import ru.slie.luna.plugins.graty.rest.admin.request.PanelRenderRequest;
import ru.slie.luna.regolith.ActiveDocManager;
import ru.slie.luna.search.SearchParams;
import ru.slie.luna.search.SearchResult;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.json.JsonMapper;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Component
@Transactional
public class PanelRenderManager {
    private static final JsonMapper JSON_MAPPER = new JsonMapper();

    private final ActiveDocManager activeDocManager;
    private final FieldManager fieldManager;
    private final I18nResolver i18n;

    public PanelRenderManager(ActiveDocManager activeDocManager,
                              FieldManager fieldManager,
                              I18nResolver i18n) {
        this.activeDocManager = activeDocManager;
        this.fieldManager = fieldManager;
        this.i18n = i18n;
    }

    private void validate(PanelRenderEntity entity) throws ValidateException {
        ValidateException validateException = new ValidateException();

        if (entity.getName() == null || entity.getName().isBlank()) {
            validateException.addError("name", i18n.getText("graty.field.name.required"));
        }

        if (entity.getFields() == null || entity.getFields().isEmpty()) {
            validateException.addError("fields", i18n.getText("graty.field.fields.required"));
        }

        if (entity.getLimit() == null || entity.getLimit() < 1) {
            validateException.addError("limit", i18n.getText("graty.field.limit.required"));
        }

        validateException.raise();
    }

    private List<PanelRenderColumnConfig> normalizeColumns(List<PanelRenderColumnConfig> columns, List<String> fallbackFields) {
        List<PanelRenderColumnConfig> normalized = new ArrayList<>();
        if (columns != null) {
            for (PanelRenderColumnConfig col : columns) {
                if (col == null || col.getFields() == null || col.getFields().isEmpty()) {
                    continue;
                }
                List<String> validIds = new ArrayList<>(new LinkedHashSet<>(
                        fieldManager.getByIds(col.getFields()).stream()
                                .filter(IssueField::isValid)
                                .map(IssueField::getId)
                                .toList()
                ));
                if (!validIds.isEmpty()) {
                    String title = col.getTitle() != null && !col.getTitle().isBlank() ? col.getTitle().trim() : null;
                    normalized.add(new PanelRenderColumnConfig(
                            validIds,
                            title,
                            Boolean.TRUE.equals(col.getHideHeader()),
                            PanelRenderColumnConfig.normalizeAlign(col.getAlign())
                    ));
                }
            }
            return normalized;
        }

        if (fallbackFields != null) {
            for (IssueField field : fieldManager.getByIds(fallbackFields)) {
                if (field.isValid()) {
                    normalized.add(new PanelRenderColumnConfig(List.of(field.getId()), null, false, "left"));
                }
            }
        }
        return normalized;
    }

    private void applyColumns(PanelRenderEntity entity, List<PanelRenderColumnConfig> columns, List<String> fallbackFields) {
        List<PanelRenderColumnConfig> normalized = normalizeColumns(columns, fallbackFields);
        Set<String> flatFieldIds = new LinkedHashSet<>();
        for (PanelRenderColumnConfig col : normalized) {
            flatFieldIds.addAll(col.getFields());
        }
        entity.setFields(new ArrayList<>(flatFieldIds));
        entity.setColumnsConfig(normalized.isEmpty() ? null : JSON_MAPPER.writeValueAsString(normalized));
    }

    private PanelRender wrap(PanelRenderEntity entity) {
        return new PanelRender(entity, fieldManager);
    }

    private Optional<PanelRender> wrapNullable(PanelRenderEntity entity) {
        if (entity == null) {
            return Optional.empty();
        }

        return Optional.of(wrap(entity));
    }

    public Optional<PanelRender> getById(Long id) {
        PanelRenderEntity entity = activeDocManager.getById(PanelRenderEntity.class, id);
        return wrapNullable(entity);
    }

    public PanelRender create(PanelRenderRequest request) throws ValidateException {
        boolean showHeader = request.getShowHeader() == null || request.getShowHeader();
        PanelRenderEntity entity = new PanelRenderEntity(
                request.getName(),
                request.getDescription(),
                List.of(),
                null,
                request.getLimit(),
                Boolean.TRUE.equals(request.getShowActions()),
                showHeader
        );
        applyColumns(entity, request.getColumns(), request.getFields());
        validate(entity);
        activeDocManager.save(entity);
        return wrap(entity);
    }

    public List<PanelRender> getAll() {
        return activeDocManager.query(PanelRenderEntity.class)
                       .list(new FindOptions().sort("name"))
                       .stream()
                       .map(this::wrap)
                       .toList();
    }

    public SearchResult<PanelRender> search(String term, SearchParams searchParams) {
        Query<PanelRenderEntity> query = activeDocManager.query(PanelRenderEntity.class);
        if (term != null && !term.isEmpty()) {
            query.filter(Filters.like("name", term, true));
        }

        FindOptions options = new FindOptions().sort("name").skip(searchParams.getSkip()).limit(searchParams.getLimit());
        return new SearchResultImpl<>(query, searchParams, options, this::wrap);
    }

    public void update(PanelRender render, PanelRenderRequest request) throws ValidateException {
        PanelRenderEntity entity = activeDocManager.getById(PanelRenderEntity.class, render.getId());
        if (entity == null) {
            return;
        }

        entity.setName(request.getName());
        entity.setDescription(request.getDescription());
        applyColumns(entity, request.getColumns(), request.getFields());
        entity.setLimit(request.getLimit());
        entity.setShowActions(Boolean.TRUE.equals(request.getShowActions()));
        entity.setShowHeader(request.getShowHeader() == null || request.getShowHeader());

        validate(entity);
        activeDocManager.save(entity);
        render.update(entity, fieldManager);
    }

    public void patch(PanelRender render, Map<String, Object> request) throws ValidateException {
        if (request == null) {
            return;
        }

        PanelRenderEntity entity = activeDocManager.getById(PanelRenderEntity.class, render.getId());
        if (entity == null) {
            return;
        }

        for (Map.Entry<String, Object> entry : request.entrySet()) {
            switch (entry.getKey()) {
                case "name" -> entity.setName(JSON_MAPPER.convertValue(entry.getValue(), String.class));
                case "description" -> entity.setDescription(JSON_MAPPER.convertValue(entry.getValue(), String.class));
                case "columns" -> {
                    List<PanelRenderColumnConfig> cols = JSON_MAPPER.convertValue(entry.getValue(), new TypeReference<>() {});
                    applyColumns(entity, cols, null);
                }
                case "fields" -> {
                    if (!request.containsKey("columns")) {
                        List<String> fieldIds = JSON_MAPPER.convertValue(entry.getValue(), new TypeReference<>() {});
                        applyColumns(entity, null, fieldIds);
                    }
                }
                case "limit" -> entity.setLimit(JSON_MAPPER.convertValue(entry.getValue(), Integer.class));
                case "showActions" -> entity.setShowActions(JSON_MAPPER.convertValue(entry.getValue(), Boolean.class));
                case "showHeader" -> entity.setShowHeader(JSON_MAPPER.convertValue(entry.getValue(), Boolean.class));
            }
        }

        validate(entity);
        activeDocManager.save(entity);
        render.update(entity, fieldManager);
    }

    public DeleteResult delete(PanelRender render) {
        return activeDocManager.delete(PanelRenderEntity.class, render.getId());
    }
}
