package com.example.common.exception;

import com.example.common.utils.MessageError;
import org.springframework.http.HttpStatus;

public class ApplicationException extends Exception{
    private HttpStatus errorCode;

    public ApplicationException() {
        super();
    }

    public ApplicationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApplicationException(String message) {
        super(message);
    }

    public ApplicationException(String message, HttpStatus errorCode) {
        super(message);
        this.setErrorCode(errorCode);
    }

    public ApplicationException(MessageError messageError) {
        super(messageError.getMessage());
        this.setErrorCode(messageError.getHttpStatus());
    }

    public ApplicationException(String message, Throwable cause, HttpStatus errorCode) {
        super(message, cause);
        this.setErrorCode(errorCode);
    }

    public HttpStatus getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(HttpStatus errorCode) {
        this.errorCode = errorCode;
    }
}
