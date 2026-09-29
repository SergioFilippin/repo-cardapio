import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class CardapioApp {

    public static void main(String[] args) {
        Cardapio cardapio = new Cardapio("Cardápio do Restaurante");

        Nacionalidade italiana = new Nacionalidade("Italiana");
        Nacionalidade brasileira = new Nacionalidade("Brasileira");

        Prato lasanhaBranca = new Lasanha(
                "Lasanha ao molho branco com brócolis e bacon",
                42.90,
                "Lasanha cremosa preparada com brócolis e bacon.",
                italiana,
                "Molho branco"
        );

        Prato lasanhaTradicional = new Lasanha(
                "Lasanha tradicional",
                39.90,
                "Lasanha com carne moída, queijo e molho de tomate.",
                italiana,
                "Molho à bolonhesa"
        );

        Prato risotoCamarao = new Risoto(
                "Risoto de camarão",
                49.90,
                "Arroz cremoso preparado com temperos frescos.",
                italiana,
                "Camarão"
        );

        Prato jantinha = new Jantinha(
                "Jantinha brasileira",
                29.90,
                "Refeição típica servida em um prato completo.",
                brasileira,
                "Arroz, feijão tropeiro, vinagrete e espetinho"
        );

        Prato hamburguerTradicional = new Hamburguer(
                "Hambúrguer caseiro",
                24.90,
                "Hambúrguer artesanal servido no pão.",
                brasileira,
                TipoHamburguer.TRADICIONAL,
                "Carne, queijo, alface e tomate"
        );

        Prato hamburguerBacon = new Hamburguer(
                "Hambúrguer caseiro",
                27.90,
                "Hambúrguer artesanal servido no pão.",
                brasileira,
                TipoHamburguer.BACON,
                "Carne, queijo, bacon, alface e tomate"
        );

        Prato hamburguerEspecial = new Hamburguer(
                "Hambúrguer caseiro",
                31.90,
                "Hambúrguer artesanal servido no pão.",
                brasileira,
                TipoHamburguer.ESPECIAL,
                "Duas carnes, queijo, bacon, ovo e salada"
        );

        italiana.adicionarPrato(lasanhaBranca);
        italiana.adicionarPrato(lasanhaTradicional);
        italiana.adicionarPrato(risotoCamarao);

        brasileira.adicionarPrato(jantinha);
        brasileira.adicionarPrato(hamburguerTradicional);
        brasileira.adicionarPrato(hamburguerBacon);
        brasileira.adicionarPrato(hamburguerEspecial);

        cardapio.adicionarNacionalidade(italiana);
        cardapio.adicionarNacionalidade(brasileira);

        cardapio.exibirCardapio();
    }
}

class Cardapio {

    // ENCAPSULAMENTO: os atributos são privados e não podem ser alterados diretamente.
    private String nome;
    private final List<Nacionalidade> nacionalidades;

    public Cardapio(String nome) {
        setNome(nome);
        this.nacionalidades = new ArrayList<>();
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O nome do cardápio não pode ficar vazio.");
        }
        this.nome = nome;
    }

    // ENCAPSULAMENTO: List.copyOf impede que a lista interna seja modificada por fora.
    public List<Nacionalidade> getNacionalidades() {
        return List.copyOf(nacionalidades);
    }

    public void adicionarNacionalidade(Nacionalidade nacionalidade) {
        Objects.requireNonNull(nacionalidade, "A nacionalidade nao pode ser nula.");

        if (!nacionalidades.contains(nacionalidade)) {
            nacionalidades.add(nacionalidade);
        }
    }

    public void exibirCardapio() {
        System.out.println("===== CARDAPIO =====");

        for (Nacionalidade nacionalidade : nacionalidades) {
            System.out.println("\n" + nacionalidade.getNome().toUpperCase(Locale.ROOT));

            for (Prato prato : nacionalidade.getPratos()) {
                // POLIMORFISMO: a mesma chamada executa a versão da classe real do prato.
                System.out.println(prato.apresentarPrato());
            }
        }
    }
}

class Nacionalidade {

    // ENCAPSULAMENTO: nome e pratos só são acessados por métodos públicos controlados.
    private String nome;
    private final List<Prato> pratos;

    public Nacionalidade(String nome) {
        setNome(nome);
        this.pratos = new ArrayList<>();
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O nome da nacionalidade não pode ficar vazio.");
        }
        this.nome = nome;
    }

    public List<Prato> getPratos() {
        return List.copyOf(pratos);
    }

    public void adicionarPrato(Prato prato) {
        Objects.requireNonNull(prato, "O prato não pode ser nulo.");

        if (prato.getNacionalidade() != this) {
            throw new IllegalArgumentException("O prato pertence a outra nacionalidade.");
        }

        if (!pratos.contains(prato)) {
            pratos.add(prato);
        }
    }
}

// ABSTRAÇÃO: reúne os dados e comportamentos essenciais de qualquer prato.
// A classe é abstrata porque um "prato genérico" não será criado diretamente.
abstract class Prato {

    // ENCAPSULAMENTO: os atributos privados são protegidos por getters e setters.
    private String nome;
    private double preco;
    private String descricao;
    private final Nacionalidade nacionalidade;

