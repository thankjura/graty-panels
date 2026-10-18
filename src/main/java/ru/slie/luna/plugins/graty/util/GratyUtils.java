package ru.slie.luna.plugins.graty.util;

import org.springframework.stereotype.Component;
import ru.slie.luna.issue.Issue;
import ru.slie.luna.issue.IssueFieldConstants;
import ru.slie.luna.issue.SearchService;
import ru.slie.luna.issue.issuetype.IssueType;
import ru.slie.luna.issue.priority.Priority;
import ru.slie.luna.issue.query.QueryParser;
import ru.slie.luna.issue.status.Status;
import ru.slie.luna.issue.status.StatusCategory;
import ru.slie.luna.project.Project;
import ru.slie.luna.system.Constants;
import ru.slie.luna.user.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class GratyUtils {
    private final SearchService searchService;
    private final String issueParamRegexp = "@issue\\.(?<field>[\\w_]++)";
    private final Pattern issueParam = Pattern.compile(issueParamRegexp);

    public GratyUtils(SearchService searchService) {
        this.searchService = searchService;
    }

    private GratyFunction makeIssueFunction(String field, String type) {
        return new GratyFunction("@issue." + field, "@issue." + field, type, false);
    }

    public List<GratyFunction> getQsFunctions() {
        List<GratyFunction> gratyFunctions = new ArrayList<>();
        gratyFunctions.add(makeIssueFunction(IssueFieldConstants.ISSUE_KEY, Issue.class.getName()));
        gratyFunctions.add(makeIssueFunction(IssueFieldConstants.AUTHOR, User.class.getName()));
        gratyFunctions.add(makeIssueFunction(IssueFieldConstants.ASSIGNEE, User.class.getName()));
        gratyFunctions.add(makeIssueFunction(IssueFieldConstants.CREATOR, User.class.getName()));
        gratyFunctions.add(makeIssueFunction(IssueFieldConstants.PROJECT, Project.class.getName()));
        gratyFunctions.add(makeIssueFunction(IssueFieldConstants.ISSUE_TYPE, IssueType.class.getName()));
        gratyFunctions.add(makeIssueFunction(IssueFieldConstants.CREATED, LocalDateTime.class.getName()));
        gratyFunctions.add(makeIssueFunction(IssueFieldConstants.UPDATED, LocalDateTime.class.getName()));
        gratyFunctions.add(makeIssueFunction(IssueFieldConstants.STATUS, Status.class.getName()));
        gratyFunctions.add(makeIssueFunction(IssueFieldConstants.STATUS_CATEGORY, StatusCategory.class.getName()));
        gratyFunctions.add(makeIssueFunction(IssueFieldConstants.PRIORITY, Priority.class.getName()));
        return gratyFunctions;
    }

    public QueryParser.ParseResult parse(User user, Issue issue, String query) {
        String lunaQs = getLunaQs(issue, query);
        return searchService.parseQuery(user, lunaQs);
    }

    public String getLunaQs(Issue issue, String gratyQs) {
        Matcher m = issueParam.matcher(gratyQs);
        String lunaQs = gratyQs;
        while (m.find()) {
            lunaQs = lunaQs.replaceAll(m.group(), Matcher.quoteReplacement(resolveIssueParam(issue, m.group("field"))));
        }
        return lunaQs;
    }

    private String resolveIssueParam(Issue issue, String param) {
        return switch (param) {
            case IssueFieldConstants.ISSUE_KEY -> issue.getKey();
            case IssueFieldConstants.AUTHOR -> resolveUser(issue.getAuthor());
            case IssueFieldConstants.ASSIGNEE -> resolveUser(issue.getAssignee());
            case IssueFieldConstants.CREATOR -> resolveUser(issue.getCreator());
            case IssueFieldConstants.PROJECT -> issue.getProjectKey();
            case IssueFieldConstants.ISSUE_TYPE -> resolveId(issue.getIssueTypeId());
            case IssueFieldConstants.CREATED -> resolveDate(issue.getCreated());
            case IssueFieldConstants.UPDATED -> resolveDate(issue.getUpdated());
            case IssueFieldConstants.STATUS -> issue.getStatus().getName();
            case IssueFieldConstants.STATUS_CATEGORY -> issue.getStatus().getCategoryKey();
            case IssueFieldConstants.PRIORITY -> resolveId(issue.getPriorityId());
            default -> "";
        };
    }

    private String resolveId(Long id) {
        if (id == null) {
            return "EMPTY";
        }

        return id.toString();
    }

    private String resolveDate(LocalDateTime localDateTime) {
        return Constants.DATE_FORMAT.format(localDateTime);
    }

    private String resolveUser(User user) {
        if (user == null) {
            return "EMPTY";
        }

        return user.getLogin();
    }

    public record GratyFunction(String functionName, String value, String valueType, boolean list) {}
}
