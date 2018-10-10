package com.moninfotech.exceptions.notfound;

import com.moninfotech.exceptions.NotFoundException;

public class SessionBookingNotFoundException extends NotFoundException {
    public SessionBookingNotFoundException() {
    }

    public SessionBookingNotFoundException(String message) {
        super(message);
    }

    public SessionBookingNotFoundException(String redirectTo,String message) {
        super(message, redirectTo);
    }

}
