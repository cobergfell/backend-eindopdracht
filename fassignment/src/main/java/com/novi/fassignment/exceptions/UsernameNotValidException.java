package com.novi.fassignment.exceptions;

public class UsernameNotValidException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public UsernameNotValidException(String username) {
        super("Entered user name not valid " + username);
    }

}
