package io.github.sanadhamadd.uptimemonitor.monitor;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * Adds one sample monitor when the application starts with an empty database.
 *
 * The sample watches this application's own /hello endpoint, so the
 * application can be tried straight away, without an internet connection.
 */
@Component
public class SampleMonitorLoader implements ApplicationRunner {

    private final MonitorRepository monitorRepository;
    private final Environment environment;

    public SampleMonitorLoader(MonitorRepository monitorRepository, Environment environment) {
        this.monitorRepository = monitorRepository;
        this.environment = environment;
    }

    @Override
    public void run(ApplicationArguments arguments) {
        if (monitorRepository.count() > 0) {
            return;
        }
        // "local.server.port" is the port the server really started on.
        String port = environment.getProperty("local.server.port",
                environment.getProperty("server.port", "8080"));
        monitorRepository.save(new Monitor(
                "This application", "http://localhost:" + port + "/hello", 60, "Hello"));
    }
}
