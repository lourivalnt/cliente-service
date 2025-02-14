package com.clienteservice.exception;

public class ClienteNaoEncontradoException extends RuntimeException {
    public ClienteNaoEncontradoException(Long id) {
        super("Cliente não encontrado com o ID: " + id);
    }
}
