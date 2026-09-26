package com.example.kadai_002.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    private final JavaMailSender mailSender;

    public MailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendVerificationMail(String email, String verificationUrl) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(email);
        message.setSubject("【店舗検索サイト】メールアドレス認証");
        message.setText(
            "会員登録ありがとうございます。\n\n" +
            "以下のURLをクリックしてメールアドレスを認証してください。\n\n" +
            verificationUrl
        );

        mailSender.send(message);
    }
}