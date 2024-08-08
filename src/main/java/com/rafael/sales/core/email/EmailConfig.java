package com.rafael.sales.core.email;

import com.rafael.sales.domain.service.SendEmailService;
import com.rafael.sales.infrastructure.service.email.FakeSendEmailService;
import com.rafael.sales.infrastructure.service.email.SmtpSendEmailService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class EmailConfig {

    private EmailProperties emailProperties;

    @Bean
    public SendEmailService sendEmailService() {
        switch (emailProperties.getImpl()) {
            case FAKE:
                return new FakeSendEmailService();
            case SMTP:
                return new SmtpSendEmailService();
            default:
                return null;
        }
    }

}
