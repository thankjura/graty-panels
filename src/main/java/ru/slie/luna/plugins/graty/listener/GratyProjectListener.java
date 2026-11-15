package ru.slie.luna.plugins.graty.listener;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import ru.slie.luna.event.ProjectDeletingEvent;
import ru.slie.luna.plugins.graty.panel.PanelManager;

/**
 * Keeps panel bindings consistent with existing projects.
 */
@Component
public class GratyProjectListener {
    private final PanelManager panelManager;

    public GratyProjectListener(PanelManager panelManager) {
        this.panelManager = panelManager;
    }

    @EventListener
    public void onProjectDeleting(ProjectDeletingEvent event) {
        panelManager.removeProjectFromPanels(event.getProject().getId());
    }
}
