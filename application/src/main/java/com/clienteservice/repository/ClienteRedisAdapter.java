package com.clienteservice.repository;

import com.clienteservice.dto.ClienteDTO;
import com.clienteservice.model.Cliente;
import com.clienteservice.ports.ClienteCachePort;
import com.clienteservice.mapper.ClienteMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class ClienteRedisAdapter implements ClienteCachePort {

    private final RedisTemplate<String, ClienteDTO> redisTemplate; // Trabalha com DTO
    private final ClienteMapper clienteMapper; // Mapeia entre DTO e entidade
    private static final String PREFIXO = "cliente:";

    @Override
    public void salvar(Cliente cliente) {
//        if (cliente.getId() == null) {
//            throw new IllegalArgumentException("ID do cliente não pode ser nulo para operações de cache");
//        }
        ClienteDTO dto = clienteMapper.toDTO(cliente);
        redisTemplate.opsForValue().set(PREFIXO + cliente.getId(), dto, 10, TimeUnit.MINUTES);
    }

    @Override
    public Optional<Cliente> buscarPorId(Long id) {
        ClienteDTO dto = redisTemplate.opsForValue().get(PREFIXO + id);
        return Optional.ofNullable(dto)
                .map(clienteMapper::toEntity);
    }

    @Override
    public void remover(Long id) {
        redisTemplate.delete(PREFIXO + id);
    }
}