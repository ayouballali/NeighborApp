package com.nieghborapp.service.impl;

import com.nieghborapp.domain.Role;
import com.nieghborapp.domain.User;
import com.nieghborapp.dto.EmailDto;
import com.nieghborapp.dto.RegisterDto;
import com.nieghborapp.exceptions.AlreadyExistsException;
import com.nieghborapp.exceptions.NotFoundException;
import com.nieghborapp.repository.IUserRepository;
import com.nieghborapp.repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.mail.MessagingException;
import java.util.Arrays;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @Mock
    IUserRepository userRepository;
    @Mock
    RoleRepository roleRepository;
    @Mock
    EmailService emailService;
    @InjectMocks
    AuthService underTest ;
    User user;

    @BeforeEach
    void before(){
         user = new User();
         user.setUsername("ayoub");
         user.setEnabled(true);
         user.setName("ayoub");
         user.setPassword(new BCryptPasswordEncoder().encode("0000"));
         user.setRoles(Arrays.asList(new Role(Role.USER)));



    }
       
    @Test
    void itShouldReturnUserDetailsWhenUserExists(){
        // given
       
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.ofNullable(user));
        //when
       
        //then
        assertThatNoException().isThrownBy(() -> {
            UserDetails userDetails =  underTest.loadUserByUsername(user.getUsername())   ;
            assertThat(userDetails.getAuthorities().stream().map((e)->e.getAuthority())).isEqualTo( Arrays.asList(Role.USER));

        });
    }

    @Test
    void itShouldThrowWhenUsernameNotExists() {
        // given
        String username = "ayoub01" ;

        //when
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty())   ;
        //then


        assertThatThrownBy(()->{
                underTest.loadUserByUsername(username);


            })
                    .isInstanceOf(UsernameNotFoundException.class)
                    .hasMessage("user not found ");

    }

    @Test
    void itShouldTrowWhenAddingUserAndUsernameIsExists() throws AlreadyExistsException, MessagingException, NotFoundException {
        // given
        RegisterDto registerDto = new RegisterDto();
        registerDto.setUsername(user.getUsername());
        registerDto.setName(user.getName());
        registerDto.setPassword(user.getPassword());

        // when
        when(userRepository.existsByUsername(registerDto.getUsername())).thenReturn(true);
        // then
        assertThatThrownBy(()->{
            underTest.addUser(registerDto,"url");
        }).isInstanceOf(AlreadyExistsException.class)
                .hasMessage("user's username already exists ") ;
    }

    @Test
    void itShouldTrowWhenAddingUserAndEmailIsExists() throws AlreadyExistsException, MessagingException, NotFoundException {
        // given
        RegisterDto registerDto = new RegisterDto();
        registerDto.setUsername(user.getUsername());
        registerDto.setName(user.getName());
        registerDto.setPassword(user.getPassword());

        // when
        when(userRepository.existsByEmail(registerDto.getEmail())).thenReturn(false);

        when(userRepository.existsByEmail(registerDto.getEmail())).thenReturn(true);
        // then
        assertThatThrownBy(()->{
            underTest.addUser(registerDto,"url");
        }).isInstanceOf(AlreadyExistsException.class)
                .hasMessage("user's email already exists ") ;
    }


    @Test
    void itShouldAddUser() throws NotFoundException, AlreadyExistsException, MessagingException {
        // given
        RegisterDto registerDto = new RegisterDto();
        registerDto.setUsername(user.getUsername());
        registerDto.setName(user.getName());
        registerDto.setPassword("0000");

        Role role = new Role(Role.USER);

        // creating the user
        User expectedUser = new User();
//        expectedUser.setPassword(new BCryptPasswordEncoder().encode("0000"));
        expectedUser.setUsername(registerDto.getUsername());
        expectedUser.setEmail(registerDto.getEmail());
        expectedUser.setName(registerDto.getName());
        expectedUser.setRoles(Arrays.asList(new Role(Role.USER)) );
        expectedUser.setEnabled(false);

        // when


        when(userRepository.existsByEmail(registerDto.getEmail())).thenReturn(false);
        when(userRepository.existsByEmail(registerDto.getEmail())).thenReturn(false);
        when(roleRepository.findByName(anyString())).thenReturn(Optional.ofNullable(role))  ;




        // then
        underTest.addUser(registerDto,anyString());
        ArgumentCaptor<User> argumentCaptor  = ArgumentCaptor.forClass(User.class)  ;
        verify(userRepository).save(argumentCaptor.capture());
        User capturedUser = argumentCaptor.getValue();
        expectedUser.setVerificationCode(capturedUser.getVerificationCode());
        expectedUser.setPassword(capturedUser.getPassword());

        assertThat(capturedUser).isEqualTo(expectedUser);
              //TODO CAPTURE THE ARGUMENTS SENT TO EMAIL SERVICE AND CHECHK THEM 
        verify(emailService).sendEmail(new EmailDto(anyString(),anyString(),anyString(),anyString()));

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