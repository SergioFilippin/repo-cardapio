package br.com.cardapio.service;

import br.com.cardapio.dto.NacionalidadeResponse;
import br.com.cardapio.repository.NacionalidadeRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class NacionalidadeService {

    private final NacionalidadeRepository repository;

    public NacionalidadeService(NacionalidadeRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<NacionalidadeResponse> listar() {
        return repository.findAll(Sort.by("nome")).stream()
                .map(NacionalidadeResponse::from)
                .toList();
    }
}
