package br.com.cardapio.repository;

import br.com.cardapio.domain.Prato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface PratoRepository extends JpaRepository<Prato, Long> {

    List<Prato> findAllByOrderByIdAsc();

    List<Prato> findByNacionalidadeNomeIgnoreCaseOrderByIdAsc(String nacionalidade);

    @Query("""
            select p from Prato p
            where lower(p.nome) like lower(concat('%', :busca, '%'))
               or lower(p.descricao) like lower(concat('%', :busca, '%'))
            order by p.id
            """)
    List<Prato> pesquisarPorTexto(@Param("busca") String busca);

    @Query("""
            select p from Prato p
            where lower(p.nacionalidade.nome) = lower(:nacionalidade)
              and (lower(p.nome) like lower(concat('%', :busca, '%'))
                   or lower(p.descricao) like lower(concat('%', :busca, '%')))
            order by p.id
            """)
    List<Prato> pesquisarPorNacionalidadeETexto(
            @Param("nacionalidade") String nacionalidade,
            @Param("busca") String busca);

    boolean existsByNomeAndPreco(String nome, BigDecimal preco);
}