    protected Prato(String nome, double preco, String descricao, Nacionalidade nacionalidade) {
        setNome(nome);
        setPreco(preco);
        setDescricao(descricao);
        this.nacionalidade = Objects.requireNonNull(
                nacionalidade,
                "A nacionalidade do prato não pode ser nula."
        );
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O nome do prato não pode ficar vazio.");
        }
        this.nome = nome;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        if (preco < 0) {
            throw new IllegalArgumentException("O preço não pode ser negativo.");
        }
        this.preco = preco;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        if (descricao == null || descricao.isBlank()) {
            throw new IllegalArgumentException("A descrição não pode ficar vazia.");
        }
        this.descricao = descricao;
    }

    public Nacionalidade getNacionalidade() {
        return nacionalidade;
    }

    // ABSTRAÇÃO: define o comportamento obrigatório sem impor uma apresentação única.
    public abstract String apresentarPrato();
}

// HERANÇA: Lasanha herda os atributos e comportamentos comuns da classe Prato.
class Lasanha extends Prato {

    private String molho;

    public Lasanha(String nome, double preco, String descricao,
                   Nacionalidade nacionalidade, String molho) {
        super(nome, preco, descricao, nacionalidade);
        setMolho(molho);
    }

    public String getMolho() {
        return molho;
    }

    public void setMolho(String molho) {
        if (molho == null || molho.isBlank()) {
            throw new IllegalArgumentException("O molho não pode ficar vazio.");
        }
        this.molho = molho;
    }

    // POLIMORFISMO: Lasanha fornece sua própria apresentação de prato.
    @Override
    public String apresentarPrato() {
        return "- " + getNome() + " | Molho: " + molho + " | " + getDescricao();
    }
}

// HERANÇA: Risoto também é uma especialização de Prato.
class Risoto extends Prato {

    private String ingredientePrincipal;

    public Risoto(String nome, double preco, String descricao,
                  Nacionalidade nacionalidade, String ingredientePrincipal) {
        super(nome, preco, descricao, nacionalidade);
        setIngredientePrincipal(ingredientePrincipal);
    }

    public String getIngredientePrincipal() {
        return ingredientePrincipal;
    }

    public void setIngredientePrincipal(String ingredientePrincipal) {
        if (ingredientePrincipal == null || ingredientePrincipal.isBlank()) {
            throw new IllegalArgumentException("O ingrediente principal não pode ficar vazio.");
        }
        this.ingredientePrincipal = ingredientePrincipal;
    }

    // POLIMORFISMO: Risoto apresenta seu ingrediente principal.
    @Override
    public String apresentarPrato() {
        return "- " + getNome() + " | Ingrediente principal: "
                + ingredientePrincipal + " | " + getDescricao();
    }
}

// HERANÇA: Jantinha aproveita a estrutura geral definida por Prato.
class Jantinha extends Prato {

    private String acompanhamentos;

    public Jantinha(String nome, double preco, String descricao,
                    Nacionalidade nacionalidade, String acompanhamentos) {
        super(nome, preco, descricao, nacionalidade);
        setAcompanhamentos(acompanhamentos);
    }

    public String getAcompanhamentos() {
        return acompanhamentos;
    }

    public void setAcompanhamentos(String acompanhamentos) {
        if (acompanhamentos == null || acompanhamentos.isBlank()) {
            throw new IllegalArgumentException("Os acompanhamentos não podem ficar vazios.");
        }
        this.acompanhamentos = acompanhamentos;
    }

    // POLIMORFISMO: Jantinha destaca seus acompanhamentos.
    @Override
    public String apresentarPrato() {
        return "- " + getNome() + " | Acompanhamentos: "
                + acompanhamentos + " | " + getDescricao();
    }
}

// HERANÇA: Hamburguer recebe de Prato os dados comuns e adiciona sua variação.
class Hamburguer extends Prato {

    private TipoHamburguer tipo;
    private String ingredientes;

    public Hamburguer(String nome, double preco, String descricao,
                      Nacionalidade nacionalidade, TipoHamburguer tipo, String ingredientes) {
        super(nome, preco, descricao, nacionalidade);
        setTipo(tipo);
        setIngredientes(ingredientes);
    }

    public TipoHamburguer getTipo() {
        return tipo;
    }

    public void setTipo(TipoHamburguer tipo) {
        this.tipo = Objects.requireNonNull(tipo, "O tipo do hambúrguer não pode ser nulo.");
    }

    public String getIngredientes() {
        return ingredientes;
    }

    public void setIngredientes(String ingredientes) {
        if (ingredientes == null || ingredientes.isBlank()) {
            throw new IllegalArgumentException("Os ingredientes não podem ficar vazios.");
        }
        this.ingredientes = ingredientes;
    }

    // POLIMORFISMO: Hamburguer inclui a variação e os ingredientes em sua apresentação.
    @Override
    public String apresentarPrato() {
        return "- " + getNome() + " - " + tipo.getNomeExibicao()
                + " | Ingredientes: " + ingredientes;
    }
}

enum TipoHamburguer {
    TRADICIONAL("Tradicional"),
    BACON("Bacon"),
    ESPECIAL("Especial");

    private final String nomeExibicao;

    TipoHamburguer(String nomeExibicao) {
        this.nomeExibicao = nomeExibicao;
    }

    public String getNomeExibicao() {
        return nomeExibicao;
    }
}
