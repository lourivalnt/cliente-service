package com.clienteservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data // Gera getters, setters, toString, equals e hashCode
@NoArgsConstructor // Gera um construtor sem argumentos
@AllArgsConstructor // Gera um construtor com todos os argumentos
@Builder
public class Cliente implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String nome;
    private int idade;
    private String cpf;
    private String profissao;
    private Endereco endereco;
}
