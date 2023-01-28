package com.nieghborapp.service;

import com.nieghborapp.domain.User;
import com.nieghborapp.dto.RegisterDto;
import com.nieghborapp.dto.UserDto;
import com.nieghborapp.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public interface IUserService  {

    void addUser(RegisterDto registerDto, String url) throws Exception;
    boolean verifyCode(String code ) throws Exception;
//    Boolean existByEmail(String email);


}
