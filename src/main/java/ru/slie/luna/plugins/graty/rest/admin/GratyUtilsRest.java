package ru.slie.luna.plugins.graty.rest.admin;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.slie.luna.plugins.graty.util.GratyUtils;

import java.util.List;

@RestController
@RequestMapping("/graty/utils")
public class GratyUtilsRest {
    private final GratyUtils gratyUtils;

    public GratyUtilsRest(GratyUtils gratyUtils) {
        this.gratyUtils = gratyUtils;
    }

    @GetMapping("/functions")
    public List<GratyUtils.GratyFunction> getFunctions() {
        return gratyUtils.getQsFunctions();
    }
}
