package com.novi.fassignment.exceptions;

public class UserAlreadyExistException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public UserAlreadyExistException(String username) {
        super("Username already exists " + username);
    }

}
