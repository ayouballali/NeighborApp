package com.nieghborapp.service.impl;

import com.nieghborapp.dto.EmailDto;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@RequiredArgsConstructor @Service
public class EmailService {
    private final JavaMailSender mailSender ;


    public void sendEmail(EmailDto emailDto) throws MessagingException {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(message);

        messageHelper.setSubject(emailDto.getSubject());
        messageHelper.setTo(emailDto.getToAddress());
        messageHelper.setFrom(emailDto.getSenderAddress());


        messageHelper.setText(emailDto.getContent(),true);


        mailSender.send(message);

    }
}
