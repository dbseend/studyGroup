package com.hgu.histudyDB.Exceptions;

public class ExceedException extends Exception {
    /**
     * Default Constructor
     */
    public ExceedException() {
        super();
    }

    /**
     * String Constructor
     */
    public ExceedException(String email) {
        super(email);
    }
}