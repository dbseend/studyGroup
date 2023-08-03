package com.hgu.histudyDB.Exceptions;

public class InvalidStudentIdException extends Exception {
    /**
     * Default Constructor
     */
    public InvalidStudentIdException() {
        super();
    }

    /**
     * String Constructor
     */
    public InvalidStudentIdException(String studentId) {
        super(studentId); // super는 문자열만 가능?
    }

}
