package ru.slie.luna.plugins.graty.imports;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.slie.luna.locale.I18nResolver;
import ru.slie.luna.plugins.graty.panel.PanelManager;
import ru.slie.luna.system.restore.project.BackupTableReader;
import ru.slie.luna.system.restore.project.ProjectImportContext;
import ru.slie.luna.system.restore.project.ProjectImportParticipant;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Panels are global configuration bound to projects by id. When a project is imported from a backup
 * it gets a new id, so panels which were bound to the project in the backup are bound to the imported
 * project. Panels are matched by name; panels missing in this system are skipped.
 */
@Component
public class GratyProjectImportParticipant implements ProjectImportParticipant {
    private static final Logger log = LoggerFactory.getLogger(GratyProjectImportParticipant.class);
    static final String PANELS_TABLE = "panels";

    private final PanelManager panelManager;
    private final I18nResolver i18n;

    public GratyProjectImportParticipant(PanelManager panelManager, I18nResolver i18n) {
        this.panelManager = panelManager;
        this.i18n = i18n;
    }

    @Override
    public String getName() {
        return i18n.getText("graty.import.participant.name");
    }

    @Override
    public void importProject(ProjectImportContext context) throws Exception {
        Optional<BackupTableReader> reader = context.read(PANELS_TABLE);
        if (reader.isEmpty()) {
            // plugin was not installed when the backup was made
            log.info("Project {} import: no graty panels table in the backup", context.getTargetProject().getKey());
            return;
        }

        long oldProjectId = context.getBackupProject().getId();
        Set<String> panelNames = new HashSet<>();
        reader.get().filter(row -> containsProject(row.get("project_ids", Long[].class), oldProjectId));
        while (reader.get().hasNext()) {
            String name = reader.get().next().get("name", String.class);
            if (name != null) {
                panelNames.add(name);
            }
        }

        log.info("Project {} import: graty panels bound to backup project {}: {}", context.getTargetProject().getKey(), oldProjectId, panelNames);
        Set<String> bound = panelManager.addProjectToPanels(panelNames, context.getTargetProject().getId());
        context.progress(i18n.getText("graty.import.panels_bound", String.valueOf(bound.size()), String.valueOf(panelNames.size())));

        Set<String> missing = new HashSet<>(panelNames);
        missing.removeAll(bound);
        if (!missing.isEmpty()) {
            log.warn("Project {} import: graty panels not found and skipped: {}", context.getTargetProject().getKey(), missing);
        }
    }

    static boolean containsProject(Long[] projectIds, long projectId) {
        return projectIds != null && Arrays.asList(projectIds).contains(projectId);
    }
}
