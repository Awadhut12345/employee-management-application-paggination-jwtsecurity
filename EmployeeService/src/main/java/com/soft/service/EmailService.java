package com.soft.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmployeeRegistrationEmail(String to, String employeeName) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Employee Registration Successful");
        message.setText("Hello " + employeeName + ",\n\nYour registration is successful.");

        mailSender.send(message);
    }
}
