package com.clienteservice.mapper;

import com.clienteservice.dto.ClienteDTO;
import com.clienteservice.dto.EnderecoDTO;
import com.clienteservice.model.Cliente;
import com.clienteservice.model.Endereco;
import org.springframework.stereotype.Component;

@Component
public class ClienteMapper {

    public ClienteDTO toDTO(Cliente cliente) {
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

    public Cliente toEntity(ClienteDTO dto) {
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
