package ru.slie.luna.plugins.graty.util;

import org.springframework.stereotype.Component;
import ru.slie.luna.issue.Issue;
import ru.slie.luna.plugins.graty.dto.IssueDto;
import ru.slie.luna.plugins.graty.panel.Panel;

import java.util.ArrayList;
import java.util.List;

@Component
public class IssueDtoMapper {
    public List<IssueDto> toDtoList(List<Issue> issues, Panel panel) {
        List<IssueDto> dtos = new ArrayList<>();
        for (Issue issue : issues) {
            IssueDto dto = new IssueDto(issue);
        }

        return dtos;
    }
}
