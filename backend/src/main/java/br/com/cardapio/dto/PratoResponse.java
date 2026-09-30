package br.com.cardapio.dto;

import br.com.cardapio.domain.Prato;
import br.com.cardapio.domain.TipoPrato;

import java.math.BigDecimal;

public record PratoResponse(
        Long id,
        TipoPrato tipo,
        String nome,
        String descricao,
        BigDecimal preco,
        String nacionalidade,
        String detalhe,
        boolean disponivel
) {
    public static PratoResponse from(Prato prato) {
        return new PratoResponse(
                prato.getId(),
                prato.getTipo(),
                prato.getNome(),
                prato.getDescricao(),
                prato.getPreco(),
                prato.getNacionalidade().getNome(),
                prato.getDetalhe(),
                prato.isDisponivel()
        );
    }
}
