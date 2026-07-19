package com.test_case.financial_transactions_ms.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Objects;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handle(MethodArgumentNotValidException ex) {
        log.error("[GLOBAL_EXCEPTION_HANDLER] Workflow failed due to {}", ex.getClass(), ex);
        ProblemDetail problemDetail =
                ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);

        problemDetail.setTitle("Validation Error");
        problemDetail.setProperty("timestamp", LocalDateTime.now());

        var errors = ex
                .getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .filter(Objects::nonNull).toList();

        problemDetail.setDetail("Request validation failed");
        problemDetail.setProperty("errors", errors);

        return problemDetail;
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ProblemDetail handle(ResourceAlreadyExistsException ex) {
        log.error("[GLOBAL_EXCEPTION_HANDLER] Workflow failed due to {}", ex.getClass(), ex);
        ProblemDetail problemDetail =
                ProblemDetail.forStatus(HttpStatus.UNPROCESSABLE_CONTENT);

        problemDetail.setTitle("Operation Failed");
        problemDetail.setProperty("timestamp", LocalDateTime.now());

        var cause = ex.getCause() != null ? ex.getCause() : ex;
        problemDetail.setDetail("Invalid Request");
        problemDetail.setProperty("errors", cause.getMessage());

        return problemDetail;
    }


    @ExceptionHandler(NoSuchElementException.class)
    public ProblemDetail handle(NoSuchElementException ex) {
        log.error("[GLOBAL_EXCEPTION_HANDLER] Workflow failed due to {}", ex.getClass(), ex);
        ProblemDetail problemDetail =
                ProblemDetail.forStatus(HttpStatus.NOT_FOUND);

        problemDetail.setTitle("Resource Not Found");
        problemDetail.setProperty("timestamp", LocalDateTime.now());

        var cause = ex.getCause() != null ? ex.getCause() : ex;
        problemDetail.setDetail("Invalid Resource Requested");
        problemDetail.setProperty("errors", cause.getMessage());

        return problemDetail;
    }

    @ExceptionHandler(ResourceDoesntExistsException.class)
    public ProblemDetail handle(ResourceDoesntExistsException ex) {
        log.error("[GLOBAL_EXCEPTION_HANDLER] Workflow failed due to {}", ex.getClass(), ex);
        ProblemDetail problemDetail =
                ProblemDetail.forStatus(HttpStatus.UNPROCESSABLE_CONTENT);

        problemDetail.setTitle("Operation Failed");
        problemDetail.setProperty("timestamp", LocalDateTime.now());

        var cause = ex.getCause() != null ? ex.getCause() : ex;
        problemDetail.setDetail("Invalid Request");
        problemDetail.setProperty("errors", cause.getMessage());

        return problemDetail;
    }


    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail handle(IllegalArgumentException ex) {
        log.error("[GLOBAL_EXCEPTION_HANDLER] Workflow failed due to {}", ex.getClass(), ex);
        ProblemDetail problemDetail =
                ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);

        problemDetail.setTitle("Operation Failed");
        problemDetail.setProperty("timestamp", LocalDateTime.now());

        var cause = ex.getCause() != null ? ex.getCause() : ex;
        problemDetail.setDetail("Inconsistent Request");
        problemDetail.setProperty("errors", cause.getMessage());

        return problemDetail;
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handle(Exception ex) {
        log.error("[GLOBAL_EXCEPTION_HANDLER] Workflow failed due to {}", ex.getClass(), ex);
        ProblemDetail problemDetail =
                ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);

        problemDetail.setTitle("Operation Failed");
        problemDetail.setProperty("timestamp", LocalDateTime.now());

        var cause = ex.getCause() != null ? ex.getCause() : ex;
        problemDetail.setDetail("Please try again later");
        problemDetail.setProperty("errors", cause.getMessage());

        return problemDetail;
    }

}
