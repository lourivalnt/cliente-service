package com.clienteservice.mapper;

import com.clienteservice.dto.ClienteDTO;
import com.clienteservice.model.Cliente;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ClienteMapper {
    ClienteMapper INSTANCE = Mappers.getMapper(ClienteMapper.class);

    ClienteDTO toDTO(Cliente cliente); // Converte entidade para DTO
    Cliente toEntity(ClienteDTO dto); // Converte DTO para entidade
}