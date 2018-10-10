package com.moninfotech.exceptions;

public class NotFoundException extends Throwable {

    private String message;
    private Throwable stackTrace;
    private String redirectTo;

    public NotFoundException() {
        super();
    }

    public NotFoundException(String message) {
        super(message);
        this.message = message;
    }


    public NotFoundException(String message,String redirectTo) {
        super(message);
        this.message = message;
        this.redirectTo = redirectTo;
    }



    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
        this.message = message;
        this.stackTrace = cause;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public String getRedirectTo() {
        return redirectTo;
    }

    public void setRedirectTo(String redirectTo) {
        this.redirectTo = redirectTo;
    }

    @Override
    public String toString() {
        return "NotFoundException{" +
                "message='" + message + '\'' +
                ", stackTrace=" + stackTrace +
                "} " + super.toString();
    }
}
