package com.nieghborapp.web;


import com.nieghborapp.domain.User;
import com.nieghborapp.dto.SignInDto;
import com.nieghborapp.dto.UserDto;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth")
public class AuthController {



    @PostMapping("/signUp")
    ResponseEntity<User> register(@RequestBody UserDto userDto){
        return null;
    }

    @PostMapping("/signIn")
    ResponseEntity<?> login(@RequestBody SignInDto signInDto ){
        return new ResponseEntity<>(HttpStatus.ACCEPTED) ;
    }

}
