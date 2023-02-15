package com.nieghborapp.filters;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
//import com.nieghborapp.domain.User;
import com.nieghborapp.dto.ErrorResponseDto;
import com.nieghborapp.exceptions.NotFoundException;
import com.nieghborapp.exceptions.NotValidRunTimeException;
import com.nieghborapp.repository.IUserRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;
@RequiredArgsConstructor @Slf4j   
public class CustomeAuthentificationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final IUserRepository userRepository;



    @Override
    public void  unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws ServletException, IOException {
             log.error("it's a failed authentication "+failed.getMessage());
            super.unsuccessfulAuthentication(request, response, failed);

    }

    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)throws AuthenticationException{
        log.info("attempt to authenticate ");

        String username = request.getParameter("username");
        String paswword = request.getParameter("password");
        
            if( username == null || username.length()<4) throw new RuntimeException("username not valid ");
            if( paswword == null || paswword.length()<4) throw new RuntimeException("password  not valid ");




        //TODO VALIDATE THE USERNAME AND THE PASSEORD BEFORE

        log.info(username+" "+paswword);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username,paswword);
        // create authentication
        Authentication  auth = authenticationManager.authenticate(authentication);

        return  auth ;

    }



//TODO SEND THE USER WHEN HE LOGED IN           DONE

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException {
        log.info("it's a successful authentication ");
        User user = (User) authResult.getPrincipal();
        com.nieghborapp.domain.User fullUser = null;
        try {
            fullUser =     userRepository.findByUsername(user.getUsername()).orElseThrow(()->new NotFoundException("somthing went wrong "));
        } catch (NotFoundException e) {
            throw new RuntimeException(e);
        }
        
        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());

        String accessToken = JWT.create()
                .withSubject(user.getUsername())
                .withIssuer(request.getRequestURI())
                .withExpiresAt(new Date(System.currentTimeMillis()+1000*60*10*100))
                .withClaim("roles",user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);

        String refreshToken = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis()+ 1000L *60*500*1000))
                .withIssuer(request.getRequestURI())
                .sign(algorithm);

        Map<String, Serializable> map = Map.of("access_token",accessToken,"refresh_token",refreshToken,"user",fullUser);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        new ObjectMapper().writeValue(response.getOutputStream(),map);
    }
}
