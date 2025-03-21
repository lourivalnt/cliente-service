package com.clienteservice.service;

import com.clienteservice.exception.ClienteJaExisteException;
import com.clienteservice.exception.ClienteNaoEncontradoException;
import com.clienteservice.model.Cliente;
import com.clienteservice.pagination.PageResult;
import com.clienteservice.ports.ClienteRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor // Gera um construtor com os campos final
public class ClienteService {
    private final ClienteRepositoryPort clienteRepository;

    public List<Cliente> listarClientes() {
        return clienteRepository.buscarTodos();
    }

    public PageResult<Cliente> listarClientesComPaginacao(
            int page,
            int pageSize,
            String sortBy,
            boolean ascending) {
        return clienteRepository.listarClientesComPaginacao(page, pageSize, sortBy, ascending);
    }

    public Cliente buscarClientePorId(Long id) {
        return clienteRepository.buscarPorId(id)
                .orElseThrow(() -> new ClienteNaoEncontradoException(id));
    }

    public Cliente cadastrarCliente(Cliente cliente) {
        // Validações adicionais podem ser feitas aqui (ex: CPF único)
        if (clienteRepository.existePorId(cliente.getId())) {
            throw new ClienteJaExisteException(cliente.getId());
        }

        return clienteRepository.salvar(cliente);
    }

    public Cliente atualizarCliente(Cliente cliente) {
        // Verifica se o cliente existe
        if (!clienteRepository.existePorId(cliente.getId())) {
            throw new ClienteNaoEncontradoException(cliente.getId());
        }

        // Atualiza o cliente no repositório
        return clienteRepository.salvar(cliente);
    }

    public void excluirCliente(Long id) {
        // Verifica se o cliente existe antes de tentar excluí-lo
        if (!clienteRepository.existePorId(id)) {
            throw new ClienteNaoEncontradoException(id);
        }
        clienteRepository.excluir(id);
    }

}
