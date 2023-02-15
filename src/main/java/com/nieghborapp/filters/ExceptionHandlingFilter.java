package com.nieghborapp.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nieghborapp.web.common.AdviceHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@RequiredArgsConstructor @Component @Slf4j
public class ExceptionHandlingFilter extends OncePerRequestFilter {
    private final   AdviceHandler adviceHandler;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        log.warn("this is custom exception handler");
        try{
            filterChain.doFilter(request,response);
        }catch (RuntimeException e){
            log.error(" there is a problem "+ e.getMessage());
            ResponseEntity<?> res =  adviceHandler.handleRuntimeException( e);
            response.setStatus(res.getStatusCodeValue());
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            
            response.getWriter().write(new ObjectMapper().writeValueAsString(res));

        }
    }
}
