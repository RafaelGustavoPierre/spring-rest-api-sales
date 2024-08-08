package com.rafael.sales.core.email;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties("sales.email")
public class EmailProperties {

    private Implementation impl = Implementation.FAKE;

    @NotNull
    private String mailer;

    public enum Implementation {
        SMTP, FAKE
    }

}
