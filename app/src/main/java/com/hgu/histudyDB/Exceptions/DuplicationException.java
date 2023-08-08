package com.hgu.histudyDB.Exceptions;

public class DuplicationException extends Exception {
    /**
     * Default Constructor
     */
    public DuplicationException() {
        super();
    }

    /**
     * String Constructor
     */
    public DuplicationException(String email) {
        super(email);
    }
}