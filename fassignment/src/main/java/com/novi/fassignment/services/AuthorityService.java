package com.novi.fassignment.services;

import com.novi.fassignment.models.Authority;
import com.novi.fassignment.models.User;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;


public interface AuthorityService {
    Set<Authority> getAuthorities(String username);
    public  void addAuthority(String username, String authority);
    public  void removeAuthority(String username, String authority);
    public  void removeAllAuthorities(String username);
}