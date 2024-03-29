package com.ufcg.psoft.commerce.service.pagamento;

import com.ufcg.psoft.commerce.util.TipoPagamento;
import org.springframework.stereotype.Service;

@Service
public class PagamentoV1DebitoService implements DescontoService{
    @Override
    public double calcularDesconto(double valorTotal) {
        return valorTotal*TipoPagamento.CARTAO_DEBITO.getDesconto();
    }
}
