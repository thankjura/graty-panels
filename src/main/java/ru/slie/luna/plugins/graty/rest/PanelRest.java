package ru.slie.luna.plugins.graty.rest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.slie.luna.db.query.DeleteResult;
import ru.slie.luna.permission.GlobalPermissionManager;
import ru.slie.luna.plugins.graty.dto.PanelDto;
import ru.slie.luna.plugins.graty.dto.PanelValueDto;
import ru.slie.luna.plugins.graty.mapper.PanelDtoMapper;
import ru.slie.luna.plugins.graty.panel.Panel;
import ru.slie.luna.plugins.graty.panel.PanelManager;
import ru.slie.luna.plugins.graty.rest.request.PanelRequest;
import ru.slie.luna.plugins.graty.rest.request.SearchTermRequest;
import ru.slie.luna.search.SearchResult;
import ru.slie.luna.security.AuthenticationContext;
import ru.slie.luna.user.User;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/graty/panels")
public class PanelRest {
    private final PanelManager panelManager;
    private final AuthenticationContext authenticationContext;
    private final GlobalPermissionManager globalPermissionManager;
    private final PanelDtoMapper panelDtoMapper;

    public PanelRest(PanelManager panelManager,
                     AuthenticationContext authenticationContext,
                     GlobalPermissionManager globalPermissionManager,
                     PanelDtoMapper panelDtoMapper) {
        this.panelManager = panelManager;
        this.authenticationContext = authenticationContext;
        this.globalPermissionManager = globalPermissionManager;
        this.panelDtoMapper = panelDtoMapper;
    }

    @GetMapping({"/", ""})
    public SearchResult<PanelValueDto> search(SearchTermRequest request) {
        User user = authenticationContext.getCurrentUser();
        if (!globalPermissionManager.hasAdminPerms(user)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        return PanelDtoMapper.map(panelManager.search(request.getSearchParams()));
    }

    @PostMapping({"/", ""})
    public PanelDto create(@RequestBody PanelRequest request) {
        User user = authenticationContext.getCurrentUser();
        if (!globalPermissionManager.hasAdminPerms(user)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        return panelDtoMapper.map(panelManager.create(user, request));
    }

    @GetMapping("/{id}")
    public PanelDto get(@PathVariable("id") Long panelId) {
        User user = authenticationContext.getCurrentUser();
        if (!globalPermissionManager.hasAdminPerms(user)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        Panel panel = getPanel(panelId);
        return panelDtoMapper.map(panel);
    }

    @PatchMapping("/{id}")
    public PanelDto patch(@PathVariable("id") Long panelId, @RequestBody Map<String, Object> request) {
        User user = authenticationContext.getCurrentUser();
        if (!globalPermissionManager.hasAdminPerms(user)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        Panel panel = getPanel(panelId);
        panelManager.patch(user, panel, request);
        return panelDtoMapper.map(panel);
    }

    @PutMapping("/{id}")
    public PanelDto put(@PathVariable("id") Long panelId, @RequestBody PanelRequest request) {
        User user = authenticationContext.getCurrentUser();
        if (!globalPermissionManager.hasAdminPerms(user)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        Panel panel = getPanel(panelId);
        panelManager.update(user, panel, request);
        return panelDtoMapper.map(panel);
    }

    @DeleteMapping("/{id}")
    public DeleteResult delete(@PathVariable("id") Long panelId) {
        User user = authenticationContext.getCurrentUser();
        if (!globalPermissionManager.hasAdminPerms(user)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        Optional<Panel> panel = panelManager.getById(panelId);
        if (panel.isEmpty()) {
            return DeleteResult.EMPTY;
        }

        return panelManager.delete(panel.get());
    }

    private Panel getPanel(Long panelId) {
        Optional<Panel> panel = panelManager.getById(panelId);
        if (panel.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return panel.get();
    }
}
