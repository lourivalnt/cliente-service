// ClienteCacheDecorator.java (application)
package com.clienteservice.repository;

import com.clienteservice.model.Cliente;
import com.clienteservice.ports.ClienteRepositoryPort;
import com.clienteservice.ports.ClienteCachePort;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class ClienteCacheDecorator implements ClienteRepositoryPort {

    private final ClienteRepositoryPort repository; // Repositório JDBC
    private final ClienteCachePort cache; // Adaptador Redis

    @Override
    public Optional<Cliente> buscarPorId(Long id) {
        // Busca primeiro no cache
        return cache.buscarPorId(id)
                .or(() -> repository.buscarPorId(id) // Se não encontrar, busca no banco
                        .map(cliente -> {
                            cache.salvar(cliente); // Atualiza o cache
                            return cliente;
                        }));
    }

    @Override
    public List<Cliente> buscarTodos() {
        // Não implementamos cache para listas aqui (pode ser complexo)
        return repository.buscarTodos();
    }

    @Override
    public Cliente salvar(Cliente cliente) {
        Cliente clienteSalvo = repository.salvar(cliente);
        cache.salvar(clienteSalvo); // Atualiza o cache após salvar no banco
        return clienteSalvo;
    }

    @Override
    public void excluir(Long id) {
        repository.excluir(id);
        cache.remover(id); // Remove do cache após excluir do banco
    }

    @Override
    public boolean existePorId(Long id) {
        return repository.existePorId(id);
    }
}