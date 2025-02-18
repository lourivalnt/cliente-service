package com.clienteservice.mapper;

import com.clienteservice.dto.ClienteDTO;
import com.clienteservice.dto.EnderecoDTO;
import com.clienteservice.model.Cliente;
import com.clienteservice.model.Endereco;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-02-18T10:57:05-0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 23.0.2 (Oracle Corporation)"
)
@Component
public class ClienteMapperImpl implements ClienteMapper {

    @Override
    public Cliente toEntity(ClienteDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Cliente cliente = new Cliente();

        cliente.setId( dto.getId() );
        cliente.setNome( dto.getNome() );
        cliente.setIdade( dto.getIdade() );
        cliente.setCpf( dto.getCpf() );
        cliente.setProfissao( dto.getProfissao() );
        cliente.setEndereco( enderecoDTOToEndereco( dto.getEndereco() ) );

        return cliente;
    }

    @Override
    public ClienteDTO toDTO(Cliente cliente) {
        if ( cliente == null ) {
            return null;
        }

        ClienteDTO clienteDTO = new ClienteDTO();

        clienteDTO.setId( cliente.getId() );
        clienteDTO.setNome( cliente.getNome() );
        clienteDTO.setIdade( cliente.getIdade() );
        clienteDTO.setCpf( cliente.getCpf() );
        clienteDTO.setProfissao( cliente.getProfissao() );
        clienteDTO.setEndereco( enderecoToEnderecoDTO( cliente.getEndereco() ) );

        return clienteDTO;
    }

    protected Endereco enderecoDTOToEndereco(EnderecoDTO enderecoDTO) {
        if ( enderecoDTO == null ) {
            return null;
        }

        Endereco endereco = new Endereco();

        endereco.setId( enderecoDTO.getId() );
        endereco.setCep( enderecoDTO.getCep() );
        endereco.setRua( enderecoDTO.getRua() );
        endereco.setNumero( enderecoDTO.getNumero() );
        endereco.setBairro( enderecoDTO.getBairro() );
        endereco.setComplemento( enderecoDTO.getComplemento() );
        endereco.setCidade( enderecoDTO.getCidade() );
        endereco.setUf( enderecoDTO.getUf() );

        return endereco;
    }

    protected EnderecoDTO enderecoToEnderecoDTO(Endereco endereco) {
        if ( endereco == null ) {
            return null;
        }

        EnderecoDTO enderecoDTO = new EnderecoDTO();

        enderecoDTO.setId( endereco.getId() );
        enderecoDTO.setCep( endereco.getCep() );
        enderecoDTO.setRua( endereco.getRua() );
        enderecoDTO.setNumero( endereco.getNumero() );
        enderecoDTO.setBairro( endereco.getBairro() );
        enderecoDTO.setComplemento( endereco.getComplemento() );
        enderecoDTO.setCidade( endereco.getCidade() );
        enderecoDTO.setUf( endereco.getUf() );

        return enderecoDTO;
    }
}
