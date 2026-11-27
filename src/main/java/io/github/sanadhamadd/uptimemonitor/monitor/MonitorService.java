package io.github.sanadhamadd.uptimemonitor.monitor;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/** The operations the application offers on monitors. */
@Service
public class MonitorService {

    private final MonitorRepository monitorRepository;

    public MonitorService(MonitorRepository monitorRepository) {
        this.monitorRepository = monitorRepository;
    }

    public List<Monitor> findAll() {
        return monitorRepository.findAll(Sort.by("id"));
    }

    public Monitor findById(Long id) {
        return monitorRepository.findById(id)
                .orElseThrow(() -> new MonitorNotFoundException(id));
    }

    public Monitor create(String name, String url, int intervalSeconds, String expectedKeyword) {
        return monitorRepository.save(new Monitor(name, url, intervalSeconds, expectedKeyword));
    }

    public Monitor update(Long id, String name, String url, int intervalSeconds, String expectedKeyword) {
        Monitor monitor = findById(id);
        monitor.update(name, url, intervalSeconds, expectedKeyword);
        return monitorRepository.save(monitor);
    }

    public void delete(Long id) {
        monitorRepository.delete(findById(id));
    }
}
