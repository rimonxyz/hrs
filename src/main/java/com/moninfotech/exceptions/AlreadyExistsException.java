package com.moninfotech.exceptions;

public class AlreadyExistsException extends IllegalStateException{
    public AlreadyExistsException(String s) {
        super(s);
    }
}
