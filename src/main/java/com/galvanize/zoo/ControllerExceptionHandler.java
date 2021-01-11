package com.galvanize.zoo;

import com.galvanize.zoo.animal.IncompatibleTypeException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(IncompatibleTypeException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public void handleIncompatibleTypeException() {
    }
}
