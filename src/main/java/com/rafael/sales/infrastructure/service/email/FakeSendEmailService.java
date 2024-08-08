package com.rafael.sales.infrastructure.service.email;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class FakeSendEmailService extends SmtpSendEmailService {

    @Override
    public void send(Message message) {
        String body = processorModel(message);

        log.info("[FAKE EMAIL] Para: {}\n{}", message.getReceivers(), body);
    }
}
