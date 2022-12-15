package com.ggapp.services.implement;

import com.ggapp.common.dto.request.LoginRequest;
import com.ggapp.common.exception.ApplicationException;
import com.ggapp.services.MailService;
import io.github.classgraph.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.activation.DataSource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * @author Tran Minh Truyen on 04/12/2022
 * Dear maintainer.
 * When I wrote this code, only me and God knew what is was.
 * Now, only God knows!
 * So if you are done trying to 'optimize' this routine (and failed), please increment the
 * following counter as a warning to the next guy
 * TOTAL_HOURS_WASTED_HERE =
 */
@Service
public class MailServiceImp implements MailService {

    private final JavaMailSender javaMailSender;

    private final SpringTemplateEngine templateEngine;


    @Value("${spring.mail.username}")
    private String mail;

    public MailServiceImp(JavaMailSender javaMailSender, MessageSource messageSource, SpringTemplateEngine templateEngine) {
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
    }

    @Override
    public void sendEmailRegisterConfirmKey(String email, String key) throws ApplicationException {
        Context context = new Context();
        context.setVariable("name", email);
        context.setVariable("key", key);
        String content = templateEngine.process("confirmKeyMailTemplate", context);
        String subject = "GG-App Register Confirm Key";
        sendMail(email, subject, content);
    }

    @Override
    public void sendEmailLoginConfirmKey(LoginRequest loginRequest) throws ApplicationException {
    }

    void sendMail(String to, String subject, String content) throws ApplicationException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, StandardCharsets.UTF_8.name());
            File file = new ClassPathResource("/static/image/GGexamplelogo.jpg").getFile();
            message.setFrom(mail);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(content, true);
            message.addInline("attachment.png", file);
            javaMailSender.send(mimeMessage);
        }  catch (MailException | MessagingException e) {
            throw new ApplicationException(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (IOException exception) {
            throw new ApplicationException(exception.getMessage(), exception.getCause(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
