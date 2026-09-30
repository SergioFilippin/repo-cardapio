package br.com.cardapio.dto;

import br.com.cardapio.domain.Nacionalidade;

public record NacionalidadeResponse(Long id, String nome) {
    public static NacionalidadeResponse from(Nacionalidade nacionalidade) {
        return new NacionalidadeResponse(nacionalidade.getId(), nacionalidade.getNome());
    }
}
