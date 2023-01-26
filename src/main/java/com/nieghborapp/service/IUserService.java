package com.nieghborapp.service;

import com.nieghborapp.domain.User;
import com.nieghborapp.dto.UserDto;
import com.nieghborapp.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public interface IUserService  {
    void save(User user) ;
//    Boolean existByEmail(String email);

}
