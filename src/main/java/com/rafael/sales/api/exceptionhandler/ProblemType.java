package com.rafael.sales.api.exceptionhandler;

import lombok.Getter;

@Getter
public enum ProblemType {

    INVALID_DATA("/dados-invalidos", "Dados inválidos"),
    ITEM_NOT_FOUND("/item-nao-encontrado", "Item não encontrado");

    private String title;
    private String uri;

    ProblemType(String path, String title) {
        this.title = title;
        this.uri = "https://localhost:8080" + path;
    }

}
