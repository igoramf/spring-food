package com.ufcg.psoft.commerce.service.pedido;

import com.ufcg.psoft.commerce.dto.pedido.PedidoResponseDTO;

@FunctionalInterface
public interface PedidoConfirmarEntregaClienteService {

    PedidoResponseDTO confirmarEntrega (Long pedidoId, Long clienteId, String codigoAcessoCliente);

}
