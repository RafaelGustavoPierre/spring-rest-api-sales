package com.rafael.sales.domain.exception;

public class EntityConflictException extends RuntimeException {

    public EntityConflictException(String message) {
        super(message);
    }

}
