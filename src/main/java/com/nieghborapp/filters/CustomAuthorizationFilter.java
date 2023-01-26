package com.nieghborapp.filters;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
@Slf4j
public class CustomAuthorizationFilter extends OncePerRequestFilter {

    @Override

    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println("i am filter ");
// if the
        if(request.getServletPath().equals("/api/login") || request.getServletPath().equals("/api/token/refresh") ){
            filterChain.doFilter(request,response);
            return;
        }
        System.out.println("i am filter 1 ");

        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {

//            filterChain.doFilter(request,response);
            response.sendError(HttpStatus.FORBIDDEN.value());
            log.error("authorization header not valid");
            return;
        }

        System.out.println("i am filter 2");

        // get the token
        String token = authorizationHeader.substring("Bearer ".length());

        try {
            // create jwt verifier
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256("secret")).build();
            // verify if token is valid ,isnotExpired and decode it, if it's not valid it will throw an exception JWTVerificationException
            DecodedJWT decodedJWT = jwtVerifier.verify(token);
            // it's in the subject because we put it there
            String username = decodedJWT.getSubject();

            List<String> roles = decodedJWT.getClaim("roles").asList(String.class);

            Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
            roles.stream().forEach(role->{
                authorities.add(new SimpleGrantedAuthority(role));
            });

            System.out.println(authorities.toArray()[0]);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, null, authorities);


            SecurityContextHolder.getContext().setAuthentication(authenticationToken);


        }catch (Exception exception){
            log.error("jwt not valid "+exception.getMessage());
            response.sendError(HttpStatus.FORBIDDEN.value(),"token not valid");
            return;
        }
        log.info("token was good ");
        filterChain.doFilter(request, response);

    }
}