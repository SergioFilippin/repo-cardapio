package br.com.cardapio.repository;

import br.com.cardapio.domain.Nacionalidade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NacionalidadeRepository extends JpaRepository<Nacionalidade, Long> {
    Optional<Nacionalidade> findByNomeIgnoreCase(String nome);
}
