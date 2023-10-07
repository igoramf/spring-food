package com.ufcg.psoft.commerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ufcg.psoft.commerce.dto.pedido.PedidoPostPutRequestDTO;
import com.ufcg.psoft.commerce.service.pedido.PedidoCriarService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/v1/pedidos", produces = MediaType.APPLICATION_JSON_VALUE)
public class PedidoV1RestController {

    @Autowired
    private PedidoCriarService pedidoCriarService;

    @PostMapping
    public ResponseEntity<?> criarPedido(
//            @RequestParam @Valid  Long clienteId,
            @RequestParam @Valid String clienteCodigoAcesso,
//            @RequestParam @Valid Long estabelecimentoId,
            @RequestBody @Valid PedidoPostPutRequestDTO pedidoPostPutRequestDTO){

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(pedidoCriarService.criar(clienteCodigoAcesso, pedidoPostPutRequestDTO));
    }

}