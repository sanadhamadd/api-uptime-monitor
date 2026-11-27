package io.github.sanadhamadd.uptimemonitor.monitor;

/** The JSON the API returns for a monitor. */
public record MonitorResponse(Long id, String name, String url, int intervalSeconds, String expectedKeyword) {

    public static MonitorResponse from(Monitor monitor) {
        return new MonitorResponse(
                monitor.getId(), monitor.getName(), monitor.getUrl(),
                monitor.getIntervalSeconds(), monitor.getExpectedKeyword());
    }
}
