package ru.slie.luna.plugins.graty.rest.issue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import ru.slie.luna.db.SearchResultEmpty;
import ru.slie.luna.exception.SearchException;
import ru.slie.luna.issue.Issue;
import ru.slie.luna.issue.IssueFieldConstants;
import ru.slie.luna.issue.SearchService;
import ru.slie.luna.issue.field.FieldManager;
import ru.slie.luna.issue.field.IssueField;
import ru.slie.luna.issue.query.QueryParser;
import ru.slie.luna.plugins.graty.dto.panel.IssuePanelDto;
import ru.slie.luna.plugins.graty.dto.panel.issue.GratyIssueDto;
import ru.slie.luna.plugins.graty.panel.Panel;
import ru.slie.luna.plugins.graty.panel.PanelManager;
import ru.slie.luna.plugins.graty.panel.PanelRender;
import ru.slie.luna.plugins.graty.util.GratyUtils;
import ru.slie.luna.plugins.graty.util.IssueDtoMapper;
import ru.slie.luna.search.SearchParams;
import ru.slie.luna.search.SearchResult;
import ru.slie.luna.security.AuthenticationContext;
import ru.slie.luna.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/graty/panels")
public class IssuePanelRest {
    private static final int DEFAULT_LIMIT = 10;
    private static final List<String> DEFAULT_FIELDS = List.of(
            IssueFieldConstants.ISSUE_TYPE,
            IssueFieldConstants.ISSUE_KEY,
            IssueFieldConstants.SUMMARY,
            IssueFieldConstants.PRIORITY,
            IssueFieldConstants.STATUS
    );

    private final SearchService searchService;
    private final GratyUtils gratyUtils;
    private final AuthenticationContext authenticationContext;
    private final PanelManager panelManager;
    private final IssueDtoMapper issueDtoMapper;
    private final FieldManager fieldManager;

    private static final Logger log = LoggerFactory.getLogger(IssuePanelRest.class);

    public IssuePanelRest(SearchService searchService,
                          GratyUtils gratyUtils,
                          AuthenticationContext authenticationContext,
                          PanelManager panelManager,
                          IssueDtoMapper issueDtoMapper,
                          FieldManager fieldManager) {
        this.searchService = searchService;
        this.gratyUtils = gratyUtils;
        this.authenticationContext = authenticationContext;
        this.panelManager = panelManager;
        this.issueDtoMapper = issueDtoMapper;
        this.fieldManager = fieldManager;
    }

    private List<IssueField> resolveFields(Panel panel) {
        PanelRender render = panel.getRender();
        if (render != null && render.getFields() != null && !render.getFields().isEmpty()) {
            return render.getFields();
        }
        return fieldManager.getByIds(DEFAULT_FIELDS);
    }

    private int resolveLimit(Panel panel, Integer requestedLimit) {
        PanelRender render = panel.getRender();
        int renderLimit = render != null && render.getLimit() != null && render.getLimit() > 0
                ? render.getLimit()
                : DEFAULT_LIMIT;
        if (requestedLimit != null && requestedLimit > 0) {
            return Math.min(requestedLimit, renderLimit);
        }
        return renderLimit;
    }

    private SearchResult<GratyIssueDto> getIssues(Panel panel, Issue issue, User user, SearchParams params, List<IssueField> fields) {
        QueryParser.ParseResult result = gratyUtils.parse(user, issue, panel.getQs());
        if (result.isValid()) {
            try {
                SearchResult<Issue> results = searchService.search(user, result.getQuery(), params);
                return issueDtoMapper.mapResult(results, fields);
            } catch (SearchException e) {
                log.warn("Failed to search issues for panel {}", panel.getId(), e);
            }
        }

        return new SearchResultEmpty<>(params);
    }

    @GetMapping("/{issueKey}")
    public List<IssuePanelDto> getPanels(@PathVariable("issueKey") Issue issue) {
        List<Panel> panels = panelManager.getForContext(issue);
        List<IssuePanelDto> panelDtos = new ArrayList<>();
        List<List<IssueField>> panelFields = new ArrayList<>();
        for (Panel panel : panels) {
            List<IssueField> fields = resolveFields(panel);
            panelFields.add(fields);
            panelDtos.add(new IssuePanelDto(panel, fields));
        }

        if (!panels.isEmpty()) {
            User user = authenticationContext.getCurrentUser();
            Panel firstPanel = panels.getFirst();
            int limit = resolveLimit(firstPanel, null);
            SearchResult<GratyIssueDto> issues = getIssues(firstPanel, issue, user, SearchParams.forPage(1, limit), panelFields.getFirst());
            panelDtos.getFirst().setIssues(issues);
        }

        return panelDtos;
    }

    @GetMapping("/{issueKey}/{panelId}/issues")
    public SearchResult<GratyIssueDto> getIssues(@PathVariable("issueKey") Issue issue,
                                                 @PathVariable Long panelId,
                                                 @RequestParam(value = "page", defaultValue = "1") int page,
                                                 @RequestParam(value = "limit", required = false) Integer limit) {
        Optional<Panel> panel = panelManager.getById(panelId);
        if (panel.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        int safePage = Math.max(1, page);
        int safeLimit = resolveLimit(panel.get(), limit);
        SearchParams params = SearchParams.forPage(safePage, safeLimit);

        User user = authenticationContext.getCurrentUser();
        List<IssueField> fields = resolveFields(panel.get());
        return getIssues(panel.get(), issue, user, params, fields);
    }
}
