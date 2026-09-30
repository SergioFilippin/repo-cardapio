package br.com.cardapio.controller;

import br.com.cardapio.dto.PratoRequest;
import br.com.cardapio.dto.PratoResponse;
import br.com.cardapio.service.PratoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/pratos")
public class PratoController {

    private final PratoService service;

    public PratoController(PratoService service) {
        this.service = service;
    }

    @GetMapping
    public List<PratoResponse> listar(
            @RequestParam(required = false) String nacionalidade,
            @RequestParam(required = false) String busca) {
        return service.listar(nacionalidade, busca);
    }

    @GetMapping("/{id}")
    public PratoResponse buscar(@PathVariable Long id) {
        return service.buscar(id);
    }

    @PostMapping
    public ResponseEntity<PratoResponse> criar(@Valid @RequestBody PratoRequest request) {
        PratoResponse response = service.criar(request);
        return ResponseEntity.created(URI.create("/api/pratos/" + response.id())).body(response);
    }

    @PutMapping("/{id}")
    public PratoResponse atualizar(@PathVariable Long id,
                                   @Valid @RequestBody PratoRequest request) {
        return service.atualizar(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
