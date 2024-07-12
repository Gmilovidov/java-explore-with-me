package ru.practicum.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.Constants;
import ru.practicum.exceptions.InvalidRequestException;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String errors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        log.error(e.getMessage());
        log.error(Arrays.toString(e.getStackTrace()));

        return new ApiError(HttpStatus.BAD_REQUEST,
                "Incorrectly made request.",
                errors,
                LocalDateTime.now().format(Constants.FORMATTER));
    }

    @ExceptionHandler(InvalidRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleInvalidRequestException(InvalidRequestException e) {
        log.error(e.getMessage());
        log.error(Arrays.toString(e.getStackTrace()));
        return new ApiError(HttpStatus.BAD_REQUEST,
                "Incorrectly made request",
                e.getMessage(),
                LocalDateTime.now().format(Constants.FORMATTER));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleValidationException(DataIntegrityViolationException e) {
        log.error(e.getMessage());
        log.error(Arrays.toString(e.getStackTrace()));
        log.error("CONFLICT 409 The operation cannot be performed {}", e.getMessage());
        return new ApiError(HttpStatus.CONFLICT, "Integrity constraint has been violated.",
                e.getMessage(), LocalDateTime.now().format(Constants.FORMATTER));
    }

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleAnyException(Throwable e) {
        log.error(e.getMessage());
        log.error(Arrays.toString(e.getStackTrace()));
        return new ApiError(HttpStatus.INTERNAL_SERVER_ERROR,
                "Internal Server Error",
                e.getMessage(),
                LocalDateTime.now().format(Constants.FORMATTER));
    }
}
