package br.com.cardapio.service;

import br.com.cardapio.domain.Hamburguer;
import br.com.cardapio.domain.Jantinha;
import br.com.cardapio.domain.Lasanha;
import br.com.cardapio.domain.Nacionalidade;
import br.com.cardapio.domain.Prato;
import br.com.cardapio.domain.Risoto;
import br.com.cardapio.dto.PratoRequest;
import br.com.cardapio.dto.PratoResponse;
import br.com.cardapio.exception.RecursoNaoEncontradoException;
import br.com.cardapio.exception.RegraNegocioException;
import br.com.cardapio.repository.NacionalidadeRepository;
import br.com.cardapio.repository.PratoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PratoService {

    private final PratoRepository pratoRepository;
    private final NacionalidadeRepository nacionalidadeRepository;

    public PratoService(PratoRepository pratoRepository,
                        NacionalidadeRepository nacionalidadeRepository) {
        this.pratoRepository = pratoRepository;
        this.nacionalidadeRepository = nacionalidadeRepository;
    }

    @Transactional(readOnly = true)
    public List<PratoResponse> listar(String nacionalidade, String busca) {
        String nacionalidadeNormalizada = normalizar(nacionalidade);
        String buscaNormalizada = normalizar(busca);

        List<Prato> pratos;
        if (nacionalidadeNormalizada != null && buscaNormalizada != null) {
            pratos = pratoRepository.pesquisarPorNacionalidadeETexto(
                    nacionalidadeNormalizada, buscaNormalizada);
        } else if (nacionalidadeNormalizada != null) {
            pratos = pratoRepository.findByNacionalidadeNomeIgnoreCaseOrderByIdAsc(
                    nacionalidadeNormalizada);
        } else if (buscaNormalizada != null) {
            pratos = pratoRepository.pesquisarPorTexto(buscaNormalizada);
        } else {
            pratos = pratoRepository.findAllByOrderByIdAsc();
        }

        return pratos.stream()
                .map(PratoResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public PratoResponse buscar(Long id) {
        return PratoResponse.from(buscarEntidade(id));
    }

    @Transactional
    public PratoResponse criar(PratoRequest request) {
        Nacionalidade nacionalidade = buscarNacionalidade(request.nacionalidade());
        Prato prato = switch (request.tipo()) {
            case LASANHA -> new Lasanha(request.nome(), request.descricao(), request.preco(),
                    nacionalidade, request.disponivel(), request.detalhe());
            case RISOTO -> new Risoto(request.nome(), request.descricao(), request.preco(),
                    nacionalidade, request.disponivel(), request.detalhe());
            case JANTINHA -> new Jantinha(request.nome(), request.descricao(), request.preco(),
                    nacionalidade, request.disponivel(), request.detalhe());
            case HAMBURGUER -> new Hamburguer(request.nome(), request.descricao(), request.preco(),
                    nacionalidade, request.disponivel(), request.detalhe());
        };
        return PratoResponse.from(pratoRepository.save(prato));
    }

    @Transactional
    public PratoResponse atualizar(Long id, PratoRequest request) {
        Prato prato = buscarEntidade(id);
        if (prato.getTipo() != request.tipo()) {
            throw new RegraNegocioException("O tipo do prato não pode ser alterado.");
        }

        prato.atualizarDados(request.nome(), request.descricao(), request.preco(),
                buscarNacionalidade(request.nacionalidade()), request.disponivel());
        prato.setDetalhe(request.detalhe());
        return PratoResponse.from(prato);
    }

    @Transactional
    public void excluir(Long id) {
        pratoRepository.delete(buscarEntidade(id));
    }

    private Prato buscarEntidade(Long id) {
        return pratoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Prato " + id + " não encontrado."));
    }

    private Nacionalidade buscarNacionalidade(String nome) {
        return nacionalidadeRepository.findByNomeIgnoreCase(nome.trim())
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Nacionalidade '" + nome.trim() + "' não encontrada."));
    }

    private String normalizar(String valor) {
        return valor == null || valor.isBlank() ? null : valor.trim();
    }
}
