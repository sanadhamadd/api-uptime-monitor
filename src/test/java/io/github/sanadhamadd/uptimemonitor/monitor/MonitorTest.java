package io.github.sanadhamadd.uptimemonitor.monitor;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
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
                () -> monitor.update("Changed", "not-a-url", 30, null));

        assertEquals("Example", monitor.getName());
        assertEquals(60, monitor.getIntervalSeconds());
    }

    @Test
    void hasNoExpectedKeywordUnlessOneIsGiven() {
        assertNull(new Monitor("Example", "https://example.com", 60).getExpectedKeyword());
        assertEquals("Welcome",
                new Monitor("Example", "https://example.com", 60, "Welcome").getExpectedKeyword());
    }

    @Test
    void treatsABlankKeywordAsNoKeyword() {
        assertNull(new Monitor("Example", "https://example.com", 60, "  ").getExpectedKeyword());
    }

    @Test
    void rejectsAKeywordThatIsTooLong() {
        String tooLong = "a".repeat(Monitor.MAX_KEYWORD_LENGTH + 1);

        assertThrows(IllegalArgumentException.class,
                () -> new Monitor("Example", "https://example.com", 60, tooLong));
    }

    @Test
    void rejectsAUrlThatCanNeverBeCalled() {
        assertThrows(InvalidMonitorException.class,
                () -> new Monitor("Bad port", "http://localhost:99999/", 60));
        assertThrows(InvalidMonitorException.class,
                () -> new Monitor("Bad character", "http://localhost:8080/hello?name=a|b", 60));
        assertThrows(InvalidMonitorException.class,
                () -> new Monitor("No host", "http://", 60));
    }
}
