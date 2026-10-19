package ru.slie.luna.plugins.graty.rest.admin;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.slie.luna.db.query.DeleteResult;
import ru.slie.luna.exception.ValidateException;
import ru.slie.luna.issue.field.FieldManager;
import ru.slie.luna.issue.field.IssueField;
import ru.slie.luna.permission.GlobalPermissionManager;
import ru.slie.luna.plugins.graty.dto.admin.PanelRenderDto;
import ru.slie.luna.plugins.graty.dto.admin.PanelRenderValueDto;
import ru.slie.luna.plugins.graty.mapper.PanelDtoMapper;
import ru.slie.luna.plugins.graty.panel.PanelRender;
import ru.slie.luna.plugins.graty.panel.PanelRenderManager;
import ru.slie.luna.plugins.graty.rest.admin.request.PanelRenderRequest;
import ru.slie.luna.plugins.graty.rest.request.SearchTermRequest;
import ru.slie.luna.search.SearchResult;
import ru.slie.luna.security.AuthenticationContext;
import ru.slie.luna.user.User;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/graty/admin/renders")
public class AdminRenderRest {
    private final PanelRenderManager renderManager;
    private final AuthenticationContext authenticationContext;
    private final GlobalPermissionManager globalPermissionManager;
    private final PanelDtoMapper mapper;
    private final FieldManager fieldManager;

    public AdminRenderRest(PanelRenderManager renderManager,
                           AuthenticationContext authenticationContext,
                           GlobalPermissionManager globalPermissionManager,
                           PanelDtoMapper mapper,
                           FieldManager fieldManager) {
        this.renderManager = renderManager;
        this.authenticationContext = authenticationContext;
        this.globalPermissionManager = globalPermissionManager;
        this.mapper = mapper;
        this.fieldManager = fieldManager;
    }

    @GetMapping({"/", ""})
    public SearchResult<PanelRenderValueDto> search(SearchTermRequest request) {
        User user = authenticationContext.getCurrentUser();
        if (!globalPermissionManager.hasAdminPerms(user)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        return mapper.mapRenders(renderManager.search(request.getTerm(), request.getSearchParams()));
    }

    @PostMapping({"/", ""})
    public PanelRenderDto create(@RequestBody PanelRenderRequest request) throws ValidateException {
        User user = authenticationContext.getCurrentUser();
        if (!globalPermissionManager.hasAdminPerms(user)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        List<IssueField> fields = fieldManager.getByIds(request.getFields());

        return PanelRenderDto.from(renderManager.create(request.getName(), request.getDescription(), fields, request.getLimit()));
    }

    @GetMapping("/{id}")
    public PanelRenderDto get(@PathVariable("id") Long renderId) {
        User user = authenticationContext.getCurrentUser();
        if (!globalPermissionManager.hasAdminPerms(user)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        PanelRender render = getRender(renderId);
        return PanelRenderDto.from(render);
    }

    @PatchMapping("/{id}")
    public PanelRenderDto patch(@PathVariable("id") Long renderId, @RequestBody Map<String, Object> request) throws ValidateException {
        User user = authenticationContext.getCurrentUser();
        if (!globalPermissionManager.hasAdminPerms(user)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        PanelRender render = getRender(renderId);
        renderManager.patch(render, request);
        return PanelRenderDto.from(render);
    }

    @PutMapping("/{id}")
    public PanelRenderDto put(@PathVariable("id") Long renderId, @RequestBody PanelRenderRequest request) throws ValidateException {
        User user = authenticationContext.getCurrentUser();
        if (!globalPermissionManager.hasAdminPerms(user)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        PanelRender render = getRender(renderId);

        List<IssueField> fields = fieldManager.getByIds(request.getFields());
        renderManager.update(render, render.getName(), render.getDescription(), fields, request.getLimit());
        return PanelRenderDto.from(render);
    }

    @DeleteMapping("/{id}")
    public DeleteResult delete(@PathVariable("id") Long renderId) {
        User user = authenticationContext.getCurrentUser();
        if (!globalPermissionManager.hasAdminPerms(user)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        Optional<PanelRender> render = renderManager.getById(renderId);
        if (render.isEmpty()) {
            return DeleteResult.EMPTY;
        }

        return renderManager.delete(render.get());
    }

    private PanelRender getRender(Long renderId) {
        Optional<PanelRender> render = renderManager.getById(renderId);
        if (render.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return render.get();
    }
}
