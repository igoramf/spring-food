package com.ufcg.psoft.commerce.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "pizza")
public class Pizza {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "pk_id_pizza", nullable = false)
    private Long id;

    @Column(name = "desc_sabor1", nullable = false)
    @JsonProperty("sabor1")
    private Sabor sabor1;

    @Column(name = "desc_sabor2", nullable = true)
    @JsonProperty("sabor2")
    private Sabor sabor2;

    @Column(name = "desc_tamanho", nullable = false)
    @JsonProperty("tamanho")
    private String tamanho;

    @Column(name = "desc_valorPizza", nullable = false)
    @JsonProperty("valorPizza")
    private double valorPizza;

}
