package com.nieghborapp.service;

import com.nieghborapp.domain.User;
import com.nieghborapp.dto.RegisterDto;
import com.nieghborapp.exceptions.AlreadyExistsException;
import com.nieghborapp.exceptions.NotFoundException;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface IAuthService {
    void addUser(RegisterDto registerDto, String url) throws AlreadyExistsException, NotFoundException, MessagingException;
    boolean verifyCode(String code ) throws NotFoundException;

    void refreshToken (String token , HttpServletResponse response) throws IOException;

}
