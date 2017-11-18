package com.moninfotech.exceptions.nullexceptions;

public class NullPasswordException extends NullObjectException{
    private String message;

    public NullPasswordException(){}

    public NullPasswordException(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "NullPasswordException{" +
                "message='" + message + '\'' +
                "} " + super.toString();
    }
}
