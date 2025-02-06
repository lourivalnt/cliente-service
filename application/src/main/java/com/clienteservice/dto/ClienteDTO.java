package com.clienteservice.dto;

import lombok.Data;

@Data
public class ClienteDTO {
    private Long id;
    private String nome;
    private int idade;
    private String cpf;
    private String profissao;
    private EnderecoDTO endereco;
}
