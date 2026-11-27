package io.github.sanadhamadd.uptimemonitor.check;

import org.springframework.web.client.RestClient;

/** A URL is up when it answers with a 2xx status code. */
public class StatusCodeCheck extends HealthCheck {

    public StatusCodeCheck(RestClient restClient) {
        super(restClient);
    }

    @Override
    protected CheckResult evaluate(int statusCode, String body, long responseTimeMillis) {
        if (isSuccess(statusCode)) {
            return CheckResult.up(statusCode, responseTimeMillis, "Answered with status " + statusCode);
        }
        return CheckResult.down(statusCode, responseTimeMillis,
                "Expected a 2xx status but got " + statusCode);
    }
}
