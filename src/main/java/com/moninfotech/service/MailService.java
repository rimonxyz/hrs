package com.moninfotech.service;

import com.sun.mail.util.MailConnectException;

import java.net.UnknownHostException;

public interface MailService {

    void sendEmail(String email, String subject, String message);

}
