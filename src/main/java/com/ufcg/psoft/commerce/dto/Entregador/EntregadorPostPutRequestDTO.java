package com.ufcg.psoft.commerce.dto.Entregador;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ufcg.psoft.commerce.models.Veiculo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class EntregadorPostPutRequestDTO {
    @JsonProperty("nome")
    private String nome;

    @JsonProperty("veiculo")
    private Veiculo veiculo;

    @JsonProperty("codigoAcesso")
    private String codigoAcesso;
}
