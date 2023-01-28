package com.nieghborapp.service.impl;

import com.nieghborapp.domain.Role;
import com.nieghborapp.domain.User;
import com.nieghborapp.dto.RegisterDto;
import com.nieghborapp.repository.IUserRepository;
import com.nieghborapp.repository.RoleRepository;
import com.nieghborapp.service.IUserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.utility.RandomString;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Collection;
import java.util.stream.Collectors;


@Service @Transactional
@AllArgsConstructor @Slf4j
public class UserService  implements IUserService, UserDetailsService {
    private final IUserRepository userRepository ;
    private final JavaMailSender mailSender ;
    private final RoleRepository roleRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(()->new UsernameNotFoundException("user not found "));
        Collection<SimpleGrantedAuthority> authorities =  user.getRoles().stream().map(role ->new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
        return new org.springframework.security.core.userdetails.User(username,
                                                                    user.getPassword(),user.isEnabled() , user.isCredentialsNonExpired(),
                                                                    user.isCredentialsNonExpired(),user.isAccountNonLocked(),authorities);
    }
    @Override
    public void addUser(RegisterDto registerDto, String url) throws Exception {


        // verify if user is already here
        if(userRepository.existsByUsername(registerDto.getUsername())){
            throw new Exception("user already here ");
        }

        User user = new User();
        user.setPassword(new BCryptPasswordEncoder().encode(registerDto.getPassword()));
        user.setUsername(registerDto.getUsername());
        user.setEmail(registerDto.getEmail());
        user.setName(registerDto.getName());
        user.addRole(roleRepository.findByName(Role.USER).orElseThrow());
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

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message);

            messageHelper.setSubject(subject);
            messageHelper.setTo(toAddress);
            messageHelper.setFrom(senderEmail);

            content = content.replace("[[name]]",user.getName());

            String verifyUrl = url+"/verify?code="+user.getVerificationCode();

            log.warn(verifyUrl);

            content = content.replace("[[URL]]",verifyUrl);

            messageHelper.setText(content,true);
            log.info("before sending message ");


            mailSender.send(message);
        }catch (Exception exception){
            log.error(exception.getMessage());
        }


    }


    public boolean verifyCode (String code ) throws Exception {
        User user = userRepository.findByVerificationCode(code).orElseThrow(()->new Exception("user Code invalid or you already verify your account "));
        if( user == null || user.isEnabled()){
           return false;
        }
        user.setVerificationCode(null);
        user.setEnabled(true);
        userRepository.save(user);

        return true;


    }
}
