package com.nieghborapp.service.impl;

import com.nieghborapp.domain.User;
import com.nieghborapp.dto.UserDto;
import com.nieghborapp.repository.IUserRepository;
import com.nieghborapp.service.IUserService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class UserService  implements IUserService, UserDetailsService {
    private final IUserRepository userRepository ;


    @Override
    public void  save(User user) {
        userRepository.save(user);
    }

//    @Override
//    public Boolean existByEmail(String email) {
//        return userRepository.existsByEmail(email);
//    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(()->new UsernameNotFoundException("user not found "));
        Collection<SimpleGrantedAuthority> authorities =  user.getRoles().stream().map(role ->new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
        return new org.springframework.security.core.userdetails.User(username, user.getPassword(), authorities);
    }
}
