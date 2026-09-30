package br.com.cardapio.domain;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.math.BigDecimal;

@Entity
@DiscriminatorValue("RISOTO")
public class Risoto extends Prato {

    protected Risoto() {
    }

    public Risoto(String nome, String descricao, BigDecimal preco, Nacionalidade nacionalidade,
                  boolean disponivel, String ingredientePrincipal) {
        super(nome, descricao, preco, nacionalidade, disponivel);
        setDetalhe(ingredientePrincipal);
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
        return TipoPrato.RISOTO;
    }
}
