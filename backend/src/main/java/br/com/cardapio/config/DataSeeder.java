package br.com.cardapio.config;

import br.com.cardapio.domain.Hamburguer;
import br.com.cardapio.domain.Jantinha;
import br.com.cardapio.domain.Lasanha;
import br.com.cardapio.domain.Nacionalidade;
import br.com.cardapio.domain.Prato;
import br.com.cardapio.domain.Risoto;
import br.com.cardapio.repository.NacionalidadeRepository;
import br.com.cardapio.repository.PratoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner carregarCardapio(NacionalidadeRepository nacionalidades,
                                        PratoRepository pratos) {
        return args -> {
            Nacionalidade italiana = obterNacionalidade("Italiana", nacionalidades);
            Nacionalidade brasileira = obterNacionalidade("Brasileira", nacionalidades);

            salvarSeAusente(new Lasanha(
                    "Lasanha ao molho branco com brócolis e bacon",
                    "Lasanha cremosa preparada com brócolis e bacon.",
                    new BigDecimal("42.90"), italiana, true, "Molho branco"), pratos);
            salvarSeAusente(new Lasanha(
                    "Lasanha tradicional",
                    "Lasanha com carne moída, queijo e molho de tomate.",
                    new BigDecimal("39.90"), italiana, true, "Molho à bolonhesa"), pratos);
            salvarSeAusente(new Risoto(
                    "Risoto de camarão",
                    "Arroz cremoso preparado com temperos frescos.",
                    new BigDecimal("49.90"), italiana, true, "Camarão"), pratos);
            salvarSeAusente(new Jantinha(
                    "Jantinha brasileira",
                    "Refeição típica servida em um prato completo.",
                    new BigDecimal("29.90"), brasileira, true,
                    "Arroz, feijão tropeiro, vinagrete e espetinho"), pratos);
            salvarSeAusente(new Hamburguer(
                    "Hambúrguer caseiro",
                    "Hambúrguer artesanal servido no pão.",
                    new BigDecimal("24.90"), brasileira, true,
                    "Tradicional: carne, queijo, alface e tomate"), pratos);
            salvarSeAusente(new Hamburguer(
                    "Hambúrguer caseiro",
                    "Hambúrguer artesanal servido no pão.",
                    new BigDecimal("27.90"), brasileira, true,
                    "Bacon: carne, queijo, bacon, alface e tomate"), pratos);
            salvarSeAusente(new Hamburguer(
                    "Hambúrguer caseiro",
                    "Hambúrguer artesanal servido no pão.",
                    new BigDecimal("31.90"), brasileira, true,
                    "Especial: duas carnes, queijo, bacon, ovo e salada"), pratos);
        };
    }

    private Nacionalidade obterNacionalidade(String nome, NacionalidadeRepository repository) {
        return repository.findByNomeIgnoreCase(nome)
                .orElseGet(() -> repository.save(new Nacionalidade(nome)));
    }

    private void salvarSeAusente(Prato prato, PratoRepository repository) {
        if (!repository.existsByNomeAndPreco(prato.getNome(), prato.getPreco())) {
            repository.save(prato);
        }
    }
}
