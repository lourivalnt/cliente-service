package com.clienteservice.mapper;

import com.clienteservice.dto.ClienteDTO;
import com.clienteservice.dto.EnderecoDTO;
import com.clienteservice.model.Cliente;
import com.clienteservice.model.Endereco;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-03-07T16:47:07-0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 23.0.2 (Oracle Corporation)"
)
@Component
public class ClienteMapperImpl implements ClienteMapper {

    @Override
    public ClienteDTO toDTO(Cliente cliente) {
        if ( cliente == null ) {
            return null;
        }

        ClienteDTO.ClienteDTOBuilder clienteDTO = ClienteDTO.builder();

        clienteDTO.id( cliente.getId() );
        clienteDTO.nome( cliente.getNome() );
        clienteDTO.idade( cliente.getIdade() );
        clienteDTO.cpf( cliente.getCpf() );
        clienteDTO.profissao( cliente.getProfissao() );
        clienteDTO.endereco( enderecoToEnderecoDTO( cliente.getEndereco() ) );

        return clienteDTO.build();
    }

    @Override
    public Cliente toEntity(ClienteDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Cliente.ClienteBuilder cliente = Cliente.builder();

        cliente.id( dto.getId() );
        cliente.nome( dto.getNome() );
        cliente.idade( dto.getIdade() );
        cliente.cpf( dto.getCpf() );
        cliente.profissao( dto.getProfissao() );
        cliente.endereco( enderecoDTOToEndereco( dto.getEndereco() ) );

        return cliente.build();
    }

    protected EnderecoDTO enderecoToEnderecoDTO(Endereco endereco) {
        if ( endereco == null ) {
            return null;
        }

        EnderecoDTO.EnderecoDTOBuilder enderecoDTO = EnderecoDTO.builder();

        enderecoDTO.id( endereco.getId() );
        enderecoDTO.cep( endereco.getCep() );
        enderecoDTO.rua( endereco.getRua() );
        enderecoDTO.numero( endereco.getNumero() );
        enderecoDTO.bairro( endereco.getBairro() );
        enderecoDTO.complemento( endereco.getComplemento() );
        enderecoDTO.cidade( endereco.getCidade() );
        enderecoDTO.uf( endereco.getUf() );

        return enderecoDTO.build();
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
}
