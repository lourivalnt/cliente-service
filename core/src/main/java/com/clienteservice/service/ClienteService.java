package com.clienteservice.service;

import com.clienteservice.model.Cliente;
import com.clienteservice.ports.ClienteRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor // Gera um construtor com os campos final
public class ClienteService {
    private final ClienteRepositoryPort clienteRepository;

    public List<Cliente> listarClientes() {
        return clienteRepository.buscarTodos();
    }

    public Optional<Cliente> buscarClientePorId(Long id) {
        return clienteRepository.buscarPorId(id);
    }

    public Cliente cadastrarCliente(Cliente cliente) {
        return clienteRepository.salvar(cliente);
    }

    public void excluirCliente(Long id) {
        clienteRepository.excluir(id);
    }
}
