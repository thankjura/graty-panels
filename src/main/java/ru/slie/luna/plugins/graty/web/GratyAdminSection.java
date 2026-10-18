package ru.slie.luna.plugins.graty.web;

import org.springframework.stereotype.Component;
import ru.slie.luna.locale.I18nResolver;
import ru.slie.luna.web.WebContext;
import ru.slie.luna.web.WebSection;

@Component
public class GratyAdminSection implements WebSection {
    private final I18nResolver i18n;

    public GratyAdminSection(I18nResolver i18n) {
        this.i18n = i18n;
    }

    @Override
    public String getId() {
        return "graty-admin";
    }

    @Override
    public String getLocation() {
        return "admin/plugins";
    }

    @Override
    public int getOrder() {
        return 300;
    }

    @Override
    public boolean isVisible(WebContext context) {
        return true;
    }

    @Override
    public String getName() {
        return i18n.getText("graty.web.section.admin.label");
    }
}
