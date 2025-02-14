package com.clienteservice.ports;

import com.clienteservice.model.Cliente;
import java.util.List;
import java.util.Optional;

public interface ClienteRepositoryPort {
    List<Cliente> buscarTodos();
    Optional<Cliente> buscarPorId(Long id);
    Cliente salvar(Cliente cliente);
    void excluir(Long id);
    boolean existePorId(Long id);
}
