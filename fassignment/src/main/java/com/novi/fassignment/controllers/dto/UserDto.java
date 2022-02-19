package com.novi.fassignment.controllers.dto;

import com.novi.fassignment.models.User;

public class UserDto {
    public String username;
    public String password;
    public String email;

    public static UserDto fromUser(User user) {
        var dto = new UserDto();
        dto.username = user.getUsername();
        dto.password = user.getPassword();
        dto.email = user.getEmail();
        return dto;
    }
}
