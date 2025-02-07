package com.clienteservice.controller;

import com.clienteservice.dto.ClienteDTO;
import com.clienteservice.mapper.ClienteMapper;
import com.clienteservice.model.Cliente;
import com.clienteservice.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/clientes")
@RequiredArgsConstructor
@Tag(name = "Clientes", description = "API para gerenciamento de clientes")
public class ClienteController {
    private final ClienteService clienteService;

    @GetMapping
    @Operation(summary = "Listar todos os clientes", description = "Retorna uma lista de todos os clientes cadastrados.")
    @ApiResponse(responseCode = "200", description = "Clientes encontrados com sucesso.")
    public List<ClienteDTO> listarClientes() {
        return clienteService.listarClientes().stream()
                .map(ClienteMapper.INSTANCE::toDTO) // Chamada direta ao mapper
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar cliente por ID", description = "Retorna um cliente com base no ID fornecido.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cliente encontrado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado.")
    })
    public Optional<ClienteDTO> buscarClientePorId(@PathVariable Long id) {
        return clienteService.buscarClientePorId(id)
                .map(ClienteMapper.INSTANCE::toDTO); // Chamada direta ao mapper
    }

    @PostMapping
    @Operation(summary = "Cadastrar novo cliente", description = "Cadastra um novo cliente com os dados fornecidos.")
    @ApiResponse(responseCode = "200", description = "Cliente cadastrado com sucesso.")
    public ClienteDTO cadastrarCliente(@RequestBody ClienteDTO dto) {
        Cliente cliente = ClienteMapper.INSTANCE.toEntity(dto); // Chamada direta ao mapper
        Cliente clienteSalvo = clienteService.cadastrarCliente(cliente);
        return ClienteMapper.INSTANCE.toDTO(clienteSalvo); // Chamada direta ao mapper
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir cliente", description = "Exclui um cliente com base no ID fornecido.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Cliente excluído com sucesso."),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado.")
    })
    public void excluirCliente(@PathVariable Long id) {
        clienteService.excluirCliente(id);
    }
}