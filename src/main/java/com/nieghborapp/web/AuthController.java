package com.nieghborapp.web;



import com.nieghborapp.domain.User;
import com.nieghborapp.dto.RegisterDto;
import com.nieghborapp.exceptions.AlreadyExistsException;
import com.nieghborapp.exceptions.NotFoundException;
import com.nieghborapp.repository.IUserRepository;
import com.nieghborapp.service.IAuthService;
import com.nieghborapp.service.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

import java.util.List;
import java.util.Map;
@RestController @Slf4j
@RequestMapping("api/auth") @RequiredArgsConstructor
public class AuthController {

    private  final IAuthService userService;
    private final IUserRepository userRepository;

    @PostMapping("/register")
    ResponseEntity<?> register(@Valid @RequestBody RegisterDto registerDto, HttpServletRequest httpServletRequest) throws AlreadyExistsException, MessagingException, NotFoundException {

        userService.addUser(registerDto,getUrl(httpServletRequest));

        return new ResponseEntity<>(Map.of("message","the user has been created successfully, you should now activate it "),null,HttpStatus.CREATED) ;
    }

    // this is a helper to get the base root
    String getUrl(HttpServletRequest httpServletRequest){
        String requestUrl = httpServletRequest.getRequestURL().toString();
        return requestUrl.replace(httpServletRequest.getContextPath(),"");
    }

    @GetMapping("/register/verify")
    ResponseEntity<?> verifyEmailCode (@Param("code") String code ) throws NotFoundException {
        log.info("verifying email en cours ");

            if(userService.verifyCode(code )){
                // return success
                log.info("verify email has been succeeded ");
                return new  ResponseEntity<>(HttpStatus.ACCEPTED);
            }

        return new  ResponseEntity<>(Map.of("message","the account is already enabled  "),null,HttpStatus.NOT_ACCEPTABLE);
    }

    @GetMapping("/all")
    List<User> getAll(){
        log.info("geting users ");
        return userRepository.findAll();
    }



    @GetMapping("/token/refresh")
    public void tokenRefresh(HttpServletRequest request, HttpServletResponse response) throws IOException,Exception {
        String tokenComplete = request.getHeader(HttpHeaders.AUTHORIZATION);

        userService.refreshToken(tokenComplete,response);
    }



}
