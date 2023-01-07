package com.novi.fassignment.services;

import com.novi.fassignment.models.Authority;
import com.novi.fassignment.models.User;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

public interface UserService {

    String createUser(User user);
    void updateUser(String username, User user);
    void deleteUser(String username);
    void deleteAllUsers();
    Collection<User> getUsers();
    Optional<User> getUser(String username);
    boolean userExists(String username);
    Set<Authority> getAuthorities(String username);

}