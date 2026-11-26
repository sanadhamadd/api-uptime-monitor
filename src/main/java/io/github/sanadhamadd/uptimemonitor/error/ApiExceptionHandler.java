package io.github.sanadhamadd.uptimemonitor.error;

import io.github.sanadhamadd.uptimemonitor.monitor.MonitorNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

/** Turns exceptions into clear HTTP error responses for every controller. */
@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(MonitorNotFoundException.class)
    public ProblemDetail handleNotFound(MonitorNotFoundException exception) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleInvalidBody(MethodArgumentNotValidException exception) {
        Map<String, String> errors = new LinkedHashMap<>();
        for (FieldError fieldError : exception.getBindingResult().getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST, "The request body is not valid");
        problem.setProperty("errors", errors);
        return problem;
    }
}
