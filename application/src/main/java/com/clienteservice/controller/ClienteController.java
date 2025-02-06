package com.clienteservice.controller;

import com.clienteservice.dto.ClienteDTO;
import com.clienteservice.mapper.ClienteMapper;
import com.clienteservice.model.Cliente;
import com.clienteservice.service.ClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/clientes")
@RequiredArgsConstructor
public class ClienteController {
    private final ClienteService clienteService;

    @GetMapping
    public List<ClienteDTO> listarClientes() {
        return clienteService.listarClientes().stream()
                .map(ClienteMapper.INSTANCE::toDTO) // Chamada direta ao mapper
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public Optional<ClienteDTO> buscarClientePorId(@PathVariable Long id) {
        return clienteService.buscarClientePorId(id)
                .map(ClienteMapper.INSTANCE::toDTO); // Chamada direta ao mapper
    }

    @PostMapping
    public ClienteDTO cadastrarCliente(@RequestBody ClienteDTO dto) {
        Cliente cliente = ClienteMapper.INSTANCE.toEntity(dto); // Chamada direta ao mapper
        Cliente clienteSalvo = clienteService.cadastrarCliente(cliente);
        return ClienteMapper.INSTANCE.toDTO(clienteSalvo); // Chamada direta ao mapper
    }

    @DeleteMapping("/{id}")
    public void excluirCliente(@PathVariable Long id) {
        clienteService.excluirCliente(id);
    }
}