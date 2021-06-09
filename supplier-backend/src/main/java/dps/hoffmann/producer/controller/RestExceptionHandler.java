package dps.hoffmann.producer.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import dps.hoffmann.producer.model.ApiError;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler {

    // todo check if error codes are correct

    @ExceptionHandler(DuplicateKeyException.class)
    protected ResponseEntity<Object> handleDublicateKey(DuplicateKeyException ex) {
        ApiError error = new ApiError(HttpStatus.BAD_REQUEST, ex);
        return new ResponseEntity<>(error, error.getStatus());
    }

    @ExceptionHandler(JsonProcessingException.class)
    protected ResponseEntity<Object> handleHttpMessageNotReadable(JsonProcessingException ex) {
        ApiError error = new ApiError(HttpStatus.BAD_REQUEST, ex);
        return new ResponseEntity<>(error, error.getStatus());
    }

    @ExceptionHandler(InterruptedException.class)
    protected ResponseEntity<Object> handleSleepInterrupt(InterruptedException ex) {
        ApiError error = new ApiError(HttpStatus.BAD_REQUEST, ex);
        return new ResponseEntity<>(error, error.getStatus());
    }

}

