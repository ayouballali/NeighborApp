package com.nieghborapp.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UserDto {
    @NotBlank(message = "you can't let name empty")
    private String name ;
    @NotBlank(message = "you can't let email empty")
    @Email(message = "not a valid email")
    private String email ;
    @NotBlank(message = "you can't let password empty")
    @Size(min = 8,message = "your password is too short")
    private String  password ;
}
