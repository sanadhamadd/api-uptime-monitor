# API Uptime Monitor

[![CI](https://github.com/sanadhamadd/api-uptime-monitor/actions/workflows/ci.yml/badge.svg)](https://github.com/sanadhamadd/api-uptime-monitor/actions/workflows/ci.yml)

A Spring Boot application that monitors the availability of HTTP endpoints and serves the results through a REST API.

> Work in progress. Monitors can be created, listed, updated and deleted through the REST API. Checking the monitored URLs is the next step.

## Requirements

- Java 17 or newer

Maven is not required: the project includes the Maven Wrapper.

## Run

```bash
./mvnw spring-boot:run
```

The application starts on `http://localhost:8080`.

## Endpoints

| Method | Path | What it does | Responses |
| ------ | ---- | ------------ | --------- |
| GET | `/hello` | Returns a greeting; accepts an optional `name` parameter | 200 |
| GET | `/api/monitors` | Lists all monitors | 200 |
| GET | `/api/monitors/{id}` | Returns one monitor | 200, 404 |
| POST | `/api/monitors` | Creates a monitor | 201, 400 |
| PUT | `/api/monitors/{id}` | Replaces a monitor | 200, 400, 404 |
| DELETE | `/api/monitors/{id}` | Deletes a monitor | 204, 404 |

A monitor has a `name`, a `url` that starts with `http://` or `https://`, and an `intervalSeconds` between 10 and 86400.

Monitors are stored in an in-memory database, so they are cleared when the application stops.

## How to call the endpoints

With the application running, open `http://localhost:8080/hello` in a browser, or use `curl`:

```bash
curl "http://localhost:8080/hello?name=Sanad"
```

Create a monitor:

```bash
curl -i -X POST http://localhost:8080/api/monitors -H "Content-Type: application/json" -d '{"name": "Example", "url": "https://example.com", "intervalSeconds": 60}'
```

List all monitors, then get the one just created:

```bash
curl http://localhost:8080/api/monitors
```

```bash
curl http://localhost:8080/api/monitors/1
```

Replace it:

```bash
curl -X PUT http://localhost:8080/api/monitors/1 -H "Content-Type: application/json" -d '{"name": "Example (renamed)", "url": "https://example.org", "intervalSeconds": 120}'
```

Delete it:

```bash
curl -i -X DELETE http://localhost:8080/api/monitors/1
```

Invalid input returns `400` with the reason for each field, and an unknown id returns `404`:

```bash
curl -X POST http://localhost:8080/api/monitors -H "Content-Type: application/json" -d '{"name": "", "url": "example.com", "intervalSeconds": 1}'
```

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
