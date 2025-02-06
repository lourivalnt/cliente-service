package com.clienteservice.controller;

import com.clienteservice.dto.ClienteDTO;
import com.clienteservice.mapper.ClienteMapper;
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
    private final ClienteMapper clienteMapper;

    @GetMapping
    public List<ClienteDTO> listarClientes() {
        return clienteService.listarClientes().stream()
                .map(clienteMapper::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public Optional<ClienteDTO> buscarClientePorId(@PathVariable Long id) {
        return clienteService.buscarClientePorId(id)
                .map(clienteMapper::toDTO);
    }

    @PostMapping
    public ClienteDTO cadastrarCliente(@RequestBody ClienteDTO dto) {
        return clienteMapper.toDTO(clienteService.cadastrarCliente(clienteMapper.toEntity(dto)));
    }

    @DeleteMapping("/{id}")
    public void excluirCliente(@PathVariable Long id) {
        clienteService.excluirCliente(id);
    }
}
