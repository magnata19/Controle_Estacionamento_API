package com.dpacifico.demo_park_api.web.exception;

import com.dpacifico.demo_park_api.exception.CpfUniqueViolation;
import com.dpacifico.demo_park_api.exception.EntityNotFoundException;
import com.dpacifico.demo_park_api.exception.PasswordInvalidException;
import com.dpacifico.demo_park_api.exception.UsernameUniqueViolation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ApiExceptionHandler {

    //exception para retornada quando algum campo está inválido
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessage> methodArgumentNotValidException (
            MethodArgumentNotValidException ex,
            HttpServletRequest request,
            BindingResult result
    ) {
        log.error("API Error - ", ex);
        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorMessage(request,
                        HttpStatus.UNPROCESSABLE_ENTITY,
                        "Campo(s) inválido(s)!",
                        result));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorMessage> accessDeniedException(AccessDeniedException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorMessage(request, HttpStatus.FORBIDDEN, ex.getMessage()));
    }

    //exception retornada ao criar um usuario ja criado
    @ExceptionHandler({UsernameUniqueViolation.class, CpfUniqueViolation.class})
    public ResponseEntity<ErrorMessage> dataIntegrityViolationException(UsernameUniqueViolation ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorMessage(
                   request,
                   HttpStatus.CONFLICT,
                        ex.getMessage()
                ));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorMessage> entityNotFound(RuntimeException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorMessage(
                        request,
                        HttpStatus.NOT_FOUND,
                        ex.getMessage()
                ));
    }

    @ExceptionHandler(PasswordInvalidException.class)
    public ResponseEntity<ErrorMessage> passwordInvalidException(RuntimeException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorMessage(
                        request,
                        HttpStatus.BAD_REQUEST,
                        ex.getMessage()
                ));
    }
}
