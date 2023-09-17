package com.ufcg.psoft.commerce.exception.entregador;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ufcg.psoft.commerce.exception.CommerceException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EntregadorInvalido extends CommerceException{
    @JsonProperty("timestamp")
    private LocalDateTime timestamp;

    @JsonProperty("message")
    private String message;

    @JsonProperty("errors")
    private List<String> errors;

    public EntregadorInvalido(CommerceException e) {
        this.timestamp = LocalDateTime.now();
        this.message = e.getMessage();
        this.errors = new ArrayList<>();
    }

}