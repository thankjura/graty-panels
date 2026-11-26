package ru.slie.luna.plugins.graty.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.slie.luna.db.SearchResultList;
import ru.slie.luna.issue.Issue;
import ru.slie.luna.issue.IssueFieldConstants;
import ru.slie.luna.issue.field.FieldManager;
import ru.slie.luna.issue.field.IssueField;
import ru.slie.luna.plugins.graty.dto.panel.issue.GratyIssueDto;
import ru.slie.luna.search.SearchResult;

import java.util.Collection;
import java.util.List;

@Component
public class IssueDtoMapper {
    private static final Logger log = LoggerFactory.getLogger(IssueDtoMapper.class);
    private final FieldManager fieldManager;

    public IssueDtoMapper(FieldManager fieldManager) {
        this.fieldManager = fieldManager;
    }

    @SuppressWarnings("unchecked")
    private GratyIssueDto mapIssue(Issue issue, Collection<IssueField> fields) {
        GratyIssueDto dto = new GratyIssueDto(issue);
        for (IssueField field : fields) {
            if (IssueFieldConstants.ISSUE_ID.equals(field.getId()) || IssueFieldConstants.ISSUE_KEY.equals(field.getId())) {
                continue;
            }
            Object value = issue.getFieldValue(field);
            if (value == null) {
                continue;
            }
            try {
                dto.setFieldValue(field.getId(), field.getFieldType().getViewValue(field, issue, value));
            } catch (Exception e) {
                log.error("Failed to serialize {} value for field {}", issue.getKey(), field.getId(), e);
            }
        }
        return dto;
    }

    public SearchResult<GratyIssueDto> mapResult(SearchResult<Issue> result, List<IssueField> fields) {
        List<Issue> issues = result.getResults();
        fieldManager.prefetchComputedValues(fields, issues);
        List<GratyIssueDto> dtos = issues.stream().map(issue -> mapIssue(issue, fields)).toList();
        return new SearchResultList<>(result.getTotal(), dtos, result.searchParams());
    }
}
