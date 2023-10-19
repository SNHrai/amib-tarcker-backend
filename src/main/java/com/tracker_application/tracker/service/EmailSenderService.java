package com.tracker_application.tracker.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import com.tracker_application.tracker.model.Mail;

@Service
public class EmailSenderService {

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private SpringTemplateEngine templateEngine;

    public void sendEmail(Mail mail) throws IOException, javax.mail.MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name());

        // Attach the image resource
        // helper.addAttachment("template-cover.png", new
        // ClassPathResource("javabydeveloper-email.PNG"));

        Context context = new Context();
        context.setVariables(mail.getProps());

        // Determine the template to use based on the "type" property
        // String template = mail.getProps().get("type").equals("NEWSLETTER") ?
        // "newsletter-template"
        // : (mail.getProps().get("PASSWORD") ? "password-template" :
        // "inlined-css-template");
        String template = mail.getProps().get("type").equals("PASSWORD")
                ? "password-template"
                : mail.getProps().get("type").equals("PASSWORD") ? "password-template" : "inlined-css-template";

        String html = templateEngine.process(template, context);

        helper.setTo(mail.getMailTo());
        helper.setText(html, true); // 'true' means it's HTML
        helper.setSubject(mail.getSubject());
        helper.setFrom(mail.getFrom());

        emailSender.send(message);
    }
}
