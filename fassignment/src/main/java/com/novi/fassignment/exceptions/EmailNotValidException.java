package com.novi.fassignment.exceptions;

public class EmailNotValidException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public EmailNotValidException(String email) {
        super("Entered email not valid " + email);
    }

}
