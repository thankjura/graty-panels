package ru.slie.luna.plugins.graty.dto.panel;

import ru.slie.luna.issue.field.IssueField;
import ru.slie.luna.plugins.graty.dto.admin.PanelFieldDto;
import ru.slie.luna.plugins.graty.dto.admin.PanelRenderColumnDto;
import ru.slie.luna.plugins.graty.dto.panel.issue.GratyIssueDto;
import ru.slie.luna.plugins.graty.panel.Panel;
import ru.slie.luna.plugins.graty.panel.PanelRender;
import ru.slie.luna.plugins.graty.panel.PanelRenderColumn;
import ru.slie.luna.search.SearchResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class IssuePanelDto {
    private final Long id;
    private final String name;
    private final List<PanelFieldDto> fields;
    private final List<PanelRenderColumnDto> columns;
    private final boolean showActions;
    private final boolean showHeader;
    private final List<GratyIssueDto> issues = new ArrayList<>();
    private final int limit;
    private Long total;

    public IssuePanelDto(Panel panel, List<PanelRenderColumn> resolvedColumns, List<IssueField> resolvedFields) {
        this.id = panel.getId();
        if (panel.getDisplayName() != null && !panel.getDisplayName().isEmpty()) {
            this.name = panel.getDisplayName();
        } else {
            this.name = panel.getName();
        }
        PanelRender render = panel.getRender();
        this.columns = resolvedColumns.stream().map(PanelRenderColumnDto::from).filter(Objects::nonNull).toList();
        this.fields = resolvedFields.stream().map(PanelFieldDto::from).filter(Objects::nonNull).toList();
        this.limit = render != null && render.getLimit() != null && render.getLimit() > 0 ? render.getLimit() : 10;
        this.showActions = render != null && render.isShowActions();
        this.showHeader = render == null || render.isShowHeader();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<PanelFieldDto> getFields() {
        return fields;
    }

    public List<PanelRenderColumnDto> getColumns() {
        return columns;
    }

    public boolean isShowActions() {
        return showActions;
    }

    public boolean isShowHeader() {
        return showHeader;
    }

    public int getLimit() {
        return limit;
    }

    public Long getTotal() {
        return total;
    }

    public List<GratyIssueDto> getIssues() {
        return issues;
    }

    public void setIssues(SearchResult<GratyIssueDto> result) {
        this.issues.clear();
        this.issues.addAll(result.getResults());
        this.total = result.getTotal();
    }
}
