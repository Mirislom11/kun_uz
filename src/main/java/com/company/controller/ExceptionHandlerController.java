package com.company.controller;

import com.company.exception.BadRequestException;
import com.company.exception.ForbiddenException;
import com.company.exception.ItemNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerController {
    @ExceptionHandler({ItemNotFoundException.class})
    public ResponseEntity<?> handleItemNotFoundException(RuntimeException ex) {
        ex.printStackTrace();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    @ExceptionHandler({ForbiddenException.class})
    public ResponseEntity<?> handleForbiddenException (RuntimeException e){
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
    @ExceptionHandler({BadRequestException.class})
    public ResponseEntity<?> handleBadRequestException(RuntimeException e){
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}
