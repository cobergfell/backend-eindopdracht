package com.novi.fassignment.services;

import com.novi.fassignment.models.Authority;
import com.novi.fassignment.models.User;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

public interface UserService {

    public abstract String createUser(User user);
    public abstract void updateUser(String username, User user);
    public abstract void deleteUser(String username);
    public abstract void deleteAllUsers();
    public abstract Collection<User> getUsers();
    public abstract Optional<User> getUser(String username);
    public abstract boolean userExists(String username);
    public abstract Set<Authority> getAuthorities(String username);

}