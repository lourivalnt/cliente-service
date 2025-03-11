package com.clienteservice.controller;

import com.clienteservice.dto.ClienteDTO;
import com.clienteservice.mapper.ClienteMapper;
import com.clienteservice.model.Cliente;
import com.clienteservice.model.PaginationRequest;
import com.clienteservice.model.PaginationResponse;
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
import java.util.stream.Collectors;

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
    public ResponseEntity<PaginationResponse<ClienteDTO>> listarClientesComPaginacao(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "true") boolean ascending) {

        // Cria request de paginação
        PaginationRequest pagination = PaginationRequest.builder()
                .page(page)
                .pageSize(pageSize)
                .sortBy(sortBy)
                .ascending(ascending)
                .build();

        PaginationResponse<Cliente> response = clienteService.listarClientesComPaginacao(page, pageSize, sortBy, ascending);

        // Converte entidades para DTOs
        List<ClienteDTO> dtos = response.getContent().stream()
                .map(clienteMapper::toDTO)
                .toList();

        PaginationResponse<ClienteDTO> dtoResponse = PaginationResponse.<ClienteDTO>builder()
                .content(dtos)
                .totalPages(response.getTotalPages())
                .totalElements(response.getTotalElements())
                .currentPage(response.getCurrentPage())
                .pageSize(response.getPageSize())
                .build();

        return ResponseEntity.ok(dtoResponse);
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