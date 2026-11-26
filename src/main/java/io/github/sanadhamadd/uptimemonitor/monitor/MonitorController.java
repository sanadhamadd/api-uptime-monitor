package io.github.sanadhamadd.uptimemonitor.monitor;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

/**
 * Serves the HTTP calls for monitors.
 *
 * GET    /api/monitors        list all monitors
 * GET    /api/monitors/{id}   get one monitor            200 or 404
 * POST   /api/monitors        create a monitor           201 or 400
 * PUT    /api/monitors/{id}   replace a monitor          200, 400 or 404
 * DELETE /api/monitors/{id}   delete a monitor           204 or 404
 */
@RestController
@RequestMapping("/api/monitors")
public class MonitorController {

    private final MonitorService monitorService;

    public MonitorController(MonitorService monitorService) {
        this.monitorService = monitorService;
    }

    @GetMapping
    public List<MonitorResponse> list() {
        return monitorService.findAll().stream()
                .map(MonitorResponse::from)
                .toList();
    }

    @GetMapping("/{id}")
    public MonitorResponse get(@PathVariable Long id) {
        return MonitorResponse.from(monitorService.findById(id));
    }

    @PostMapping
    public ResponseEntity<MonitorResponse> create(@Valid @RequestBody MonitorRequest request) {
        Monitor created = monitorService.create(
                request.name(), request.url(), request.intervalSeconds());
        URI location = URI.create("/api/monitors/" + created.getId());
        return ResponseEntity.created(location).body(MonitorResponse.from(created));
    }

    @PutMapping("/{id}")
    public MonitorResponse update(@PathVariable Long id, @Valid @RequestBody MonitorRequest request) {
        Monitor updated = monitorService.update(
                id, request.name(), request.url(), request.intervalSeconds());
        return MonitorResponse.from(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        monitorService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
