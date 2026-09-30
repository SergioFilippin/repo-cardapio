package br.com.cardapio.domain;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.math.BigDecimal;

@Entity
@DiscriminatorValue("PORCAO")
public class Porcao extends Prato {

    protected Porcao() {
    }

    public Porcao(String nome, String descricao, BigDecimal preco, Nacionalidade nacionalidade,
                  boolean disponivel, String acompanhamento) {
        super(nome, descricao, preco, nacionalidade, disponivel);
        setDetalhe(acompanhamento);
    }

    @Override
    public String getDetalhe() {
        return detalhe();
    }

    @Override
    public void setDetalhe(String detalhe) {
        atualizarDetalhe(detalhe);
    }

    @Override
    public TipoPrato getTipo() {
        return TipoPrato.PORCAO;
    }
}
