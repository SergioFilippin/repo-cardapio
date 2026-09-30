package br.com.cardapio.domain;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.math.BigDecimal;

@Entity
@DiscriminatorValue("LASANHA")
public class Lasanha extends Prato {

    protected Lasanha() {
    }

    public Lasanha(String nome, String descricao, BigDecimal preco, Nacionalidade nacionalidade,
                   boolean disponivel, String molho) {
        super(nome, descricao, preco, nacionalidade, disponivel);
        setDetalhe(molho);
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
        return TipoPrato.LASANHA;
    }

}
