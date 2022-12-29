package com.nieghborapp.service.impl;

import com.nieghborapp.domain.User;
import com.nieghborapp.dto.UserDto;
import com.nieghborapp.repository.IUserRepository;
import com.nieghborapp.service.IUserService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class UserService  implements IUserService {
    private final IUserRepository userRepository ;


    @Override
    public void  save(User user) {
        userRepository.save(user);
    }

    @Override
    public Boolean existByEmail(String email) {
        return userRepository.existsByEmail(email);
    }


}
