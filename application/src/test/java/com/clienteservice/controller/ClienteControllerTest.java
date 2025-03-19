package com.clienteservice.controller;

import com.clienteservice.dto.ClienteDTO;
import com.clienteservice.dto.EnderecoDTO;
import com.clienteservice.mapper.ClienteMapper;
import com.clienteservice.model.Cliente;
import com.clienteservice.model.Endereco;
import com.clienteservice.pagination.PageResult;
import com.clienteservice.service.ClienteService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteControllerTest {

    @InjectMocks
    private ClienteController clienteController;

    @Mock
    private ClienteService clienteService;

    @Mock
    private ClienteMapper clienteMapper;

    @Test
    void testListarClientes() {
        List<Cliente> clientes = getClienteList();
        when(clienteService.listarClientes()).thenReturn(clientes);

        ResponseEntity<List<ClienteDTO>> response = clienteController.listarClientes();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        assertEquals("João Silva", response.getBody().get(0).getNome());
        verify(clienteService, times(1)).listarClientes();
    }

    @Test
    void testBuscarClientePorId() {
        Long id = 1L;
        Cliente cliente = getCliente();
        when(clienteService.buscarClientePorId(cliente.getId())).thenReturn(cliente);

        ResponseEntity<ClienteDTO> response = clienteController.buscarClientePorId(id);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("João Silva", response.getBody().getNome());
        verify(clienteService, times(1)).buscarClientePorId(id);
    }

    @Test
    void testBuscarClientePorId_NaoEncontrado() {
        Long id = 99L;
        when(clienteService.buscarClientePorId(id)).thenThrow(new RuntimeException("Cliente não encontrado"));

        assertThrows(RuntimeException.class, () -> clienteController.buscarClientePorId(id));
        verify(clienteService, times(1)).buscarClientePorId(id);
    }

    @Test
    void testCadastrarCliente() {
        Endereco endereco = new Endereco(1L, "12345-678", "Rua A", "100", "Centro", "Apto 10", "São Paulo", "SP");
        EnderecoDTO enderecoDTO = new EnderecoDTO(1L, "12345-678", "Rua A", "100", "Centro", "Apto 10", "São Paulo", "SP");
        ClienteDTO dto = new ClienteDTO(null, "João Silva", 30, "123.456.789-00", "Engenheiro", enderecoDTO);
        Cliente cliente = ClienteMapper.INSTANCE.toEntity(dto);
        Cliente clienteSalvo = new Cliente(1L, "João Silva", 30, "123.456.789-00", "Engenheiro", endereco);

        when(clienteService.cadastrarCliente(cliente)).thenReturn(clienteSalvo);

        ResponseEntity<ClienteDTO> response = clienteController.cadastrarCliente(dto);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("João Silva", response.getBody().getNome());
        verify(clienteService, times(1)).cadastrarCliente(cliente);
    }

    @Test
    void testExcluirCliente() {
        Long id = 1L;
        doNothing().when(clienteService).excluirCliente(id);

        ResponseEntity<Void> response = clienteController.excluirCliente(id);

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(clienteService, times(1)).excluirCliente(id);
    }

    @Test
    void testExcluirCliente_NaoEncontrado() {
        Long id = 99L;
        doThrow(new RuntimeException("Cliente não encontrado")).when(clienteService).excluirCliente(id);
        
        assertThrows(RuntimeException.class, () -> clienteController.excluirCliente(id));
        verify(clienteService, times(1)).excluirCliente(id);
    }

    @Test
    void testListarClientesComPaginacao() {

        int page = 0;
        int pageSize = 10;
        String sortBy = "nome";
        boolean ascending = true;

//      Criar objeto PageResult com dados Mockados
        PageResult<Cliente> pageResult = new PageResult<>(
                List.of(new Cliente(), new Cliente()),
                1,
                2L,
                page, pageSize
        );

        // Mock do ClienteDTO
        ClienteDTO clienteDTO = new ClienteDTO();
        clienteDTO.setId(1L);
        clienteDTO.setNome("João Silva");

        when(clienteMapper.toDTO(any(Cliente.class))).thenReturn(clienteDTO);
        when(clienteService.listarClientesComPaginacao(page, pageSize, sortBy, ascending)).thenReturn(pageResult);


        ResponseEntity<PageResult<ClienteDTO>> response = clienteController.listarClientesComPaginacao(page, pageSize, sortBy, ascending);

        PageResult<ClienteDTO> dtoPageResult = response.getBody();

        PageResult<ClienteDTO> dtoResult = response.getBody();
        assertNotNull(dtoResult);
        assertEquals(1, dtoResult.getTotalPages());
        assertEquals(2L, dtoResult.getTotalElements());
        assertEquals(page, dtoResult.getCurrentPage());
        assertEquals(pageSize, dtoResult.getPageSize());

        List<ClienteDTO> dtos = dtoResult.getContent();
        assertEquals(2, dtos.size());
        assertEquals(clienteDTO, dtos.get(0));
        assertEquals(clienteDTO, dtos.get(1));

        verify(clienteService, times(1)).listarClientesComPaginacao(page, pageSize, sortBy, ascending);
        verify(clienteMapper, times(2)).toDTO(any(Cliente.class));
    }

    @Test
    void atualizarCliente() {

        Long id = 1L;
        Endereco endereco = new Endereco(id, "12345-678", "Rua A", "100", "Centro", "Apto 10", "São Paulo", "SP");
        EnderecoDTO enderecoDTO = new EnderecoDTO(id, "12345-678", "Rua A", "100", "Centro", "Apto 10", "São Paulo", "SP");

        Cliente cliente = new Cliente(id, "João Silva", 30, "123.456.789-00", "Engenheiro", endereco);
        ClienteDTO dto = new ClienteDTO(id, "João Silva", 30, "123.456.789-00", "Engenheiro", enderecoDTO);
        when(clienteMapper.toEntity(dto)).thenReturn(cliente);

        Cliente clienteAtualizado = new Cliente(id, "João Silva Atualizado", 35, "123.456.789-00", "Engenheiro", endereco);
        when(clienteService.atualizarCliente(cliente)).thenReturn(clienteAtualizado);
        ClienteDTO dtoAtulizado = new ClienteDTO(id, "João Silva Atualizado", 35, "123.456.789-00", "Engenheiro", enderecoDTO);
        when(clienteMapper.toDTO(clienteAtualizado)).thenReturn(dtoAtulizado);

        ResponseEntity<ClienteDTO> response = clienteController.atualizarCliente(id, dto);

        ClienteDTO responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("João Silva Atualizado", responseBody.getNome());
        assertEquals(35, responseBody.getIdade());
        assertEquals("123.456.789-00", responseBody.getCpf());
        assertEquals("Engenheiro", responseBody.getProfissao());

        // Verificar se o endereço foi atualizado corretamente
        assertNotNull(responseBody.getEndereco());
        assertEquals("12345-678", responseBody.getEndereco().getCep());

        // Verificar se o método do service foi chamado corretamente
        verify(clienteService, times(1)).atualizarCliente(cliente);
    }


    private static Cliente getCliente() {
        Endereco endereco = new Endereco(1L, "12345-678", "Rua A", "100", "Centro", "Apto 10", "São Paulo", "SP");
        Cliente cliente = new Cliente(1l, "João Silva", 30, "123.456.789-00", "Engenheiro", endereco);
        return cliente;
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
}