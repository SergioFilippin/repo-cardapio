package br.com.cardapio.domain;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "pratos")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo", discriminatorType = DiscriminatorType.STRING, length = 20)
public abstract class Prato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String nome;

    @Column(nullable = false, length = 500)
    private String descricao;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal preco;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "nacionalidade_id", nullable = false)
    private Nacionalidade nacionalidade;

    @Column(nullable = false)
    private boolean disponivel;

    @Column(nullable = false, length = 500)
    private String detalhe;

    protected Prato() {
    }

    protected Prato(String nome, String descricao, BigDecimal preco,
                    Nacionalidade nacionalidade, boolean disponivel) {
        atualizarDados(nome, descricao, preco, nacionalidade, disponivel);
    }

    public void atualizarDados(String nome, String descricao, BigDecimal preco,
                               Nacionalidade nacionalidade, boolean disponivel) {
        setNome(nome);
        setDescricao(descricao);
        setPreco(preco);
        setNacionalidade(nacionalidade);
        this.disponivel = disponivel;
    }

    public abstract String getDetalhe();

    public abstract void setDetalhe(String detalhe);

    public abstract TipoPrato getTipo();

    protected final String detalhe() {
        return detalhe;
    }

    protected final void atualizarDetalhe(String detalhe) {
        if (detalhe == null || detalhe.isBlank()) {
            throw new IllegalArgumentException("O detalhe do prato não pode ficar vazio.");
        }
        this.detalhe = detalhe.trim();
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O nome do prato não pode ficar vazio.");
        }
        this.nome = nome.trim();
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        if (descricao == null || descricao.isBlank()) {
            throw new IllegalArgumentException("A descrição não pode ficar vazia.");
        }
        this.descricao = descricao.trim();
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        if (preco == null || preco.signum() < 0) {
            throw new IllegalArgumentException("O preço não pode ser negativo ou nulo.");
        }
        this.preco = preco;
    }

    public Nacionalidade getNacionalidade() {
        return nacionalidade;
    }

    public void setNacionalidade(Nacionalidade nacionalidade) {
        this.nacionalidade = Objects.requireNonNull(nacionalidade,
                "A nacionalidade do prato não pode ser nula.");
    }

    public boolean isDisponivel() {
        return disponivel;
    }
}
