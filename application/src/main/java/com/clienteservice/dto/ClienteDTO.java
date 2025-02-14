package com.clienteservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class ClienteDTO {
    private Long id;
    @NotBlank(message = "O nome é obrigatório")
    @Schema(description = "Nome completo do cliente", example = "João Silva", requiredMode = Schema.RequiredMode.REQUIRED)
    private String nome;

    @Min(value = 1, message = "A idade deve ser maior que zero")
    @Schema(description = "Idade do cliente", example = "30", requiredMode = Schema.RequiredMode.REQUIRED)
    private int idade;

    @Pattern(regexp = "\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}", message = "CPF inválido")
    @Schema(description = "CPF do cliente", example = "123.456.789-00", requiredMode = Schema.RequiredMode.REQUIRED)
    private String cpf;

    @Schema(description = "Profissão do cliente", example = "Engenheiro")
    private String profissao;

    @Schema(description = "ID do endereço associado ao cliente", example = "1")
    private EnderecoDTO endereco;
}
