# API Uptime Monitor

[![CI](https://github.com/sanadhamadd/api-uptime-monitor/actions/workflows/ci.yml/badge.svg)](https://github.com/sanadhamadd/api-uptime-monitor/actions/workflows/ci.yml)

A Spring Boot application that monitors the availability of HTTP endpoints and serves the results through a REST API.

> Work in progress. This is the initial project skeleton.

## Requirements

- Java 17 or newer

Maven is not required: the project includes the Maven Wrapper.

## Run

```bash
./mvnw spring-boot:run
```

The application starts on `http://localhost:8080`.

## Test

```bash
./mvnw verify
```

## Continuous integration

Every pull request is built and tested on Java 17 and Java 21 with GitHub Actions.

## Author

Sanad Hamad

## License

Released under the [MIT License](LICENSE).
