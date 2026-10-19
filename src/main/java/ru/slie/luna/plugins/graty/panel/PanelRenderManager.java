package ru.slie.luna.plugins.graty.panel;

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
import ru.slie.luna.regolith.ActiveDocManager;
import ru.slie.luna.search.SearchParams;
import ru.slie.luna.search.SearchResult;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.json.JsonMapper;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class PanelRenderManager {
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

        if (entity.getName() == null || entity.getName().isEmpty()) {
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


    public PanelRender create(String name, String description, List<IssueField> fields, Integer limit) throws ValidateException {
        PanelRenderEntity entity = new PanelRenderEntity(name, description, fields.stream().map(IssueField::getId).toList(), limit);
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

    public void update(PanelRender render, String name, String description, List<IssueField> fields, Integer limit) throws ValidateException {
        PanelRenderEntity entity = activeDocManager.getById(PanelRenderEntity.class, render.getId());
        if (entity == null) {
            return;
        }

        entity.setName(name);
        entity.setDescription(description);
        entity.setFields(fields.stream().map(IssueField::getId).toList());
        entity.setLimit(limit);

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

        JsonMapper mapper = new JsonMapper();

        for (Map.Entry<String, Object> entry : request.entrySet()) {
            switch (entry.getKey()) {
                case "name" -> entity.setName(mapper.convertValue(entry.getValue(), String.class));
                case "description" -> entity.setDescription(mapper.convertValue(entry.getValue(), String.class));
                case "fields" -> {
                    List<IssueField> issueFields = fieldManager.getByIds(mapper.convertValue(entry.getValue(), new TypeReference<>() {}));
                    entity.setFields(issueFields.stream().map(IssueField::getId).toList());
                }
                case "limit" -> entity.setLimit(mapper.convertValue(entry.getValue(), Integer.class));
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
