package io.github.sanadhamadd.uptimemonitor.monitor;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * A URL that the application watches.
 *
 * The fields are private and there are no setters: the only way to change a
 * monitor is through {@link #update}, which applies the same rules as the
 * constructor, so a monitor can never hold invalid values.
 *
 * The expected keyword is optional. When it is set, the monitored page must
 * contain that text to count as up.
 */
@Entity
@Table(name = "monitors")
public class Monitor {

    public static final int MIN_INTERVAL_SECONDS = 10;
    public static final int MAX_NAME_LENGTH = 100;
    public static final int MAX_URL_LENGTH = 2000;
    public static final int MAX_KEYWORD_LENGTH = 200;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = MAX_NAME_LENGTH)
    private String name;
    @Column(nullable = false, length = MAX_URL_LENGTH)
    private String url;
    private int intervalSeconds;
    @Column(length = MAX_KEYWORD_LENGTH)
    private String expectedKeyword;

    /** Required by JPA. Application code uses the public constructor. */
    protected Monitor() {
    }

    /** Creates a monitor without an expected keyword. */
    public Monitor(String name, String url, int intervalSeconds) {
        this(name, url, intervalSeconds, null);
    }

    public Monitor(String name, String url, int intervalSeconds, String expectedKeyword) {
        update(name, url, intervalSeconds, expectedKeyword);
    }

    public void update(String name, String url, int intervalSeconds, String expectedKeyword) {
        if (name == null || name.isBlank()) {
            throw new InvalidMonitorException("The monitor name must not be blank");
        }
        if (url == null || !(url.startsWith("http://") || url.startsWith("https://"))) {
            throw new InvalidMonitorException("The monitor URL must start with http:// or https://");
        }
        if (!isCallable(url)) {
            throw new InvalidMonitorException("The monitor URL is not a valid web address");
        }
        if (expectedKeyword != null && expectedKeyword.length() > MAX_KEYWORD_LENGTH) {
            throw new InvalidMonitorException(
                    "The expected keyword must be at most " + MAX_KEYWORD_LENGTH + " characters");
        }
        if (intervalSeconds < MIN_INTERVAL_SECONDS) {
            throw new InvalidMonitorException(
                    "The check interval must be at least " + MIN_INTERVAL_SECONDS + " seconds");
        }
        this.name = name;
        this.url = url;
        this.intervalSeconds = intervalSeconds;
        // A blank keyword means the same as no keyword.
        this.expectedKeyword = (expectedKeyword == null || expectedKeyword.isBlank()) ? null : expectedKeyword;
    }

    /** True when the URL can be parsed and names a host, so an HTTP call to it is possible. */
    private static boolean isCallable(String url) {
        try {
            URI uri = new URI(url);
            return uri.getHost() != null && uri.getPort() <= 65535;
        } catch (URISyntaxException exception) {
            return false;
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public int getIntervalSeconds() {
        return intervalSeconds;
    }

    /** Returns the text the page must contain, or null when there is none. */
    public String getExpectedKeyword() {
        return expectedKeyword;
    }

    @Override
    public String toString() {
        return String.format("Monitor[id=%d, name='%s', url='%s', intervalSeconds=%d]",
                id, name, url, intervalSeconds);
    }
}
