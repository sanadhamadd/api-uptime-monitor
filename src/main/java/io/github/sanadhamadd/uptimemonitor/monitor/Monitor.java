package io.github.sanadhamadd.uptimemonitor.monitor;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * A URL that the application watches.
 *
 * The fields are private and there are no setters: the only way to change a
 * monitor is through {@link #update}, which applies the same rules as the
 * constructor, so a monitor can never hold invalid values.
 */
@Entity
@Table(name = "monitors")
public class Monitor {

    public static final int MIN_INTERVAL_SECONDS = 10;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String url;
    private int intervalSeconds;

    /** Required by JPA. Application code uses the public constructor. */
    protected Monitor() {
    }

    public Monitor(String name, String url, int intervalSeconds) {
        update(name, url, intervalSeconds);
    }

    public void update(String name, String url, int intervalSeconds) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("The monitor name must not be blank");
        }
        if (url == null || !(url.startsWith("http://") || url.startsWith("https://"))) {
            throw new IllegalArgumentException("The monitor URL must start with http:// or https://");
        }
        if (intervalSeconds < MIN_INTERVAL_SECONDS) {
            throw new IllegalArgumentException(
                    "The check interval must be at least " + MIN_INTERVAL_SECONDS + " seconds");
        }
        this.name = name;
        this.url = url;
        this.intervalSeconds = intervalSeconds;
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

    @Override
    public String toString() {
        return String.format("Monitor[id=%d, name='%s', url='%s', intervalSeconds=%d]",
                id, name, url, intervalSeconds);
    }
}
