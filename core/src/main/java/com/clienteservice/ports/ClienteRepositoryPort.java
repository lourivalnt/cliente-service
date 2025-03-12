package com.clienteservice.ports;

import com.clienteservice.model.Cliente;
import com.clienteservice.pagination.PageResult;
import com.clienteservice.pagination.PaginationRequest;

import java.util.List;
import java.util.Optional;

public interface ClienteRepositoryPort {
    List<Cliente> buscarTodos();
    PageResult<Cliente> listarClientesComPaginacao(int page, int pageSize, String sortBy, boolean ascending);
    Optional<Cliente> buscarPorId(Long id);
    Cliente salvar(Cliente cliente);
    void excluir(Long id);
    boolean existePorId(Long id);
}
