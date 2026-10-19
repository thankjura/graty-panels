package ru.slie.luna.plugins.graty.rest.issue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.slie.luna.exception.SearchException;
import ru.slie.luna.issue.Issue;
import ru.slie.luna.issue.SearchService;
import ru.slie.luna.issue.query.QueryParser;
import ru.slie.luna.plugins.graty.dto.IssueDto;
import ru.slie.luna.plugins.graty.dto.IssuePanelDto;
import ru.slie.luna.plugins.graty.panel.Panel;
import ru.slie.luna.plugins.graty.panel.PanelManager;
import ru.slie.luna.plugins.graty.util.GratyUtils;
import ru.slie.luna.plugins.graty.util.IssueDtoMapper;
import ru.slie.luna.search.SearchParams;
import ru.slie.luna.search.SearchResult;
import ru.slie.luna.security.AuthenticationContext;
import ru.slie.luna.user.User;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/graty/panels")
public class IssuePanelRest {
    private final SearchService searchService;
    private final GratyUtils gratyUtils;
    private final AuthenticationContext authenticationContext;
    private final PanelManager panelManager;
    private final IssueDtoMapper issueDtoMapper;

    private static final Logger log = LoggerFactory.getLogger(IssuePanelRest.class);

    public IssuePanelRest(SearchService searchService,
                          GratyUtils gratyUtils,
                          AuthenticationContext authenticationContext,
                          PanelManager panelManager,
                          IssueDtoMapper issueDtoMapper) {
        this.searchService = searchService;
        this.gratyUtils = gratyUtils;
        this.authenticationContext = authenticationContext;
        this.panelManager = panelManager;
        this.issueDtoMapper = issueDtoMapper;
    }

    @GetMapping("/{issueKey}")
    public List<IssuePanelDto> getPanels(@PathVariable("issueKey") Issue issue) {
        List<Panel> panels = panelManager.getForContext(issue);
        List<IssuePanelDto> panelDtos = new ArrayList<>();
        for (Panel panel: panels) {
            IssuePanelDto panelDto = new IssuePanelDto(panel);
            panelDtos.add(panelDto);
        }

        if (!panels.isEmpty()) {
            User user = authenticationContext.getCurrentUser();
            QueryParser.ParseResult result = gratyUtils.parse(user, issue, panels.getFirst().getQs());
            if (result.isValid()) {
                SearchParams params = SearchParams.forPage(1, 10);
                try {
                    SearchResult<Issue> results = searchService.search(user, result.getQuery(), params);
                    List<IssueDto> dtos = issueDtoMapper.toDtoList(results.getResults(), panels.getFirst());
                    panelDtos.getFirst().setIssues(dtos, results.getTotal());
                } catch (SearchException e) {
                    log.warn("Failed to search issues for panel {}", panels.getFirst().getId(), e);
                }
            }
        }

        return panelDtos;
    }
}
