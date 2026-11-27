package io.github.sanadhamadd.uptimemonitor.monitor;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * The JSON body a client sends to create or replace a monitor.
 * A record is an immutable class: its fields are set once and never change.
 * The expected keyword is optional and may be left out.
 */
public record MonitorRequest(

        @NotBlank(message = "must not be blank")
        @Size(max = Monitor.MAX_NAME_LENGTH, message = "must be at most {max} characters")
        String name,

        @NotBlank(message = "must not be blank")
        @Size(max = Monitor.MAX_URL_LENGTH, message = "must be at most {max} characters")
        @Pattern(regexp = "^https?://\\S+$", message = "must start with http:// or https:// and contain no spaces")
        String url,

        @Min(value = Monitor.MIN_INTERVAL_SECONDS, message = "must be at least {value} seconds")
        @Max(value = 86400, message = "must be at most 86400 seconds (one day)")
        int intervalSeconds,

        @Size(max = Monitor.MAX_KEYWORD_LENGTH, message = "must be at most {max} characters")
        String expectedKeyword) {
}
