package com.novi.fassignment.exceptions;

public class PasswordNotValidException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public PasswordNotValidException(String password) {
        super("Entered password not valid " + password);
    }

}
