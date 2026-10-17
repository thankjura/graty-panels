package ru.slie.luna.plugins.graty.web;

import ru.slie.luna.locale.I18nResolver;
import ru.slie.luna.plugins.graty.panel.PanelManager;
import ru.slie.luna.web.WebContext;
import ru.slie.luna.web.WebPanel;
import ru.slie.luna.web.WebSection;

import java.util.List;

public class GratyWebPanel implements WebPanel {
    private final I18nResolver i18n;
    private final PanelManager panelManager;

    public GratyWebPanel(I18nResolver i18n,
                         PanelManager panelManager) {
        this.i18n = i18n;
        this.panelManager = panelManager;
    }

    @Override
    public boolean isVisible(WebContext context) {
        if (context.getIssue() == null) {
            return false;
        }

        return panelManager.hasForContext(context.getIssue());
    }

    @Override
    public String getName() {
        return i18n.getText("graty.panel.name");
    }

    @Override
    public Object getViewParams(WebContext context) {
        if (context.getIssue() != null) {
            return context.getIssue().getKey();
        }

        return null;
    }

    @Override
    public List<Action> getActions() {
        return List.of();
    }

    @Override
    public String getId() {
        return "graty-panel";
    }

    @Override
    public String getLocation() {
        return WebSection.ISSUE_MAIN_SECTION;
    }

    @Override
    public int getOrder() {
        return 300;
    }
}
