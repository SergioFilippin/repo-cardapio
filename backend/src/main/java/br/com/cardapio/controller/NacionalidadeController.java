package br.com.cardapio.controller;

import br.com.cardapio.dto.NacionalidadeResponse;
import br.com.cardapio.service.NacionalidadeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/nacionalidades")
public class NacionalidadeController {

    private final NacionalidadeService service;

    public NacionalidadeController(NacionalidadeService service) {
        this.service = service;
    }

    @GetMapping
    public List<NacionalidadeResponse> listar() {
        return service.listar();
    }
}
