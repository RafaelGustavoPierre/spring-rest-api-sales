package com.rafael.sales.api.exceptionhandler;

import lombok.Getter;

@Getter
public enum ProblemType {

    ENTITY_CONFLICT("/conflito-de-entidades", "Conflito de entidades"),
    INSUFFICIENT_STOCK("/estoque-insuficiente", "Estoque insuficiente"),
    RESOURCE_NOT_FOUND("/recurso-nao-encontrado", "Recurso não encontrado"),
    MEDIA_TYPE_NOT_SUPPORTED("/midia-nao-suportada", "Formato Inválido"),
    ERROR_SYNTAX("/error-de-sintaxe", "Campos preenchidos incorretamente"),
    INVALID_DATA("/dados-invalidos", "Dados inválidos"),
    ITEM_NOT_FOUND("/item-nao-encontrado", "Item não encontrado"),
    ENTITY_IN_USE("/entidade-em-uso", "Entidade está em uso");

    private String title;
    private String uri;

    ProblemType(String path, String title) {
        this.title = title;
        this.uri = "https://localhost:8080" + path;
    }

}
