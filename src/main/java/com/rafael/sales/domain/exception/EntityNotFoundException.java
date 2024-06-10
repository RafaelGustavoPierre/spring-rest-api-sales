package com.rafael.sales.domain.exception;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(String message) {
        super(message);
    }

    public EntityNotFoundException(Long id) {
        this(String.format("Não existe o produto com código %s", id));
    }

}
