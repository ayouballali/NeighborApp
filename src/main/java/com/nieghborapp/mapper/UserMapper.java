package com.nieghborapp.mapper;

import com.nieghborapp.domain.Role;
import com.nieghborapp.domain.User;
import com.nieghborapp.dto.UserDto;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;

public class UserMapper {
    public User toEntity(UserDto userDto){
        User user = new User();
        user.setName(userDto.getName());
        user.setUsername(userDto.getUsername());
        user.setRoles(Arrays.asList(new Role(Role.USER)) );
        user.setPassword(new BCryptPasswordEncoder().encode(userDto.getPassword()));
        return user;
    }
}
