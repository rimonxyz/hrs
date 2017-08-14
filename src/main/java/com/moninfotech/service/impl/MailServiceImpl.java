package com.moninfotech.service.impl;

import com.moninfotech.service.MailService;
import com.sun.mail.util.MailConnectException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.net.UnknownHostException;

@Service
public class MailServiceImpl implements MailService {


    private final JavaMailSender javaMailSender;

    @Autowired
    public MailServiceImpl(@Qualifier("javaMailSenderBean") JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public void sendEmail(String email, String subject, String message) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);
        try {
            javaMailSender.send(mailMessage);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

}
