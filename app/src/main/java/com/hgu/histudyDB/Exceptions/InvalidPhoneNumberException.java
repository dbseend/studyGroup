package com.hgu.histudyDB.Exceptions;

public class InvalidPhoneNumberException extends Exception {
    /**
     * Default Constructor
     */
    public InvalidPhoneNumberException() {
        super();
    }

    /**
     * String Constructor
     */
    public InvalidPhoneNumberException(String email) {
        super(email);
    }
}