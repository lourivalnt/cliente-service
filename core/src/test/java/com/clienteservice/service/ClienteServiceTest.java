package com.clienteservice.service;

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

//    @Test
//    void deveRetornarClientesPaginados() {
//
//        PageResult<Cliente> response = clienteService.listarClientesComPaginacao(
//                0, 10, "id", true);
//
//        assertEquals(10, response.getContent().size());
//        assertEquals(5, response.getTotalPages());
//    }

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
        Endereco endereco = new Endereco(1L, "12345-678", "Rua A", "100", "Centro", "Apto 10", "São Paulo", "SP");
        Cliente cliente = new Cliente(id, "João Silva", 30, "123.456.789-00", "Engenheiro", endereco);
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
        Endereco endereco = new Endereco(1L, "12345-678", "Rua A", "100", "Centro", "Apto 10", "São Paulo", "SP");
        Cliente cliente = new Cliente(null, "João Silva", 30, "123.456.789-00", "Engenheiro", endereco);
        Cliente clienteSalvo = new Cliente(1L, "João Silva", 30, "123.456.789-00", "Engenheiro", endereco);

        when(clienteRepository.salvar(cliente)).thenReturn(clienteSalvo);

        Cliente resultado = clienteService.cadastrarCliente(cliente);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("João Silva", resultado.getNome());
        assertEquals("Rua A", resultado.getEndereco().getRua());
        verify(clienteRepository, times(1)).salvar(cliente);
    }

    @Test
    void testExcluirCliente() {
        // Arrange
        Long id = 1L;
        when(clienteRepository.existePorId(id)).thenReturn(true);

        // Act
        clienteService.excluirCliente(id);

        // Assert
        verify(clienteRepository, times(1)).existePorId(id);
        verify(clienteRepository, times(1)).excluir(id);
    }

    @Test
    void testExcluirCliente_NaoEncontrado() {
        // Arrange
        Long id = 99L;
        when(clienteRepository.existePorId(id)).thenReturn(false);

        // Act & Assert
        assertThrows(ClienteNaoEncontradoException.class, () -> clienteService.excluirCliente(id));
        verify(clienteRepository, times(1)).existePorId(id);
        verify(clienteRepository, never()).excluir(id); // Verifica que o método excluir nunca foi chamado
    }
}