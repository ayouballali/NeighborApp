package com.nieghborapp.service;

import com.nieghborapp.domain.User;
import com.nieghborapp.dto.RegisterDto;
import com.nieghborapp.dto.UserDto;
import com.nieghborapp.exceptions.AlreadyExistsException;
import com.nieghborapp.exceptions.NotFoundException;
import com.nieghborapp.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.mail.MessagingException;
import java.util.Optional;

public interface IUserService  {

    void addUser(RegisterDto registerDto, String url) throws AlreadyExistsException, NotFoundException, MessagingException;
    boolean verifyCode(String code ) throws NotFoundException;
//    Boolean existByEmail(String email);


}
