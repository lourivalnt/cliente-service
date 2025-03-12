package com.clienteservice.repository;

import com.clienteservice.dto.ClienteDTO;
import com.clienteservice.mapper.ClienteMapper;
import com.clienteservice.model.Cliente;
import com.clienteservice.pagination.PageResult;
import com.clienteservice.ports.ClienteRepositoryPort;
import com.clienteservice.mapper.ClienteRowMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    private final ClienteMapper clienteMapper;

    // Método original (sem paginação)
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
        List<ClienteDTO> dtos = jdbcTemplate.query(sql, clienteRowMapper);
        return dtos.stream()
                .map(clienteMapper::toEntity)
                .collect(Collectors.toList());
    }

    // Método com paginação
    @Override
    public PageResult<Cliente> listarClientesComPaginacao(
            int page,
            int pageSize,
            String sortBy,
            boolean ascending) {

        // Calcula offset
        int offset = page * pageSize;

        // Query com paginação
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
            ORDER BY 
                %s %s
            LIMIT 
                %d
            OFFSET 
                %d
            """.formatted(
                sortBy,
                ascending ? "ASC" : "DESC",
                pageSize,
                offset
        );

        // Busca dados paginados
        List<ClienteDTO> dtosPaginados = jdbcTemplate.query(sql, clienteRowMapper);
        List<Cliente> clientes = dtosPaginados.stream()
                .map(clienteMapper::toEntity)
                .toList();

        // Contagem total de elementos
        Integer totalElements = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM cliente", Integer.class);
        int totalPages = (int) Math.ceil((double) totalElements / pageSize);

        // Retorna PageResult
        return PageResult.<Cliente>builder()
                .content(clientes)
                .totalPages(totalPages)
                .totalElements(totalElements)
                .currentPage(page)
                .pageSize(pageSize)
                .build();
    }


    // Buscar por ID (com cache)
    @Override
    @Cacheable(value = "clientes", key = "'cliente:' + #id")
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
                ClienteDTO dto = clienteRowMapper.mapRow(rs, 1);
                return Optional.of(clienteMapper.toEntity(dto));
            }
            return Optional.empty();
        });
    }

    @Override
    @CachePut(value = "clientes", key = "'cliente:' + #cliente.id") // Atualiza cache após salvar/atualizar
    public Cliente salvar(Cliente cliente) {
        ClienteDTO dto = clienteMapper.toDTO(cliente);

        // Verifica se o cliente já existe (ID não nulo)
        if (dto.getId() == null || !existePorId(dto.getId())) {
            return inserirNovoCliente(dto); // Insere novo cliente
        } else {
            return atualizarClienteExistente(dto); // Atualiza cliente existente
        }
    }


    private Cliente inserirNovoCliente(ClienteDTO dto) {
        // Insere o endereço primeiro
        String sqlEndereco = """
            INSERT INTO endereco (cep, rua, numero, bairro, complemento, cidade, uf)
            VALUES (?, ?, ?, ?, ?, ?, ?)
            """;
        jdbcTemplate.update(sqlEndereco,
                dto.getEndereco().getCep(),
                dto.getEndereco().getRua(),
                dto.getEndereco().getNumero(),
                dto.getEndereco().getBairro(),
                dto.getEndereco().getComplemento(),
                dto.getEndereco().getCidade(),
                dto.getEndereco().getUf());

        // Recupera o ID do endereço recém-inserido
        Long enderecoId = jdbcTemplate.queryForObject("SELECT LASTVAL()", Long.class);

        // Insere o cliente com o ID do endereço
        String sqlCliente = """
            INSERT INTO cliente (nome, idade, cpf, profissao, endereco_id)
            VALUES (?, ?, ?, ?, ?)
            """;
        jdbcTemplate.update(sqlCliente,
                dto.getNome(),
                dto.getIdade(),
                dto.getCpf(),
                dto.getProfissao(),
                enderecoId);

        // Recupera o ID do cliente recém-inserido
        Long clienteId = jdbcTemplate.queryForObject("SELECT LASTVAL()", Long.class);

        // Define os IDs no cliente antes de retornar
        dto.setId(clienteId);
        dto.getEndereco().setId(enderecoId);

        return clienteMapper.toEntity(dto);
    }

    private Cliente atualizarClienteExistente(ClienteDTO dto) {
        // Recupera o ID do endereço associado ao cliente
        Long enderecoId = jdbcTemplate.queryForObject(
                "SELECT endereco_id FROM cliente WHERE id = ?", Long.class, dto.getId());

        // Atualiza o endereço (se existir)
        if (enderecoId != null) {
            String sqlEndereco = """
                UPDATE endereco
                SET cep = ?, rua = ?, numero = ?, bairro = ?, complemento = ?, cidade = ?, uf = ?
                WHERE id = ?
                """;
            jdbcTemplate.update(sqlEndereco,
                    dto.getEndereco().getCep(),
                    dto.getEndereco().getRua(),
                    dto.getEndereco().getNumero(),
                    dto.getEndereco().getBairro(),
                    dto.getEndereco().getComplemento(),
                    dto.getEndereco().getCidade(),
                    dto.getEndereco().getUf(),
                    enderecoId);

            // Atribui o ID do endereço ao DTO
            dto.getEndereco().setId(enderecoId);
        }

        // Atualiza o cliente
        String sqlCliente = """
            UPDATE cliente
            SET nome = ?, idade = ?, cpf = ?, profissao = ?, endereco_id = ?
            WHERE id = ?
            """;
        jdbcTemplate.update(sqlCliente,
                dto.getNome(),
                dto.getIdade(),
                dto.getCpf(),
                dto.getProfissao(),
                dto.getEndereco().getId(),
                dto.getId());

        return clienteMapper.toEntity(dto);
    }

    @Override
    @CacheEvict(value = "clientes", key = "'cliente:' + #id") // Remove a chave cliente:1
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

}