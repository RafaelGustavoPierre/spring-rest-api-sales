package com.rafael.sales.domain.listener;

import com.rafael.sales.domain.event.SendEmailEvent;
import com.rafael.sales.domain.model.Sale;
import com.rafael.sales.domain.service.SendEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class SendEmailListener {

    @Autowired
    private SendEmailService emailService;

    @TransactionalEventListener
    public void mailSending(SendEmailEvent event) {
        Sale sale = event.getSale();

        String subject = String.format("%s - Venda", sale.getUser().getName());

        SendEmailService.Message message = SendEmailService.Message.builder()
                .receiver(sale.getUser().getEmail())
                .subject(subject)
                .body("sale-emitida.html")
                .variable("sale", sale)
                .build();
        emailService.send(message);
    }

}