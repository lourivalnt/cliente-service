package com.clienteservice.controller;

import com.clienteservice.dto.ClienteDTO;
import com.clienteservice.mapper.ClienteMapper;
import com.clienteservice.model.Cliente;
import com.clienteservice.pagination.PaginationRequest;
import com.clienteservice.pagination.PageResult;
import com.clienteservice.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/clientes")
@RequiredArgsConstructor
public class ClienteController {
    private final ClienteService clienteService;
    private final ClienteMapper clienteMapper;

    @GetMapping
    public ResponseEntity<List<ClienteDTO>> listarClientes() {
        List<ClienteDTO> clientes = clienteService.listarClientes().stream()
                .map(ClienteMapper.INSTANCE::toDTO)
                .toList();
        return new ResponseEntity<>(clientes, HttpStatus.OK);
    }

    @GetMapping("/paginacao")
    @Operation(summary = "Listar todos os clientes", description = "Retorna uma lista de todos os clientes cadastrados.")
    @ApiResponse(responseCode = "200", description = "Clientes encontrados com sucesso.")
    public ResponseEntity<PageResult<ClienteDTO>> listarClientesComPaginacao(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "nome") String sortBy,
            @RequestParam(defaultValue = "true") boolean ascending) {

        // Chama o service (core)
        PageResult<Cliente> result = clienteService.listarClientesComPaginacao(page, pageSize, sortBy, ascending);

        // Converte entidades para DTOs
        List<ClienteDTO> dtos = result.getContent().stream()
                .map(clienteMapper::toDTO)
                .toList();

        // Monta resposta paginada com DTOs
        PageResult<ClienteDTO> dtoResult = PageResult.<ClienteDTO>builder()
                .content(dtos)
                .totalPages(result.getTotalPages())
                .totalElements(result.getTotalElements())
                .currentPage(result.getCurrentPage())
                .pageSize(result.getPageSize())
                .build();

        return ResponseEntity.ok(dtoResult);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar cliente por ID", description = "Retorna um cliente com base no ID fornecido.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cliente encontrado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado.")
    })
    public ResponseEntity<ClienteDTO> buscarClientePorId(
            @Parameter(description = "ID do cliente", required = true, example = "1")
            @PathVariable Long id) {
        Cliente cliente = clienteService.buscarClientePorId(id);
        return new ResponseEntity<>(ClienteMapper.INSTANCE.toDTO(cliente), HttpStatus.OK);
    }

    @PostMapping
    @Operation(summary = "Cadastrar novo cliente", description = "Cadastra um novo cliente com os dados fornecidos.")
    @ApiResponse(responseCode = "201", description = "Cliente cadastrado com sucesso.")
    public ResponseEntity<ClienteDTO> cadastrarCliente(
            @Parameter(description = "Dados do cliente", required = true)
            @Valid @RequestBody ClienteDTO dto) {
        Cliente cliente = ClienteMapper.INSTANCE.toEntity(dto);
        Cliente clienteSalvo = clienteService.cadastrarCliente(cliente);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/clientes/" + clienteSalvo.getId());

        return new ResponseEntity<>(ClienteMapper.INSTANCE.toDTO(clienteSalvo), headers, HttpStatus.CREATED);
    }

    // ClienteController.java (application)
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar cliente existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cliente atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    public ResponseEntity<ClienteDTO> atualizarCliente(
            @PathVariable Long id,
            @Valid @RequestBody ClienteDTO dto) {

        // Define o ID no DTO para garantir que o cliente correto seja atualizado
        dto.setId(id);
        if (dto.getEndereco() != null) {
            dto.getEndereco().setId(dto.getEndereco().getId());
        }

        // Converte DTO para entidade
        Cliente cliente = clienteMapper.toEntity(dto);

        // Chama o service para atualizar o cliente
        Cliente clienteAtualizado = clienteService.atualizarCliente(cliente);

        // Retorna DTO convertido com status 200 (OK)
        return ResponseEntity.ok(clienteMapper.toDTO(clienteAtualizado));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir cliente", description = "Exclui um cliente com base no ID fornecido.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Cliente excluído com sucesso."),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado.")
    })
    public ResponseEntity<Void> excluirCliente(
            @Parameter(description = "ID do cliente", required = true, example = "1")
            @PathVariable Long id) {
        clienteService.excluirCliente(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}