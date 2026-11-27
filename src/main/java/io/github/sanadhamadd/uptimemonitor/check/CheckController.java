package io.github.sanadhamadd.uptimemonitor.check;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Serves the HTTP call that checks a monitor now.
 *
 * POST /api/monitors/{id}/check   check the monitor's URL   200 or 404
 *
 * The response is 200 whether the monitored URL is up or down: the call to
 * this application succeeded, and the body says what the check found.
 */
@RestController
public class CheckController {

    private final CheckService checkService;

    public CheckController(CheckService checkService) {
        this.checkService = checkService;
    }

    @PostMapping("/api/monitors/{id}/check")
    public CheckResult check(@PathVariable Long id) {
        return checkService.check(id);
    }
}
