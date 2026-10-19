package ru.slie.luna.plugins.graty.dto.admin;

import com.fasterxml.jackson.annotation.JsonFormat;
import ru.slie.luna.plugins.graty.Constants;
import ru.slie.luna.plugins.graty.panel.Panel;
import ru.slie.luna.user.UserManager;
import ru.slie.luna.user.dto.UserValueDto;

import java.time.LocalDateTime;
import java.util.Set;

public class PanelDto extends PanelValueDto {
    private final LocalDateTime created;
    private final LocalDateTime updated;
    private final UserValueDto author;
    private final UserValueDto updater;
    private final String qs;
    private final Set<Long> projectIds;
    private final Set<Long> issueTypeIds;

    public PanelDto(Panel panel, UserManager userManager) {
        super(panel);
        this.created = panel.getCreated();
        this.updated = panel.getUpdated();
        this.author = UserValueDto.from(userManager.getById(panel.getAuthorId()).orElse(null));
        this.updater = UserValueDto.from(userManager.getById(panel.getUpdaterId()).orElse(null));
        this.qs = panel.getQs();
        this.projectIds = panel.getProjectIds();
        this.issueTypeIds = panel.getIssueTypeIds();
    }

    @JsonFormat(pattern = Constants.DATE_TIME_FORMAT)
    public LocalDateTime getCreated() {
        return created;
    }

    @JsonFormat(pattern = Constants.DATE_TIME_FORMAT)
    public LocalDateTime getUpdated() {
        return updated;
    }

    public UserValueDto getAuthor() {
        return author;
    }

    public UserValueDto getUpdater() {
        return updater;
    }

    public String getQs() {
        return qs;
    }

    public Set<Long> getProjectIds() {
        return projectIds;
    }

    public Set<Long> getIssueTypeIds() {
        return issueTypeIds;
    }
}
