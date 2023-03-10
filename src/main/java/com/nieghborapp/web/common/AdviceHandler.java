package com.nieghborapp.web.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nieghborapp.dto.ErrorResponseDto;
import com.nieghborapp.exceptions.AlreadyExistsException;
import com.nieghborapp.exceptions.NotFoundException;
import com.nieghborapp.exceptions.NotValidRunTimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestControllerAdvice
@Slf4j
public class AdviceHandler  {



    @ExceptionHandler(AlreadyExistsException.class)
    ResponseEntity<ErrorResponseDto> handleAlreadyExistsException(AlreadyExistsException ex, WebRequest req){

        log.error("handleAlreadyExistsException "+ex.getMessage());
        return new ResponseEntity<>(new ErrorResponseDto(HttpStatus.BAD_REQUEST.value(),ex.getMessage()),null, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler (MessagingException.class)
    ResponseEntity<ErrorResponseDto> handleMessagingException(MessagingException ex,WebRequest req){
        log.error("handleMessagingException   "+ex.getMessage());

        return new ResponseEntity<>(new ErrorResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(),"error in sending you email "),null ,HttpStatus.EXPECTATION_FAILED);
    }

    @ExceptionHandler(NotFoundException.class)
    ResponseEntity<ErrorResponseDto> handleNotFoundException(NotFoundException ex,WebRequest req){
        log.error("handleNotFoundException  "+ex.getMessage());

        return new ResponseEntity<>(new ErrorResponseDto(HttpStatus.NOT_FOUND.value(), ex.getMessage()),null,HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(IOException.class)
    ResponseEntity<ErrorResponseDto> handleNotFoundException(IOException ex,WebRequest req){
        log.error(ex.getMessage());

        return new ResponseEntity<>(new ErrorResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), "problem with refreshing the token "),null,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<ErrorResponseDto> handleException(Exception ex,WebRequest req){
        log.error("handleException"+ex.getMessage());

        return new ResponseEntity<>(new ErrorResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), " internal problem  "),null,HttpStatus.INTERNAL_SERVER_ERROR);
    }


    // handle validating fields
    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<Map<String , String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, WebRequest req){


        Map<String , String > errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error->{
            log.error(error.getDefaultMessage());
            String nameField = ((FieldError)error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(nameField,errorMessage);
        });



        return new ResponseEntity<>(errors,new HttpHeaders(),HttpStatus.BAD_REQUEST );
    }

//    @Override
//    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
//        log.error("this is commence from Advice "+authException.getMessage());
//        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
//        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//
//        Map<String ,String > errors = Map.of("error",authException.getMessage());
//
//        new ObjectMapper().writeValue(response.getOutputStream(),errors);
//    }

    @Primary
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRuntimeException(RuntimeException exception){
        log.error("handleRuntimeException   "+exception.getMessage());
        return new ResponseEntity<>( exception.getMessage(),null,HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(AccessDeniedException.class)
    public void handleAccessDeniedException (HttpServletRequest req,HttpServletResponse res , AccessDeniedException ex) throws IOException {
        log.error(ex.getMessage());

        res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        res.setContentType(MediaType.APPLICATION_JSON_VALUE);

        Map<String ,String > errors = Map.of("error",ex.getMessage());

        new ObjectMapper().writeValue(res.getOutputStream(),errors);
    }


    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponseDto> handleAuthenticationException(AuthenticationException exception){
        log.error(exception.getMessage());
        return new ResponseEntity<>(new ErrorResponseDto(HttpStatus.UNAUTHORIZED.value(), exception.getMessage()),null,HttpStatus.UNAUTHORIZED);

    }
    @ExceptionHandler(NotValidRunTimeException.class)
    ResponseEntity<ErrorResponseDto> handleNotValidException(NotValidRunTimeException exception){
        log.error(exception.getMessage());
        return new ResponseEntity<>(new ErrorResponseDto(HttpStatus.UNAUTHORIZED.value(), exception.getMessage()),null,HttpStatus.UNAUTHORIZED);

    }


}
