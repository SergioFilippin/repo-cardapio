package br.com.cardapio.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class PratoControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void deveListarCardapioInicial() throws Exception {
        mockMvc.perform(get("/api/pratos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(7)))
                .andExpect(jsonPath("$[0].id").isNumber())
                .andExpect(jsonPath("$[0].tipo").isString())
                .andExpect(jsonPath("$[0].detalhe").isNotEmpty())
                .andExpect(jsonPath("$[0].disponivel", is(true)));
    }

    @Test
    void deveFiltrarPorNacionalidadeEBusca() throws Exception {
        mockMvc.perform(get("/api/pratos")
                        .param("nacionalidade", "Italiana")
                        .param("busca", "lasanha"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].nacionalidade", everyItem(is("Italiana"))))
                .andExpect(jsonPath("$[*].tipo", everyItem(is("LASANHA"))));
    }

    @Test
    void deveRejeitarCriacaoInvalida() throws Exception {
        mockMvc.perform(post("/api/pratos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "tipo": "RISOTO",
                                  "nome": "",
                                  "descricao": "Novo prato",
                                  "preco": -1,
                                  "nacionalidade": "Italiana",
                                  "detalhe": "",
                                  "disponivel": true
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title", is("Dados inválidos")))
                .andExpect(jsonPath("$.erros.nome").exists())
                .andExpect(jsonPath("$.erros.preco").exists())
                .andExpect(jsonPath("$.erros.detalhe").exists());
    }

    @Test
    void deveCriarPratoValido() throws Exception {
        mockMvc.perform(post("/api/pratos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "tipo": "RISOTO",
                                  "nome": "Risoto de limão siciliano",
                                  "descricao": "Arroz arbóreo cremoso.",
                                  "preco": 45.50,
                                  "nacionalidade": "Italiana",
                                  "detalhe": "Limão siciliano",
                                  "disponivel": true
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", org.hamcrest.Matchers.matchesPattern("/api/pratos/\\d+")))
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.tipo", is("RISOTO")))
                .andExpect(jsonPath("$.preco", is(45.5)))
                .andExpect(jsonPath("$.nacionalidade", is("Italiana")))
                .andExpect(jsonPath("$.detalhe", is("Limão siciliano")));
    }
}
