package com.clienteservice.dto;

import lombok.Data;

@Data
public class EnderecoDTO {
    private Long id;
    private String cep;
    private String rua;
    private String numero;
    private String bairro;
    private String complemento;
    private String cidade;
    private String uf;
}
