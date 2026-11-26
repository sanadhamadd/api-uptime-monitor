package io.github.sanadhamadd.uptimemonitor.monitor;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MonitorTest {

    @Test
    void keepsTheValuesItWasCreatedWith() {
        Monitor monitor = new Monitor("Example", "https://example.com", 60);

        assertEquals("Example", monitor.getName());
        assertEquals("https://example.com", monitor.getUrl());
        assertEquals(60, monitor.getIntervalSeconds());
    }

    @Test
    void rejectsABlankName() {
        assertThrows(IllegalArgumentException.class,
                () -> new Monitor(" ", "https://example.com", 60));
    }

    @Test
    void rejectsAUrlThatIsNotHttp() {
        assertThrows(IllegalArgumentException.class,
                () -> new Monitor("Example", "ftp://example.com", 60));
    }

    @Test
    void rejectsAnIntervalBelowTheMinimum() {
        assertThrows(IllegalArgumentException.class,
                () -> new Monitor("Example", "https://example.com", Monitor.MIN_INTERVAL_SECONDS - 1));
    }

    @Test
    void anInvalidUpdateLeavesTheMonitorUnchanged() {
        Monitor monitor = new Monitor("Example", "https://example.com", 60);

        assertThrows(IllegalArgumentException.class,
                () -> monitor.update("Changed", "not-a-url", 30));

        assertEquals("Example", monitor.getName());
        assertEquals(60, monitor.getIntervalSeconds());
    }
}
