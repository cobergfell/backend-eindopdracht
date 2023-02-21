package com.novi.fassignment.controllers.dto;

import com.novi.fassignment.models.User;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class UserDto {
    @NotNull
    @NotEmpty
    public String username;
    @NotNull
    @NotEmpty
    public String password;
    @NotNull
    @NotEmpty
    public String email;

    public static UserDto fromUser(User user) {
        var dto = new UserDto();
        dto.username = user.getUsername();
        dto.password = user.getPassword();
        dto.email = user.getEmail();
        return dto;
    }
}
