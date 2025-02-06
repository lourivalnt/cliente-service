package com.clienteservice.mapper;

import com.clienteservice.dto.ClienteDTO;
import com.clienteservice.dto.EnderecoDTO;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class ClienteRowMapper implements RowMapper<ClienteDTO> {
    @Override
    public ClienteDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        EnderecoDTO enderecoDTO = new EnderecoDTO();
        enderecoDTO.setId(rs.getLong("endereco_id"));
        enderecoDTO.setCep(rs.getString("endereco_cep"));
        enderecoDTO.setRua(rs.getString("endereco_rua"));
        enderecoDTO.setNumero(rs.getString("endereco_numero"));
        enderecoDTO.setBairro(rs.getString("endereco_bairro"));
        enderecoDTO.setComplemento(rs.getString("endereco_complemento"));
        enderecoDTO.setCidade(rs.getString("endereco_cidade"));
        enderecoDTO.setUf(rs.getString("endereco_uf"));

        ClienteDTO clienteDTO = new ClienteDTO();
        clienteDTO.setId(rs.getLong("cliente_id"));
        clienteDTO.setNome(rs.getString("cliente_nome"));
        clienteDTO.setIdade(rs.getInt("cliente_idade"));
        clienteDTO.setCpf(rs.getString("cliente_cpf"));
        clienteDTO.setProfissao(rs.getString("cliente_profissao"));
        clienteDTO.setEndereco(enderecoDTO);

        return clienteDTO;
    }
}
