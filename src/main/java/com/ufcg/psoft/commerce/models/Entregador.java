package com.ufcg.psoft.commerce.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "entregador")
public class Entregador {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "pk_entregador_id")
    private Long id;

    @Column(name = "usuario")
    private String usuario;

    @Column(name = "codigoAcesso")
    private String codigoAcesso;

    @OneToOne(cascade = CascadeType.ALL)
    private Veiculo veiculo;

    @Column(name = "nome")
    private String nome;


    @OneToOne(cascade = CascadeType.ALL)
    private Estabelecimento estabelecimento;
 
    @Column(name = "disponivel")
    private String disponivel;
}
