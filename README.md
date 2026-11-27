# API Uptime Monitor

[![CI](https://github.com/sanadhamadd/api-uptime-monitor/actions/workflows/ci.yml/badge.svg)](https://github.com/sanadhamadd/api-uptime-monitor/actions/workflows/ci.yml)

A Spring Boot application that monitors the availability of HTTP endpoints and serves the results through a REST API.

> Work in progress. Monitors can be created, listed, updated, deleted and checked on demand through the REST API. Checking them automatically on a schedule is the next step.

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
| POST | `/api/monitors/{id}/check` | Calls the monitor's URL now and returns the result | 200, 404 |

A monitor has a `name`, a `url` that starts with `http://` or `https://`, and an `intervalSeconds` between 10 and 86400. It can also have an `expectedKeyword`: text the page must contain.

Monitors are stored in an in-memory database, so they are cleared when the application stops.

## How a check works

A check sends an HTTP GET to the monitor's URL and waits at most 5 seconds.

- Without an `expectedKeyword`, the URL is up when it answers with a 2xx status code.
- With an `expectedKeyword`, the URL is up when it answers with a 2xx status code and the page contains that text.

The check endpoint answers `200` whether the URL is up or down. The result is in the body: `up`, `statusCode`, `responseTimeMillis`, `message` and `checkedAt`. When the URL does not answer at all, `statusCode` is `null`.

The application starts with one sample monitor, with id 1, that watches the application's own `/hello` endpoint. Checking it makes the application call itself over HTTP, so it works without an internet connection.

## How to call the endpoints

With the application running, open `http://localhost:8080/hello` in a browser, or use `curl`:

```bash
curl "http://localhost:8080/hello?name=Sanad"
```

List all monitors. The list contains the sample monitor:

```bash
curl http://localhost:8080/api/monitors
```

Check the sample monitor. The application calls its own `/hello` endpoint and reports it as up:

```bash
curl -X POST http://localhost:8080/api/monitors/1/check
```

Create a monitor. It gets id 2:

```bash
curl -i -X POST http://localhost:8080/api/monitors -H "Content-Type: application/json" -d '{"name": "Missing page", "url": "http://localhost:8080/no-such-page", "intervalSeconds": 60}'
```

Get it, then check it. The page does not exist, so the check reports it as down with status 404:

```bash
curl http://localhost:8080/api/monitors/2
```

```bash
curl -X POST http://localhost:8080/api/monitors/2/check
```

Replace it with a monitor that expects a keyword, and check again. The page answers, but it does not contain the keyword, so it is still down:

```bash
curl -X PUT http://localhost:8080/api/monitors/2 -H "Content-Type: application/json" -d '{"name": "Wrong content", "url": "http://localhost:8080/hello", "intervalSeconds": 120, "expectedKeyword": "Goodbye"}'
```

```bash
curl -X POST http://localhost:8080/api/monitors/2/check
```

Delete it:

```bash
curl -i -X DELETE http://localhost:8080/api/monitors/2
```

Invalid input returns `400` with the reason for each field, and an unknown id returns `404`:

```bash
curl -X POST http://localhost:8080/api/monitors -H "Content-Type: application/json" -d '{"name": "", "url": "example.com", "intervalSeconds": 1}'
```

```bash
curl -X POST http://localhost:8080/api/monitors/999/check
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
