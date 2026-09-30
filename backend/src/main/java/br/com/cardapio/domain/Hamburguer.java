package br.com.cardapio.domain;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.math.BigDecimal;

@Entity
@DiscriminatorValue("HAMBURGUER")
public class Hamburguer extends Prato {

    protected Hamburguer() {
    }

    public Hamburguer(String nome, String descricao, BigDecimal preco, Nacionalidade nacionalidade,
                      boolean disponivel, String ingredientes) {
        super(nome, descricao, preco, nacionalidade, disponivel);
        setDetalhe(ingredientes);
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
        return TipoPrato.HAMBURGUER;
    }
}
