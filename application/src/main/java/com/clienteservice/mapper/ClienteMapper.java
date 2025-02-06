package com.clienteservice.mapper;

import com.clienteservice.dto.ClienteDTO;
import com.clienteservice.model.Cliente;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ClienteMapper {
    ClienteMapper INSTANCE = Mappers.getMapper(ClienteMapper.class);

    Cliente toEntity(ClienteDTO dto);

    ClienteDTO toDTO(Cliente cliente);
}