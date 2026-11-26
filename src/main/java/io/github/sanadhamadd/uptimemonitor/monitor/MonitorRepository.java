package io.github.sanadhamadd.uptimemonitor.monitor;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MonitorRepository extends JpaRepository<Monitor, Long> {
}
