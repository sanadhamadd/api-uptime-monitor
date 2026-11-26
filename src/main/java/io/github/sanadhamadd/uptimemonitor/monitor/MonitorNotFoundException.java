package io.github.sanadhamadd.uptimemonitor.monitor;

/** Thrown when a monitor is requested by an id that does not exist. */
public class MonitorNotFoundException extends RuntimeException {

    public MonitorNotFoundException(Long id) {
        super("No monitor exists with id " + id);
    }
}
