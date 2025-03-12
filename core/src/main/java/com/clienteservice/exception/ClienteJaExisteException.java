package com.clienteservice.exception;

public class ClienteJaExisteException extends RuntimeException {
    public ClienteJaExisteException(Long id) {
        super("Já existe um cliente com este ID: " + id);
    }
}
