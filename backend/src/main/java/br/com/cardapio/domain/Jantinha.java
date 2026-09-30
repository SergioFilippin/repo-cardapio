package br.com.cardapio.domain;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.math.BigDecimal;

@Entity
@DiscriminatorValue("JANTINHA")
public class Jantinha extends Prato {

    protected Jantinha() {
    }

    public Jantinha(String nome, String descricao, BigDecimal preco, Nacionalidade nacionalidade,
                    boolean disponivel, String acompanhamentos) {
        super(nome, descricao, preco, nacionalidade, disponivel);
        setDetalhe(acompanhamentos);
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
        return TipoPrato.JANTINHA;
    }
}
