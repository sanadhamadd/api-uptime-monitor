package io.github.sanadhamadd.uptimemonitor.check;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.net.URI;

/**
 * A way of deciding whether a URL is up.
 *
 * This class does the part every check has in common: it makes the HTTP call
 * and measures how long it took. What counts as "up" differs per kind of
 * check, so that decision is left to the subclasses through the abstract
 * {@link #evaluate} method.
 */
public abstract class HealthCheck {

    private final RestClient restClient;

    protected HealthCheck(RestClient restClient) {
        this.restClient = restClient;
    }

    /**
     * Calls the URL with an HTTP GET and returns the outcome. This method never
     * throws: a URL that cannot be reached is a result, not an error.
     */
    public final CheckResult run(String url) {
        long startedAt = System.nanoTime();
        try {
            ResponseEntity<String> response = restClient.get()
                    .uri(URI.create(url))
                    .retrieve()
                    // By default RestClient throws on 4xx and 5xx. A check needs to
                    // look at those answers too, so every status is accepted here.
                    .onStatus(status -> true, (request, clientResponse) -> { })
                    .toEntity(String.class);

            String body = response.getBody() == null ? "" : response.getBody();
            return evaluate(response.getStatusCode().value(), body, millisSince(startedAt));
        } catch (RestClientException | IllegalArgumentException exception) {
            return CheckResult.noResponse(millisSince(startedAt), "No response: " + reasonOf(exception));
        }
    }

    /** Decides whether an answer with this status code and body counts as up. */
    protected abstract CheckResult evaluate(int statusCode, String body, long responseTimeMillis);

    /** True for the 2xx status codes, which mean the request succeeded. */
    protected boolean isSuccess(int statusCode) {
        return statusCode >= 200 && statusCode < 300;
    }

    private static long millisSince(long startedAt) {
        return (System.nanoTime() - startedAt) / 1_000_000;
    }

    /** Finds the original cause of a failed call, which names the real problem. */
    private static String reasonOf(Exception exception) {
        Throwable cause = exception;
        while (cause.getCause() != null) {
            cause = cause.getCause();
        }
        String name = cause.getClass().getSimpleName();
        return cause.getMessage() == null ? name : name + ": " + cause.getMessage();
    }
}
