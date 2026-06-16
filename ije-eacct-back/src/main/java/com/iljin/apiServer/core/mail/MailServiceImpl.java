package com.iljin.apiServer.core.mail;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;
import java.util.Arrays;

@RequiredArgsConstructor
@Service
public class MailServiceImpl implements MailService {
    private final JavaMailSender emailSender;

    @Autowired
    private Environment env;

    @Override
    public void sendSimpleMessage(String from, String to, String subject, String text) {
        try {

            MimeMessage mimeMessage = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "UTF-8");

            if (from == null) {
                from = "system@iljin.co.kr";
            }
            mimeMessage.setContent(text, "text/html; charset=utf-8");

            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);

            //profiles.active가 production일 때, 메일 발송
            String activeProperties = "production";
            if(Arrays.stream(env.getActiveProfiles()).anyMatch(
                    e -> (e.equalsIgnoreCase(activeProperties)) )){
                emailSender.send(mimeMessage);
            }

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void sendSimpleMessage(MailDto mailDto) {
        sendSimpleMessage(mailDto.getFrom(), mailDto.getTo(), mailDto.getSubject(), mailDto.getText());
    }
}
