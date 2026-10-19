package ru.slie.luna.plugins.graty.rest.admin;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.slie.luna.db.query.DeleteResult;
import ru.slie.luna.exception.ValidateException;
import ru.slie.luna.permission.GlobalPermissionManager;
import ru.slie.luna.plugins.graty.dto.admin.PanelDto;
import ru.slie.luna.plugins.graty.mapper.PanelDtoMapper;
import ru.slie.luna.plugins.graty.panel.Panel;
import ru.slie.luna.plugins.graty.panel.PanelManager;
import ru.slie.luna.plugins.graty.rest.admin.request.PanelRequest;
import ru.slie.luna.plugins.graty.rest.request.SearchPanelsRequest;
import ru.slie.luna.search.SearchResult;
import ru.slie.luna.security.AuthenticationContext;
import ru.slie.luna.user.User;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/graty/admin/panels")
public class AdminPanelRest {
    private final PanelManager panelManager;
    private final AuthenticationContext authenticationContext;
    private final GlobalPermissionManager globalPermissionManager;
    private final PanelDtoMapper panelDtoMapper;

    public AdminPanelRest(PanelManager panelManager,
                          AuthenticationContext authenticationContext,
                          GlobalPermissionManager globalPermissionManager,
                          PanelDtoMapper panelDtoMapper) {
        this.panelManager = panelManager;
        this.authenticationContext = authenticationContext;
        this.globalPermissionManager = globalPermissionManager;
        this.panelDtoMapper = panelDtoMapper;
    }

    @GetMapping({"/", ""})
    public SearchResult<PanelDto> search(SearchPanelsRequest request) {
        User user = authenticationContext.getCurrentUser();
        if (!globalPermissionManager.hasAdminPerms(user)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        return panelDtoMapper.map(panelManager.search(request.getSearchParams()));
    }

    @PostMapping({"/", ""})
    public PanelDto create(@RequestBody PanelRequest request) throws ValidateException {
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
    public PanelDto patch(@PathVariable("id") Long panelId, @RequestBody Map<String, Object> request) throws ValidateException {
        User user = authenticationContext.getCurrentUser();
        if (!globalPermissionManager.hasAdminPerms(user)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        Panel panel = getPanel(panelId);
        panelManager.patch(user, panel, request);
        return panelDtoMapper.map(panel);
    }

    @PutMapping("/{id}")
    public PanelDto put(@PathVariable("id") Long panelId, @RequestBody PanelRequest request) throws ValidateException {
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
