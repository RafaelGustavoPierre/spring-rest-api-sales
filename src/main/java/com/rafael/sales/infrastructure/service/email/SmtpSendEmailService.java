package com.rafael.sales.infrastructure.service.email;

import com.rafael.sales.core.email.EmailProperties;
import com.rafael.sales.domain.exception.EmailException;
import com.rafael.sales.domain.service.SendEmailService;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SmtpSendEmailService implements SendEmailService {

    private JavaMailSender mailSender;
    private EmailProperties emailProperties;

    @Override
    public void send(Message message) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
            helper.setFrom(emailProperties.getMailer());
            helper.setTo(message.getReceivers().toArray(new String[0]));
            helper.setSubject(message.getSubject());
            helper.setText(message.getBody(), true);

            mailSender.send(mimeMessage);
        } catch (Exception e) {
            throw new EmailException("Não foi possível enviar e-mail", e);
        }
    }

}
