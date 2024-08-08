package com.rafael.sales.infrastructure.service.email;

import com.rafael.sales.core.email.EmailProperties;
import com.rafael.sales.domain.exception.EmailException;
import com.rafael.sales.domain.service.SendEmailService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.IOException;

@Service
@AllArgsConstructor
public class SmtpSendEmailService implements SendEmailService {

    private JavaMailSender mailSender;
    private EmailProperties emailProperties;
    private Configuration freemarkerConfig;

    @Override
    public void send(Message message) {
        try {
            String body = processorModel(message);

            MimeMessage mimeMessage = mailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
            helper.setFrom(emailProperties.getMailer());
            helper.setTo(message.getReceivers().toArray(new String[0]));
            helper.setSubject(message.getSubject());
            helper.setText(body, true);

            mailSender.send(mimeMessage);
        } catch (Exception e) {
            throw new EmailException("Não foi possível enviar e-mail", e);
        }
    }

    private String processorModel(Message message) {
        try {
            Template template = freemarkerConfig.getTemplate(message.getBody());

            return FreeMarkerTemplateUtils.processTemplateIntoString(template, message.getVariables());
        } catch (Exception e) {
            throw new EmailException("Não foi possivel montar o template do email",e);
        }

    }

}
