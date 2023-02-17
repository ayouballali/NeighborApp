package com.nieghborapp.service.impl;

import com.nieghborapp.domain.Role;
import com.nieghborapp.domain.User;
import com.nieghborapp.repository.IUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Optional;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    IUserRepository userRepository;
    @InjectMocks
    UserService underTest ;
    User user;

    @BeforeEach
    void before(){
         user = new User();
         user.setUsername("ayoub");
         user.setEnabled(true);
         user.setPassword(new BCryptPasswordEncoder().encode("0000"));
         user.setRoles(Arrays.asList(new Role(Role.USER)));

         userRepository.save(user);

    }
          //TODO TEST EACH FIELD 
    @Test
    void itShouldReturnUserDetailsWhenUserExists(){
        // given
        UserDetails expectedUserDetails = new org.springframework.security.core.userdetails.User(user.getUsername(),
                user.getPassword(),user.isEnabled() , user.isCredentialsNonExpired(),
                user.isCredentialsNonExpired(),user.isAccountNonLocked(),new ArrayList<>(Arrays.asList(new SimpleGrantedAuthority(Role.ADMIN))));
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.ofNullable(user));
        //when
        assertThatNoException().isThrownBy(() -> {
            UserDetails userDetails =  underTest.loadUserByUsername(user.getUsername())   ;

            System.out.println(expectedUserDetails.equals(userDetails));

            assertThat(expectedUserDetails).isEqualTo(userDetails);

        });
        //then
    }

    @Test
    void itShouldThrowWhenUsernameNotExists() {
        // given


        //when
        //then


            assertThatThrownBy(()->{
                underTest.loadUserByUsername("ayoub01");


            })
                    .isInstanceOf(UsernameNotFoundException.class)
                    .hasMessage("user not found ");

    }

    @Test  @Disabled
    void addUser() {
    }

    @Test           @Disabled
    void sendEmailVerification() {
    }

    @Test                    @Disabled
    void verifyCode() {
    }

    @Test                             @Disabled
    void refreshToken() {
    }
}