package ru.ostritsov.projectTSM.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationError(final ValidationException e) {
        return new ErrorResponse(String.format("validation error: %s", e.getMessage()));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleAppError(final AppError e) {
        return new ErrorResponse(String.format("app error: %s", e.getMessage()));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleAppError(final NotFoundException e) {
        return new ErrorResponse(String.format("not found error: %s", e.getMessage()));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleAppError(final ConflictException e) {
        return new ErrorResponse(String.format("conflict error: %s", e.getMessage()));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleAppError(final ForbiddenException e) {
        return new ErrorResponse(String.format("forbidden error: %s", e.getMessage()));
    }
}
