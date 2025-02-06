package com.clienteservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // Gera getters, setters, toString, equals e hashCode
@NoArgsConstructor // Gera um construtor sem argumentos
@AllArgsConstructor // Gera um construtor com todos os argumentos
public class Cliente {
    private Long id;
    private String nome;
    private int idade;
    private String cpf;
    private String profissao;
    private Endereco endereco;
}
