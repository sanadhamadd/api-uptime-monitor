package io.github.sanadhamadd.uptimemonitor.monitor;

/**
 * Thrown when a monitor is given values it cannot hold, such as a blank name
 * or a URL that cannot be called.
 */
public class InvalidMonitorException extends IllegalArgumentException {

    public InvalidMonitorException(String message) {
        super(message);
    }
}
