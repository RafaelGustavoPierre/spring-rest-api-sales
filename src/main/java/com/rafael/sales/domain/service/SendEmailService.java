package com.rafael.sales.domain.service;

import lombok.Builder;
import lombok.Getter;
import lombok.Singular;

import java.util.Set;

public interface SendEmailService {

    void send(Message message);

    @Getter
    @Builder
    class Message {

        @Singular
        private Set<String> receivers;

        private String subject;
        private String body;

    }

}
