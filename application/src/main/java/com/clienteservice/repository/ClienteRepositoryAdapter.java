package com.clienteservice.repository;

import com.clienteservice.dto.ClienteDTO;
import com.clienteservice.dto.EnderecoDTO;
import com.clienteservice.model.Cliente;
import com.clienteservice.model.Endereco;
import com.clienteservice.ports.ClienteRepositoryPort;
import com.clienteservice.mapper.ClienteRowMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ClienteRepositoryAdapter implements ClienteRepositoryPort {
    private final JdbcTemplate jdbcTemplate;
    private final ClienteRowMapper clienteRowMapper;

    @Override
    public List<Cliente> buscarTodos() {
        String sql = """
            SELECT 
                c.id AS cliente_id, 
                c.nome AS cliente_nome, 
                c.idade AS cliente_idade, 
                c.cpf AS cliente_cpf, 
                c.profissao AS cliente_profissao, 
                e.id AS endereco_id, 
                e.cep AS endereco_cep, 
                e.rua AS endereco_rua, 
                e.numero AS endereco_numero, 
                e.bairro AS endereco_bairro, 
                e.complemento AS endereco_complemento, 
                e.cidade AS endereco_cidade, 
                e.uf AS endereco_uf 
            FROM 
                cliente c 
            JOIN 
                endereco e 
            ON 
                c.endereco_id = e.id
            """;
        List<ClienteDTO> clienteDTOs = jdbcTemplate.query(sql, clienteRowMapper);
        return clienteDTOs.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }


    @Override
    @Cacheable(value = "clientes", key = "#id") // Busca no cache antes de acessar o banco
    public Optional<Cliente> buscarPorId(Long id) {
        String sql = """
            SELECT 
                c.id AS cliente_id, 
                c.nome AS cliente_nome, 
                c.idade AS cliente_idade, 
                c.cpf AS cliente_cpf, 
                c.profissao AS cliente_profissao, 
                e.id AS endereco_id, 
                e.cep AS endereco_cep, 
                e.rua AS endereco_rua, 
                e.numero AS endereco_numero, 
                e.bairro AS endereco_bairro, 
                e.complemento AS endereco_complemento, 
                e.cidade AS endereco_cidade, 
                e.uf AS endereco_uf 
            FROM 
                cliente c 
            JOIN 
                endereco e 
            ON 
                c.endereco_id = e.id 
            WHERE 
                c.id = ?
            """;
        return jdbcTemplate.query(sql, new Object[]{id}, rs -> {
            if (rs.next()) {
                return Optional.of(toEntity(clienteRowMapper.mapRow(rs, 1)));
            }
            return Optional.empty();
        });
    }

    @Override
    @CachePut(value = "clientes", key = "#cliente.id") // Atualiza o cache após salvar
    public Cliente salvar(Cliente cliente) {
        ClienteDTO dto = toDTO(cliente);
        if (dto.getId() == null) {
            // Inserir novo cliente
            String sqlEndereco = "INSERT INTO endereco (cep, rua, numero, bairro, complemento, cidade, uf) VALUES (?, ?, ?, ?, ?, ?, ?)";
            jdbcTemplate.update(sqlEndereco,
                    dto.getEndereco().getCep(),
                    dto.getEndereco().getRua(),
                    dto.getEndereco().getNumero(),
                    dto.getEndereco().getBairro(),
                    dto.getEndereco().getComplemento(),
                    dto.getEndereco().getCidade(),
                    dto.getEndereco().getUf());

            // Recuperar o ID do endereço inserido
            Long enderecoId = jdbcTemplate.queryForObject("SELECT LASTVAL()", Long.class);

            String sqlCliente = "INSERT INTO cliente (nome, idade, cpf, profissao, endereco_id) VALUES (?, ?, ?, ?, ?)";
            jdbcTemplate.update(sqlCliente,
                    dto.getNome(),
                    dto.getIdade(),
                    dto.getCpf(),
                    dto.getProfissao(),
                    enderecoId);

            // Recuperar o ID do cliente inserido
            Long clienteId = jdbcTemplate.queryForObject("SELECT LASTVAL()", Long.class);
            cliente.setId(clienteId);
        } else {
            // Atualizar cliente existente
            String sqlEndereco = "UPDATE endereco SET cep = ?, rua = ?, numero = ?, bairro = ?, complemento = ?, cidade = ?, uf = ? WHERE id = ?";
            jdbcTemplate.update(sqlEndereco,
                    dto.getEndereco().getCep(),
                    dto.getEndereco().getRua(),
                    dto.getEndereco().getNumero(),
                    dto.getEndereco().getBairro(),
                    dto.getEndereco().getComplemento(),
                    dto.getEndereco().getCidade(),
                    dto.getEndereco().getUf(),
                    dto.getEndereco().getId());

            String sqlCliente = "UPDATE cliente SET nome = ?, idade = ?, cpf = ?, profissao = ?, endereco_id = ? WHERE id = ?";
            jdbcTemplate.update(sqlCliente,
                    dto.getNome(),
                    dto.getIdade(),
                    dto.getCpf(),
                    dto.getProfissao(),
                    dto.getEndereco().getId(),
                    dto.getId());
        }
        return cliente;
    }

    @Override
    @CacheEvict(value = "clientes", key = "#id") // Remove do cache após excluir
    public void excluir(Long id) {
        String sql = "DELETE FROM cliente WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public boolean existePorId(Long id) {
        String sql = "SELECT COUNT(*) FROM cliente WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }

    private ClienteDTO toDTO(Cliente cliente) {
        ClienteDTO dto = new ClienteDTO();
        dto.setId(cliente.getId());
        dto.setNome(cliente.getNome());
        dto.setIdade(cliente.getIdade());
        dto.setCpf(cliente.getCpf());
        dto.setProfissao(cliente.getProfissao());

        EnderecoDTO enderecoDTO = new EnderecoDTO();
        enderecoDTO.setId(cliente.getEndereco().getId());
        enderecoDTO.setCep(cliente.getEndereco().getCep());
        enderecoDTO.setRua(cliente.getEndereco().getRua());
        enderecoDTO.setNumero(cliente.getEndereco().getNumero());
        enderecoDTO.setBairro(cliente.getEndereco().getBairro());
        enderecoDTO.setComplemento(cliente.getEndereco().getComplemento());
        enderecoDTO.setCidade(cliente.getEndereco().getCidade());
        enderecoDTO.setUf(cliente.getEndereco().getUf());

        dto.setEndereco(enderecoDTO);
        return dto;
    }

    private Cliente toEntity(ClienteDTO dto) {
        Cliente cliente = new Cliente();
        cliente.setId(dto.getId());
        cliente.setNome(dto.getNome());
        cliente.setIdade(dto.getIdade());
        cliente.setCpf(dto.getCpf());
        cliente.setProfissao(dto.getProfissao());

        Endereco endereco = new Endereco();
        endereco.setId(dto.getEndereco().getId());
        endereco.setCep(dto.getEndereco().getCep());
        endereco.setRua(dto.getEndereco().getRua());
        endereco.setNumero(dto.getEndereco().getNumero());
        endereco.setBairro(dto.getEndereco().getBairro());
        endereco.setComplemento(dto.getEndereco().getComplemento());
        endereco.setCidade(dto.getEndereco().getCidade());
        endereco.setUf(dto.getEndereco().getUf());

        cliente.setEndereco(endereco);
        return cliente;
    }
}