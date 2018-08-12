package com.moninfotech.exceptions.forbidden;

public class ForbiddenException extends Throwable {

    private String message;
    private String redirectTo;
    private Throwable stackTrace;

    public ForbiddenException() {
        super();
    }

    public ForbiddenException(String redirectTo, String message) {
        super(message);
        this.message = message;
        this.redirectTo = redirectTo;
    }

    public ForbiddenException(String message, String redirectTo, Throwable cause) {
        super(message, cause);
        this.message = message;
        this.redirectTo = redirectTo;
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

    public void setStackTrace(Throwable stackTrace) {
        this.stackTrace = stackTrace;
    }

    @Override
    public String toString() {
        return "InvalidException{" +
                "message='" + message + '\'' +
                ", redirectTo='" + redirectTo + '\'' +
                ", stackTrace=" + stackTrace +
                "} " + super.toString();
    }
}
