package ru.slie.luna.plugins.graty.mapper;

import org.springframework.stereotype.Component;
import ru.slie.luna.db.SearchResultList;
import ru.slie.luna.plugins.graty.dto.PanelDto;
import ru.slie.luna.plugins.graty.dto.PanelValueDto;
import ru.slie.luna.plugins.graty.panel.Panel;
import ru.slie.luna.search.SearchResult;
import ru.slie.luna.user.UserManager;

@Component
public class PanelDtoMapper {
    private final UserManager userManager;

    public PanelDtoMapper(UserManager userManager) {
        this.userManager = userManager;
    }

    private static PanelValueDto wrapValue(Panel panel) {
        return PanelValueDto.from(panel);
    }

    public static SearchResult<PanelValueDto> map(SearchResult<Panel> searchResult) {
        return new SearchResultList<>(searchResult.getTotal(),
                searchResult.getResults().stream().map(PanelDtoMapper::wrapValue).toList(),
                searchResult.searchParams());
    }

    public PanelDto map(Panel panel) {
        if (panel == null) {
            return null;
        }

        return new PanelDto(panel, userManager);
    }
}
