//package com.clienteservice.controller;
//
//import com.clienteservice.dto.ClienteDTO;
//import com.clienteservice.dto.EnderecoDTO;
//import com.clienteservice.mapper.ClienteMapper;
//import com.clienteservice.model.Cliente;
//import com.clienteservice.model.Endereco;
//import com.clienteservice.service.ClienteService;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//
//import java.util.Arrays;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class ClienteControllerTest {
//
//    @InjectMocks
//    private ClienteController clienteController;
//
//    @Mock
//    private ClienteService clienteService;
//
//    @Test
//    void testListarClientes() {
//        // Arrange
//        Endereco endereco1 = new Endereco(1L, "12345-678", "Rua A", "100", "Centro", "Apto 10", "São Paulo", "SP");
//        Endereco endereco2 = new Endereco(2L, "98765-432", "Rua B", "200", "Jardim", null, "Rio de Janeiro", "RJ");
//
//        List<Cliente> clientes = Arrays.asList(
//                new Cliente(1L, "João Silva", 30, "123.456.789-00", "Engenheiro", endereco1),
//                new Cliente(2L, "Maria Souza", 25, "987.654.321-00", "Advogada", endereco2)
//        );
//        when(clienteService.listarClientes()).thenReturn(clientes);
//
//        // Act
//        ResponseEntity<List<ClienteDTO>> response = clienteController.listarClientes();
//
//        // Assert
//        assertNotNull(response);
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(2, response.getBody().size());
//        assertEquals("João Silva", response.getBody().get(0).getNome());
//        verify(clienteService, times(1)).listarClientes();
//    }
//
//    @Test
//    void testBuscarClientePorId() {
//        // Arrange
//        Long id = 1L;
//        Endereco endereco = new Endereco(1L, "12345-678", "Rua A", "100", "Centro", "Apto 10", "São Paulo", "SP");
//        Cliente cliente = new Cliente(id, "João Silva", 30, "123.456.789-00", "Engenheiro", endereco);
//        when(clienteService.buscarClientePorId(id)).thenReturn(cliente);
//
//        // Act
//        ResponseEntity<ClienteDTO> response = clienteController.buscarClientePorId(id);
//
//        // Assert
//        assertNotNull(response);
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals("João Silva", response.getBody().getNome());
//        verify(clienteService, times(1)).buscarClientePorId(id);
//    }
//
//    @Test
//    void testBuscarClientePorId_NaoEncontrado() {
//        // Arrange
//        Long id = 99L;
//        when(clienteService.buscarClientePorId(id)).thenThrow(new RuntimeException("Cliente não encontrado"));
//
//        // Act & Assert
//        assertThrows(RuntimeException.class, () -> clienteController.buscarClientePorId(id));
//        verify(clienteService, times(1)).buscarClientePorId(id);
//    }
//
//    @Test
//    void testCadastrarCliente() {
//        // Arrange
//        Endereco endereco = new Endereco(1L, "12345-678", "Rua A", "100", "Centro", "Apto 10", "São Paulo", "SP");
//        EnderecoDTO enderecoDTO = new EnderecoDTO(1L, "12345-678", "Rua A", "100", "Centro", "Apto 10", "São Paulo", "SP");
//        ClienteDTO dto = new ClienteDTO(null, "João Silva", 30, "123.456.789-00", "Engenheiro", enderecoDTO);
//        Cliente cliente = ClienteMapper.INSTANCE.toEntity(dto);
//        Cliente clienteSalvo = new Cliente(1L, "João Silva", 30, "123.456.789-00", "Engenheiro", endereco);
//        when(clienteService.cadastrarCliente(cliente)).thenReturn(clienteSalvo);
//
//        // Act
//        ResponseEntity<ClienteDTO> response = clienteController.cadastrarCliente(dto);
//
//        // Assert
//        assertNotNull(response);
//        assertEquals(HttpStatus.CREATED, response.getStatusCode());
//        assertEquals("João Silva", response.getBody().getNome());
//        verify(clienteService, times(1)).cadastrarCliente(cliente);
//    }
//
//    @Test
//    void testExcluirCliente() {
//        // Arrange
//        Long id = 1L;
//        doNothing().when(clienteService).excluirCliente(id);
//
//        // Act
//        ResponseEntity<Void> response = clienteController.excluirCliente(id);
//
//        // Assert
//        assertNotNull(response);
//        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
//        verify(clienteService, times(1)).excluirCliente(id);
//    }
//
//    @Test
//    void testExcluirCliente_NaoEncontrado() {
//        // Arrange
//        Long id = 99L;
//        doThrow(new RuntimeException("Cliente não encontrado")).when(clienteService).excluirCliente(id);
//
//        // Act & Assert
//        assertThrows(RuntimeException.class, () -> clienteController.excluirCliente(id));
//        verify(clienteService, times(1)).excluirCliente(id);
//    }
//}