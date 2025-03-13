package com.clienteservice.service;

import com.clienteservice.exception.ClienteJaExisteException;
import com.clienteservice.exception.ClienteNaoEncontradoException;
import com.clienteservice.model.Cliente;
import com.clienteservice.model.Endereco;
import com.clienteservice.pagination.PageResult;
import com.clienteservice.ports.ClienteRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @InjectMocks
    private ClienteService clienteService;

    @Mock
    private ClienteRepositoryPort clienteRepository;

    @Test
    void deveRetornarClientesPaginados() {

//        Criar objeto PageResult com dados Mockados
        PageResult<Cliente> pageResult = new PageResult<>(
                List.of(new Cliente(), new Cliente()),
                1,
                2L,
                0,
                2
        );

//         Mockar o método listarClientesComPaginacao do clienteRepository
        when(clienteRepository.listarClientesComPaginacao(0, 2, "nome", true))
                .thenReturn(pageResult);

//         Chamar o método listarClientesComPaginacao do clienteService
        PageResult<Cliente> response = clienteService.listarClientesComPaginacao(
                0, 2, "nome", true);

        assertEquals(pageResult.getContent(), response.getContent());
        assertEquals(pageResult.getTotalPages(), response.getTotalPages());
        assertEquals(pageResult.getTotalElements(), response.getTotalElements());
        assertEquals(pageResult.getCurrentPage(), response.getCurrentPage());
        assertEquals(pageResult.getPageSize(), response.getPageSize());

    }

    @Test
    void testListarClientes() {
        List<Cliente> clientes = getClienteList();
        when(clienteRepository.buscarTodos()).thenReturn(clientes);

        List<Cliente> resultado = clienteService.listarClientes();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("João Silva", resultado.get(0).getNome());
        assertEquals("Rua A", resultado.get(0).getEndereco().getRua());
        verify(clienteRepository, times(1)).buscarTodos();
    }

    private static List<Cliente> getClienteList() {
        Endereco endereco1 = new Endereco(1L, "12345-678", "Rua A", "100", "Centro", "Apto 10", "São Paulo", "SP");
        Endereco endereco2 = new Endereco(2L, "98765-432", "Rua B", "200", "Jardim", null, "Rio de Janeiro", "RJ");

        List<Cliente> clientes = Arrays.asList(
                new Cliente(1L, "João Silva", 30, "123.456.789-00", "Engenheiro", endereco1),
                new Cliente(2L, "Maria Souza", 25, "987.654.321-00", "Advogada", endereco2)
        );
        return clientes;
    }

    @Test
    void testBuscarClientePorId() {
        Long id = 1L;
        Cliente cliente = getCliente();

        when(clienteRepository.buscarPorId(id)).thenReturn(Optional.of(cliente));

        Cliente resultado = clienteService.buscarClientePorId(id);

        assertNotNull(resultado);
        assertEquals("João Silva", resultado.getNome());
        assertEquals("Rua A", resultado.getEndereco().getRua());
        verify(clienteRepository, times(1)).buscarPorId(id);
    }

    @Test
    void testBuscarClientePorId_NaoEncontrado() {
        Long id = 99L;
        when(clienteRepository.buscarPorId(id)).thenReturn(Optional.empty());

        assertThrows(ClienteNaoEncontradoException.class, () -> clienteService.buscarClientePorId(id));
        verify(clienteRepository, times(1)).buscarPorId(id);
    }

    @Test
    void testCadastrarCliente() {
        Cliente cliente = getCliente();

        when(clienteRepository.salvar(cliente)).thenReturn(cliente);

        Cliente resultado = clienteService.cadastrarCliente(cliente);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("João Silva", resultado.getNome());
        assertEquals("Rua A", resultado.getEndereco().getRua());
        verify(clienteRepository, times(1)).salvar(cliente);
    }

    @Test
    void testExcluirCliente() {
        Long id = 1L;
        when(clienteRepository.existePorId(id)).thenReturn(true);

        clienteService.excluirCliente(id);

        verify(clienteRepository, times(1)).existePorId(id);
        verify(clienteRepository, times(1)).excluir(id);
    }

    @Test
    void testExcluirCliente_NaoEncontrado() {
        Long id = 99L;
        when(clienteRepository.existePorId(id)).thenReturn(false);

        assertThrows(ClienteNaoEncontradoException.class, () -> clienteService.excluirCliente(id));
        verify(clienteRepository, times(1)).existePorId(id);
        verify(clienteRepository, never()).excluir(id); // Verifica que o método excluir nunca foi chamado
    }

    @Test
    void atualizarCliente() {
        Cliente cliente = getCliente();

        when(clienteRepository.existePorId(cliente.getId())).thenReturn(true);
        when(clienteRepository.salvar(cliente)).thenReturn(cliente);

        Cliente result = clienteService.atualizarCliente(cliente);

        assertNotNull(result);
        assertEquals(cliente.getId(), result.getId());

        verify(clienteRepository, times(1)).salvar(cliente);

    }

    @Test
    void testClienteJaExisteException() {
       Cliente cliente = getCliente();
       when(clienteRepository.existePorId(cliente.getId())).thenReturn(true);

       assertThrows(ClienteJaExisteException.class, () -> clienteService.cadastrarCliente(cliente));
    }

    @Test
    void testClienteNaoEncontradoException() {
        Cliente cliente = getCliente();

        when(clienteRepository.existePorId(cliente.getId())).thenReturn(false);

        assertThrows(ClienteNaoEncontradoException.class, () -> clienteService.atualizarCliente(cliente));
    }

    private static Cliente getCliente() {
        Endereco endereco = new Endereco(1L, "12345-678", "Rua A", "100", "Centro", "Apto 10", "São Paulo", "SP");
        Cliente cliente = new Cliente(1L, "João Silva", 30, "123.456.789-00", "Engenheiro", endereco);

        return cliente;
    }

}