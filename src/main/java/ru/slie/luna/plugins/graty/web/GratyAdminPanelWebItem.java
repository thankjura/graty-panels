package ru.slie.luna.plugins.graty.web;

import org.springframework.stereotype.Component;
import ru.slie.luna.locale.I18nResolver;
import ru.slie.luna.web.WebContext;
import ru.slie.luna.web.WebItem;

import java.util.Map;

@Component
public class GratyAdminPanelWebItem implements WebItem {
    private final I18nResolver i18n;

    public GratyAdminPanelWebItem(I18nResolver i18n) {
        this.i18n = i18n;
    }

    @Override
    public String getId() {
        return "graty-panel-list";
    }

    @Override
    public String getLocation() {
        return "admin/plugins/graty-admin";
    }

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public boolean isVisible(WebContext context) {
        return true;
    }

    @Override
    public String getName() {
        return i18n.getText("graty.web.item.admin.panels.label");
    }

    @Override
    public String getRouteName() {
        return "adminGratyPanels";
    }

    @Override
    public Map<String, String> getRouteParams() {
        return Map.of();
    }
}
