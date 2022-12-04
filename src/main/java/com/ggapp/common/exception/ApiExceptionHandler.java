package com.ggapp.common.exception;

import com.ggapp.common.dto.response.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler({ApplicationException.class})
    public ResponseEntity<BaseResponse> handleApplicationException(ApplicationException exception) {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setStatus(exception.getErrorCode().value());
        baseResponse.setStatusname(exception.getErrorCode().name());
        baseResponse.setMessage(exception.getMessage());
        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<BaseResponse> handleAllException(Exception exception) {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setStatus(HttpStatus.FORBIDDEN.value());
        baseResponse.setStatusname(HttpStatus.FORBIDDEN.name());
        baseResponse.setMessage(exception.getMessage());
        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<BaseResponse> handleValidationExceptions(MethodArgumentNotValidException exception) {
        BaseResponse baseResponse = new BaseResponse();
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        baseResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        baseResponse.setStatusname(HttpStatus.BAD_REQUEST.name());
        baseResponse.setMessage("Validation failed");
        baseResponse.setPayload(errors);
        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }
}
