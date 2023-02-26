package com.nieghborapp.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nieghborapp.domain.Role;
import com.nieghborapp.domain.User;
import com.nieghborapp.dto.EmailDto;
import com.nieghborapp.dto.RegisterDto;
import com.nieghborapp.exceptions.AlreadyExistsException;
import com.nieghborapp.exceptions.NotFoundException;
import com.nieghborapp.repository.IUserRepository;
import com.nieghborapp.repository.RoleRepository;
import com.nieghborapp.service.IAuthService;
import com.nieghborapp.service.IUserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.utility.RandomString;

import org.springframework.http.MediaType;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;


@Service @Transactional
@AllArgsConstructor @Slf4j
public class AuthService  implements IAuthService, UserDetailsService {
    private final IUserRepository userRepository ;

    private final EmailService emailService;
    private final RoleRepository roleRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(()->new UsernameNotFoundException("user not found "));
        Collection<SimpleGrantedAuthority> authorities =  user.getRoles().stream().map(role ->new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
        return new org.springframework.security.core.userdetails.User(username,
                user.getPassword(),user.isEnabled() , user.isCredentialsNonExpired(),
                user.isCredentialsNonExpired(),user.isAccountNonLocked(),authorities);
    }
    // url : is the base root of my application
    @Override
    public void addUser(RegisterDto registerDto, String url) throws AlreadyExistsException, NotFoundException, MessagingException {

        if(userRepository.existsByUsername(registerDto.getUsername())){
            throw new AlreadyExistsException("user's username already exists ");
        }
        if(userRepository.existsByEmail(registerDto.getEmail())){
            throw new AlreadyExistsException("user's email already exists ");
        }


        User user = new User();
        user.setPassword(new BCryptPasswordEncoder().encode(registerDto.getPassword()));
        user.setUsername(registerDto.getUsername());
        user.setEmail(registerDto.getEmail());
        user.setName(registerDto.getName());
        user.addRole(
                roleRepository.findByName(Role.USER)
                        .orElseThrow(()->new NotFoundException("something went wrong in your registration ")));
        user.setEnabled(false);

        // set a verification code
        String randomCode = RandomString.make(64);

        user.setVerificationCode(randomCode);

        userRepository.save(user);

        sendEmailVerification( user,url);
    }

    public void sendEmailVerification(User user,String url) throws MessagingException {
        String toAddress = user.getEmail();
        String senderEmail = "ayoubtest739@gmail.com";
        String senderName = "Your company name";
        String subject = "Please verify your registration";
        String content = "Dear [[name]],<br>"
                + "Please click the link below to verify your registration:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
                + "Thank you,<br>"
                + "Your company name.";



        content = content.replace("[[name]]",user.getName());

        String verifyUrl = url+"/verify?code="+user.getVerificationCode();

        log.warn(verifyUrl);
        content = content.replace("[[URL]]",verifyUrl);

        EmailDto emailDto = new EmailDto(toAddress,senderEmail,subject,content);
        emailService.sendEmail(emailDto);



    }


    public boolean verifyCode (String code ) throws NotFoundException {
        User user = userRepository.findByVerificationCode(code).orElseThrow(()->  new NotFoundException("user Code invalid or you already verify your account "));
        if(  user.isEnabled()){
            return false;
        }
        user.setVerificationCode(null);
        user.setEnabled(true);
        userRepository.save(user);

        return true;


    }


    @Override
    public void refreshToken(String tokenComplete, HttpServletResponse response) throws IOException {

        log.info("refresh token en cours ");
        if(tokenComplete != null || tokenComplete.startsWith("Baerer ")){
            String refersh_token = tokenComplete.substring("Baerer ".length());

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


        }else {
            throw new RuntimeException("refresh token is missing ");
        }
    }
}
