package com.hgu.histudyDB.Exceptions;

public class InvalidIdException extends Exception {
    /**
     * Default Constructor
     */
    public InvalidIdException() {
        super();
    }

    /**
     * String Constructor
     */
    public InvalidIdException(String email) {
        super(email);
    }
}