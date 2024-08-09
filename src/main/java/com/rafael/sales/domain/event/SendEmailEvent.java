package com.rafael.sales.domain.event;

import com.rafael.sales.domain.model.Sale;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SendEmailEvent {

    private Sale sale;

}
