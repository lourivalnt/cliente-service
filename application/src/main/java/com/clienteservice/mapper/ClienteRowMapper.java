package com.clienteservice.mapper;

import com.clienteservice.dto.ClienteDTO;
import com.clienteservice.dto.EnderecoDTO;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Vantagens da Refatoração
 *
 * <h2>Legibilidade</h2>
 * O código é mais conciso e fácil de entender.
 * A construção dos objetos é feita de forma fluente.
 *
 * <h2>Manutenção</h2>
 * Adicionar/remover campos nos DTOs é mais simples com o construtor.
 * Reduza o risco de erros ao definir campos manualmente.
 *
 * <h2>Padrão de Projeto</h2>
 * Segue o Builder Pattern , que é recomendado para objetos complexos.
 */
@Component
public class ClienteRowMapper implements RowMapper<ClienteDTO> {

    @Override
    public ClienteDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        // Cria EnderecoDTO usando Builder
        EnderecoDTO enderecoDTO = EnderecoDTO.builder()
                .id(rs.getLong("endereco_id"))
                .cep(rs.getString("endereco_cep"))
                .rua(rs.getString("endereco_rua"))
                .numero(rs.getString("endereco_numero"))
                .bairro(rs.getString("endereco_bairro"))
                .complemento(rs.getString("endereco_complemento"))
                .cidade(rs.getString("endereco_cidade"))
                .uf(rs.getString("endereco_uf"))
                .build();

        // Cria ClienteDTO usando Builder
        return ClienteDTO.builder()
                .id(rs.getLong("cliente_id"))
                .nome(rs.getString("cliente_nome"))
                .idade(rs.getInt("cliente_idade"))
                .cpf(rs.getString("cliente_cpf"))
                .profissao(rs.getString("cliente_profissao"))
                .endereco(enderecoDTO)
                .build();
    }
}