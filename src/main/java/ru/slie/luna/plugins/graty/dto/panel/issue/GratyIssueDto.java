package ru.slie.luna.plugins.graty.dto.panel.issue;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import ru.slie.luna.issue.Issue;

import java.util.HashMap;
import java.util.Map;

public class GratyIssueDto {
    private final Long id;
    private final String key;
    private final Map<String, Object> fieldValues = new HashMap<>();

    public GratyIssueDto(Issue issue) {
        this.id = issue.getId();
        this.key = issue.getKey();
    }

    public Long getId() {
        return id;
    }

    public String getKey() {
        return key;
    }

    public void setFieldValue(String fieldId, Object value) {
        fieldValues.put(fieldId, value);
    }

    @JsonAnyGetter
    public Map<String, Object> getFieldValues() {
        return fieldValues;
    }
}
