//package adapted from https://github.com/PeterAnema/springboot-15-security-with-jwt.git
//user is replaced by CustomUser to make it different from org.springframework.security.core.userdetails.User

package com.novi.fassignment.models;

import java.io.Serializable;

public class AuthorityKey implements Serializable {
    private String username;
    private String authority;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }
}