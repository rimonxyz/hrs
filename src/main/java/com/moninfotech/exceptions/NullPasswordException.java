package com.moninfotech.exceptions;

public class NullPasswordException extends Exception{
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
