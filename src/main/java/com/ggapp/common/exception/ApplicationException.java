package com.ggapp.common.exception;

import com.ggapp.common.enums.MessageResponse;
import org.springframework.http.HttpStatus;

public class ApplicationException extends Exception{
    private HttpStatus errorCode;
    private StackTraceElement stackTraceElement;

    public ApplicationException() {
        super();
    }

    public ApplicationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApplicationException(String message, Throwable cause, StackTraceElement stackTraceElement, HttpStatus errorCode) {
        super(message, cause);
        this.setErrorCode(errorCode);
        this.setStackTraceElement(stackTraceElement);
    }

    public ApplicationException(String message) {
        super(message);
    }

    public ApplicationException(String message, HttpStatus errorCode) {
        super(message);
        this.setErrorCode(errorCode);
    }


    public ApplicationException(MessageResponse messageResponse) {
        super(messageResponse.getMessage());
        this.setErrorCode(messageResponse.getHttpStatus());
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

    public StackTraceElement getStackTraceElement() {
        return stackTraceElement;
    }

    public void setStackTraceElement(StackTraceElement stackTraceElement) {
        this.stackTraceElement = stackTraceElement;
    }
}
