package com.moninfotech.exceptions;

public class NotFoundException extends Throwable {

    private String message;
    private Throwable stackTrace;

    public NotFoundException() {
        super();
    }

    public NotFoundException(String message) {
        super(message);
        this.message = message;
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
        this.message = message;
        this.stackTrace = cause;
    }

    @Override
    public String toString() {
        return "NotFoundException{" +
                "message='" + message + '\'' +
                ", stackTrace=" + stackTrace +
                "} " + super.toString();
    }
}
