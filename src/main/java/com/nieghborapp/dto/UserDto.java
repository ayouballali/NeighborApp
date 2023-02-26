package com.nieghborapp.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
@Data
public class UserDto {
    @NotBlank(message = "you can't let name empty")
    private String name ;
    @NotBlank(message = "you can't let email empty")

    private String username  ;

    @Email(message = "not a valid email")
    private String email ;

    private String password ;


}
