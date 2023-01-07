package com.novi.fassignment.exceptions;

public class EmailAlreadyExistException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public EmailAlreadyExistException(String email) {
        super("Email already exists " + email);
    }

}
