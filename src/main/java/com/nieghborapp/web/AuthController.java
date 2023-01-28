package com.nieghborapp.web;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nieghborapp.domain.Role;
import com.nieghborapp.domain.User;
import com.nieghborapp.dto.RegisterDto;
import com.nieghborapp.repository.IUserRepository;
import com.nieghborapp.service.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController @Slf4j
@RequestMapping("api/auth") @RequiredArgsConstructor
public class AuthController {

    private  final IUserService userService;
    private final IUserRepository userRepository;

    @PostMapping("/register")
    ResponseEntity<String> register(@Valid @RequestBody RegisterDto registerDto, HttpServletRequest httpServletRequest) throws Exception {
      try {
          userService.addUser(registerDto,getUrl(httpServletRequest));
      }catch (Exception exception){
          log.error(exception.getMessage());
          return new ResponseEntity<>(HttpStatus.CONFLICT) ;

      }


        return new ResponseEntity<>(HttpStatus.CREATED) ;
    }

    // this is a helper to get the base root

    String getUrl(HttpServletRequest httpServletRequest){
        String requestUrl = httpServletRequest.getRequestURL().toString();
        return requestUrl.replace(httpServletRequest.getContextPath(),"");
    }

    @GetMapping("/register/verify")
    ResponseEntity<String> verifyEmailCode (@Param("code") String code )  {
        log.info("verifying email en cours ");
        try{
            if(userService.verifyCode(code )){
                // return success

                log.info("verify email has been succeeded ");
                return new  ResponseEntity<>(HttpStatus.ACCEPTED);
            }
        }catch (Exception e){
            log.error(e.getMessage());


        }
        return new  ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/all")
    List<User> getAll(){
        log.info("geting users ");
        return userRepository.findAll();
    }

    @GetMapping("/token/refresh")
    public void tokenRefresh(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String tokenComplete = request.getHeader(HttpHeaders.AUTHORIZATION);
        log.info("refresh token en cours ");
        if(tokenComplete != null || tokenComplete.startsWith("Baerer ")){
            String refersh_token = tokenComplete.substring("Baerer ".length());
            try {
                log.info("token is starting with ");

                Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
                JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256("secret")).build();

                DecodedJWT decodedJWT = jwtVerifier.verify(refersh_token);

                String username = decodedJWT.getSubject();

                User user = userRepository.findByUsername(username).orElseThrow(()->new UsernameNotFoundException("username not found"));

                String accessToken = JWT.create()
                        .withSubject(user.getUsername())
                        .withClaim("roles",user.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
                        .withExpiresAt(new Date(System.currentTimeMillis()+1000*60*5000))
                        .sign(algorithm);


                Map<String,String> map = Map.of("access_token",accessToken,"refresh_token",refersh_token);

                response.setContentType(MediaType.APPLICATION_JSON_VALUE);

                new ObjectMapper().writeValue(response.getOutputStream(),map);
            }catch (Exception exception){
                log.error("probkem in refresh token "+exception.getMessage());
//             response.;
                response.setHeader("error",exception.getMessage());
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                Map <String , String> errors = Map.of("error",exception.getMessage());

                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), errors);
            }

        }else {
            throw new RuntimeException("refresh tokem is missing ");
        }
    }



}
