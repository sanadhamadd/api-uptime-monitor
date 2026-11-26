package io.github.sanadhamadd.uptimemonitor.monitor;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * The JSON body a client sends to create or replace a monitor.
 * A record is an immutable class: its fields are set once and never change.
 */
public record MonitorRequest(

        @NotBlank(message = "must not be blank")
        @Size(max = 100, message = "must be at most 100 characters")
        String name,

        @NotBlank(message = "must not be blank")
        @Size(max = 2000, message = "must be at most 2000 characters")
        @Pattern(regexp = "^https?://.+", message = "must start with http:// or https://")
        String url,

        @Min(value = Monitor.MIN_INTERVAL_SECONDS, message = "must be at least 10 seconds")
        @Max(value = 86400, message = "must be at most 86400 seconds (one day)")
        int intervalSeconds) {
}
