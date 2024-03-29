package com.ufcg.psoft.commerce.controller;


import com.ufcg.psoft.commerce.dto.Estabelecimento.EstabelecimentoPatchCodigoDTO;
import com.ufcg.psoft.commerce.dto.Estabelecimento.EstabelecimentoPostPutRequestDTO;
import com.ufcg.psoft.commerce.dto.sabor.SaborPatchDisponibilidadeDTO;
import com.ufcg.psoft.commerce.service.EstabelecimentoServices.*;
import com.ufcg.psoft.commerce.utils.EmailSender;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(
        value = "/v1/estabelecimentos",
        produces = MediaType.APPLICATION_JSON_VALUE
)
public class EstabelecimentoV1Controller {


    @Autowired
    private EstabelecimentoCriarService estabelecimentoCriarService;

    @Autowired
    private EstabelecimentoPutService estabelecimentoPutService;

    @Autowired
    EstabelecimentoDeleteService estabelecimentoDeleteService;

    @Autowired
    EstabelecimentoBuscarService estabelecimentoBuscarService;

    @Autowired
    EstabelecimentoBuscarCardapioService estabelecimentoBuscarCardapioService;

    @Autowired
    EstabelecimentoPatchCodigoService estabelecimentoPatchCodigoService;

    @Autowired
    EstabelecimentoPatchDisponibilidadeSaborService estabelecimentoPatchDisponibilidadeSaborService;

    @GetMapping
    ResponseEntity getAll(){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(estabelecimentoBuscarService.getAll());
    }

    @GetMapping("{id}")
    ResponseEntity getOne(
            @PathVariable("id") Long id
    ){
      return ResponseEntity
              .status(HttpStatus.OK)
              .body(estabelecimentoBuscarService.getOne(id));
    }

    @GetMapping("{id}/sabores")
    ResponseEntity getCardapio(
            @PathVariable("id") Long id
    ){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(estabelecimentoBuscarCardapioService.getCardapio(id));
    }

    @GetMapping("{id}/sabores/tipo")
    ResponseEntity getCardapioPorTipo(
            @PathVariable("id") Long id,
            @Param("tipo") String tipo
    ){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(estabelecimentoBuscarCardapioService.getCardapioPorTipo(id, tipo));
    }

    @GetMapping("{id}/sabores/disponibilidade")
    ResponseEntity getCardapioPorDisponibilidade(
            @PathVariable("id") Long id,
            @Param("disponivel") Boolean disponivel
    ){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(estabelecimentoBuscarCardapioService.getCardapioPorDisponibilidade(id, disponivel));
    }

    @PatchMapping("{idEstabelecimento}/sabores/disponibilidade")
    public ResponseEntity<?> atualizarDisponibilidade(
            @PathVariable("idEstabelecimento") Long idEstabelecimento,
            @Param("idSabor") Long idSabor,
            @Param("codigoDeAcesso") String codigoDeAcesso,
            @RequestBody @Valid SaborPatchDisponibilidadeDTO saborPatchDisponibilidadeDTO
    ){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(estabelecimentoPatchDisponibilidadeSaborService.alterarDisponibilidade(idEstabelecimento, idSabor, codigoDeAcesso, saborPatchDisponibilidadeDTO));
    }

    @PostMapping
    ResponseEntity criar(
            @RequestBody @Valid EstabelecimentoPostPutRequestDTO estabelecimentoPostPutRequestDTO
            ){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(estabelecimentoCriarService.criar(estabelecimentoPostPutRequestDTO));
    }

    @PutMapping("{id}")
    ResponseEntity atualizar(
            @Param("codigo") String codigo,
            @RequestBody @Valid EstabelecimentoPostPutRequestDTO estabelecimentoPostPutRequestDTO,
            @PathVariable("id") Long id
    ){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(estabelecimentoPutService.update(id, codigo, estabelecimentoPostPutRequestDTO));
    }

    @PatchMapping("{id}")
    ResponseEntity atualizarCodigoDeAcesso(
            @Param("codigo") String codigo,
            @PathVariable("id") Long id,
            @RequestBody @Valid EstabelecimentoPatchCodigoDTO estabelecimentoPatchCodigoDTO
            ){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(estabelecimentoPatchCodigoService.atualizarEmail(id, codigo, estabelecimentoPatchCodigoDTO));
    }


    @DeleteMapping("{id}")
    ResponseEntity delete(
            @Param("codigo")String codigo,
            @PathVariable("id") Long id
    ){
        estabelecimentoDeleteService.delete(id, codigo);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

}
