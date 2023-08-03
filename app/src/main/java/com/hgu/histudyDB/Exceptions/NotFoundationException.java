package com.hgu.histudyDB.Exceptions;

public class NotFoundationException extends Exception {
    /**
     * Default Constructor
     */
    public NotFoundationException() {
        super();
    }

    /**
     * String Constructor
     */
    public NotFoundationException(String name) {
        super(name); // super는 문자열만 가능?
    }

}
