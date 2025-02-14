package com.clienteservice.exception;

public abstract class NegocioException extends RuntimeException {
    public NegocioException(String message) {
        super(message);
    }
}