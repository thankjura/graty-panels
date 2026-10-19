package ru.slie.luna.plugins.graty.panel;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;
import ru.slie.luna.db.SearchResultImpl;
import ru.slie.luna.db.filter.Filters;
import ru.slie.luna.db.query.DeleteResult;
import ru.slie.luna.db.query.FindOptions;
import ru.slie.luna.db.query.Query;
import ru.slie.luna.exception.ValidateException;
import ru.slie.luna.issue.IssueContext;
import ru.slie.luna.issue.field.FieldManager;
import ru.slie.luna.locale.I18nResolver;
import ru.slie.luna.plugins.graty.db.PanelEntity;
import ru.slie.luna.plugins.graty.rest.admin.request.PanelRequest;
import ru.slie.luna.regolith.ActiveDocManager;
import ru.slie.luna.search.SearchResult;
import ru.slie.luna.user.User;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.json.JsonMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@Transactional
public class PanelManager {
    private final ActiveDocManager activeDocManager;
    private final I18nResolver i18n;
    private final FieldManager fieldManager;

    public PanelManager(ActiveDocManager activeDocManager,
                        I18nResolver i18n,
                        FieldManager fieldManager) {
        this.activeDocManager = activeDocManager;
        this.i18n = i18n;
        this.fieldManager = fieldManager;
    }

    private Panel wrap(PanelEntity entity) {
        return new Panel(entity, fieldManager);
    }

    private Optional<Panel> wrapNullable(PanelEntity entity) {
        if (entity == null) {
            return Optional.empty();
        }

        return Optional.of(wrap(entity));
    }

    private void validate(PanelEntity entity) throws ValidateException {
        ValidateException exception = new ValidateException();
        if (entity.getName() == null || entity.getName().isEmpty()) {
            exception.addError("name", i18n.getText("graty.field.name.required"));
        }

        if (entity.getRender() == null) {
            exception.addError("render", i18n.getText("graty.field.render.required"));
        }

        exception.raise();
    }

    public Optional<Panel> getById(Long id) {
        return wrapNullable(activeDocManager.getById(PanelEntity.class, id));
    }

    public List<Panel> getForContext(IssueContext issueContext) {
        FindOptions option = new FindOptions().sort("priority");
        return activeDocManager.query(PanelEntity.class)
                       .filter(Filters.eq("projectIds", issueContext.getProjectId()),
                               Filters.eq("issueTypeIds", issueContext.getIssueTypeId()))
                       .list(option)
                       .stream()
                       .map(this::wrap)
                       .toList();
    }

    public boolean hasForContext(IssueContext context) {
        return activeDocManager.query(PanelEntity.class)
                       .filter(Filters.eq("projectIds", context.getProjectId()),
                               Filters.eq("issueTypeIds", context.getIssueTypeId()))
                       .count() > 0;
    }

    public SearchResult<Panel> search(PanelSearchParams params) {
        Query<PanelEntity> query = activeDocManager.query(PanelEntity.class);
        if (params.term() != null && !params.term().isEmpty()) {
            query.filter(Filters.or(
                    Filters.like("name", params.term(), true),
                    Filters.like("displayName", params.term(), true)
            ));
        }

        if (params.projectIds() != null && !params.projectIds().isEmpty()) {
            query.filter(Filters.in("projectIds", params.projectIds()));
        }

        if (params.issueTypeIds() != null && !params.issueTypeIds().isEmpty()) {
            query.filter(Filters.in("issueTypeIds", params.issueTypeIds()));
        }

        FindOptions options = new FindOptions().sort("priority").skip(params.getSkip()).limit(params.getLimit());
        return new SearchResultImpl<>(query, params, options, this::wrap);
    }

    public Panel create(User author, PanelRequest request) throws ValidateException {
        PanelEntity entity = new PanelEntity(author.getId(),
                request.getName(),
                request.getDescription(),
                request.getDisplayName(),
                request.getQs(),
                request.getPriority());
        entity.setProjectIds(request.getProjectIds());
        entity.setIssueTypeIds(request.getIssueTypeIds());
        validate(entity);
        activeDocManager.save(entity);
        return wrap(entity);
    }

    public void update(User updater, Panel panel, PanelRequest request) throws ValidateException {
        PanelEntity entity = activeDocManager.getById(PanelEntity.class, panel.getId());
        if (entity == null) {
            return;
        }

        entity.setName(request.getName());
        entity.setDisplayName(request.getDisplayName());
        entity.setQs(request.getQs());
        entity.setPriority(request.getPriority());
        entity.setProjectIds(request.getProjectIds());
        entity.setIssueTypeIds(request.getIssueTypeIds());
        entity.setUpdaterId(updater.getId());
        entity.setUpdated(LocalDateTime.now());
        validate(entity);
        activeDocManager.save(entity);
        panel.update(entity, fieldManager);
    }

    public void patch(User updater, Panel panel, Map<String, Object> request) throws ValidateException {
        PanelEntity entity = activeDocManager.getById(PanelEntity.class, panel.getId());
        if (entity == null) {
            return;
        }

        JsonMapper mapper = new JsonMapper();

        for (Map.Entry<String, Object> entry : request.entrySet()) {
            switch (entry.getKey()) {
                case "name" -> entity.setName(mapper.convertValue(entry.getValue(), String.class));
                case "displayName" -> entity.setDisplayName(mapper.convertValue(entry.getValue(), String.class));
                case "qs" -> entity.setQs(mapper.convertValue(entry.getValue(), String.class));
                case "priority" -> entity.setPriority(mapper.convertValue(entry.getValue(), Integer.class));
                case "issueTypeIds" -> entity.setIssueTypeIds(mapper.convertValue(entry.getValue(), new TypeReference<>() {}));
                case "projectIds" -> entity.setProjectIds(mapper.convertValue(entry.getValue(), new TypeReference<>() {}));
                case "disabled" -> entity.setDisabled(mapper.convertValue(entry.getValue(), Boolean.class));
            }
        }

        entity.setUpdaterId(updater.getId());
        entity.setUpdated(LocalDateTime.now());
        validate(entity);
        activeDocManager.save(entity);
        panel.update(entity, fieldManager);
    }

    public DeleteResult delete(Panel panel) {
        return activeDocManager.delete(PanelEntity.class, panel.getId());
    }
}
