package com.rafael.sales.api.exceptionhandler;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL) // Deixa de exibir valores Null
@Builder
@Getter
public class Problem {

    private Integer status;
    private String title;
    private String detail;
    private String userMessage;
    private String type;
    private List<Object> objects;
    private LocalDateTime timestamp;

    @Builder
    @Getter
    public static class Object {
        private String name;
        private String userMessage;
    }

}
