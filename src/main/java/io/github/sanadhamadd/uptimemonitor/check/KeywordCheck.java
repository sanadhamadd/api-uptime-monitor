package io.github.sanadhamadd.uptimemonitor.check;

import org.springframework.web.client.RestClient;

/**
 * A URL is up when it answers with a 2xx status code and the page contains
 * the expected keyword. This catches a page that loads but shows the wrong
 * content, such as an error page served with status 200.
 */
public class KeywordCheck extends HealthCheck {

    private final String keyword;

    public KeywordCheck(RestClient restClient, String keyword) {
        super(restClient);
        if (keyword == null || keyword.isBlank()) {
            throw new IllegalArgumentException("The keyword must not be blank");
        }
        this.keyword = keyword;
    }

    @Override
    protected CheckResult evaluate(int statusCode, String body, long responseTimeMillis) {
        if (!isSuccess(statusCode)) {
            return CheckResult.down(statusCode, responseTimeMillis,
                    "Expected a 2xx status but got " + statusCode);
        }
        if (!body.contains(keyword)) {
            return CheckResult.down(statusCode, responseTimeMillis,
                    "The page does not contain '" + keyword + "'");
        }
        return CheckResult.up(statusCode, responseTimeMillis,
                "Answered with status " + statusCode + " and contains '" + keyword + "'");
    }
}
