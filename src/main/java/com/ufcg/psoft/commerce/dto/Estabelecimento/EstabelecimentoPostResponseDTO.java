package com.ufcg.psoft.commerce.dto.Estabelecimento;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EstabelecimentoPostResponseDTO {

    @Id
    Long id;

    @JsonProperty("codigoDeAcesso")
    private String codigoAcesso;

    @JsonProperty("usuario")
    private String usuario;
}
