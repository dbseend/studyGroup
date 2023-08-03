package com.hgu.histudyDB.Exceptions;

public class InvalidEmailException extends Exception {
    /**
     * Default Constructor
     */
    public InvalidEmailException() {
        super();
    }

    /**
     * String Constructor
     */
    public InvalidEmailException(String email) {
        super(email);
    }
}