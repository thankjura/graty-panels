package ru.slie.luna.plugins.graty.util;

import org.springframework.stereotype.Component;
import ru.slie.luna.db.SearchResultList;
import ru.slie.luna.issue.Issue;
import ru.slie.luna.plugins.graty.dto.GratyIssueDto;
import ru.slie.luna.search.SearchResult;

import java.util.ArrayList;
import java.util.List;

@Component
public class IssueDtoMapper {
    public SearchResult<GratyIssueDto> mapResult(SearchResult<Issue> result) {
        List<GratyIssueDto> dtos = new ArrayList<>();
        for (Issue issue : result.getResults()) {
            GratyIssueDto dto = new GratyIssueDto(issue);
            dtos.add(dto);
        }

        return new SearchResultList<>(result.getTotal(), dtos, result.searchParams());
    }
}
